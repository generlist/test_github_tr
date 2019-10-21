package    com.linecorp.menu.validate.network.factory


import android.util.Log
import com.linecorp.menu.validate.network.annotation.Retry
import com.linecorp.menu.validate.network.config.LVNetworkPolicy
import com.linecorp.menu.validate.network.exception.LVNetworkError
import com.linecorp.menu.validate.network.exception.LVTimeoutError
import okhttp3.Request
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.*
import java.io.IOException
import java.lang.reflect.Type
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


/**
 * @autor hyunbung.shin@navercorp.com
 * Retries calls marked with {[Retry]}.
 */
internal class LVFilterCallAdapterFactory private constructor(private val mDefaultRetryPolicy: LVNetworkPolicy) : CallAdapter.Factory() {

    companion object {

        private val TAG = "RetryCallAdapterFactory"

        fun create(defaultRetryPolicy: LVNetworkPolicy): LVFilterCallAdapterFactory {
            return LVFilterCallAdapterFactory(
                defaultRetryPolicy
            )
        }
    }

    private val mExecutor: ScheduledExecutorService


    init {
        this.mExecutor = Executors.newScheduledThreadPool(1)
    }



    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        var hasRetryAnnotation = false
        var value = 0
        for (annotation in annotations) {
            if (annotation is Retry) {
                hasRetryAnnotation = true
                value = annotation.value
            }
        }
        val shouldRetryCall = hasRetryAnnotation
        val maxRetries = value
        // Look up the next call adapter which would otherwise be used if this one was not present.

        val delegate = retrofit.nextCallAdapter(this, returnType, annotations) as CallAdapter<Any, *>

        return object : CallAdapter<Any, Any> {
            override fun adapt(call: Call<Any>): Any {

                return delegate.adapt(
                    if (shouldRetryCall) RetryingCall(
                        call,
                        mExecutor,
                        mDefaultRetryPolicy
                    ) else call
                )
            }

            override fun responseType(): Type {
                return delegate.responseType()
            }
        }

    }


    private class RetryingCall<T>(
        private val mDelegate: Call<T>,
        private val mExecutor: ScheduledExecutorService,
        private val mDefaultRetryPolicy: LVNetworkPolicy
    ) : Call<T> {

        @Throws(IOException::class)
        override fun execute(): Response<T> {
            return mDelegate.execute()
        }

        override fun enqueue(callback: Callback<T>) {
            mDelegate.enqueue(
                RetryingCallback(
                    mDelegate,
                    callback,
                    mExecutor,
                    mDefaultRetryPolicy
                )
            )
        }

        override fun isExecuted(): Boolean {
            return mDelegate.isExecuted
        }

        override fun cancel() {
            mDelegate.cancel()
        }

        override fun isCanceled(): Boolean {
            return mDelegate.isCanceled
        }

        override/* Performing deep clone */ fun clone(): Call<T> {
            return RetryingCall(
                mDelegate.clone(),
                mExecutor,
                mDefaultRetryPolicy
            )
        }

        override fun request(): Request {
            return mDelegate.request()
        }
    }


    // Exponential backoff approach from https://developers.google.com/drive/web/handle-errors
    private class RetryingCallback<T> @JvmOverloads internal constructor(
        private val mCall: Call<T>,
        private val mDelegate: Callback<T>,
        private val mExecutor: ScheduledExecutorService,
        private val mDefaultRetryPolicy: LVNetworkPolicy,
        private val mRetries: Int = 0
    ) : Callback<T> {
        private val mMaxRetries: Int
        private val mCurrentTimeoutMs: Long
        private val mBackoffMultiplier: Float
        private val mRetryRequest = HashMap<String, Boolean>() //unknwonexception 관련

        init {
            this.mMaxRetries = mDefaultRetryPolicy.maxNumRetries
            this.mBackoffMultiplier = mDefaultRetryPolicy.backoffMultiplier
            this.mCurrentTimeoutMs = mDefaultRetryPolicy.currentTimeout


        }//바뀌는값

        override fun onResponse(call: Call<T>, response: Response<T>) {

                if (response.code() >= 400 && response.code() < 599) {
                    if (response.code() == 401 || response.code() == 403) {
                        attemptRetryOnException(call, null, response.code())
                    } else {
                    }
                } else {
                    if (response.code() != 200) {
                    }

                    mDelegate.onResponse(call, response)
                }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            // Retry failed request
            if (t is SocketTimeoutException ||
                t is ConnectTimeoutException ||
                t is MalformedURLException ||
                t is java.net.UnknownHostException
            ) {

                Log.d(TAG, "attemptRetryOnException : $t")
                attemptRetryOnException(call, t, 408)
            } else {
                mDelegate.onFailure(call, LVTimeoutError())
            }

        }

        private fun attemptRetryOnException(call: Call<T>, t: Throwable?, responseCode: Int) {

            if (mRetries <= mMaxRetries) {
                retryCall()
            } else {
                if (mRetries == mMaxRetries + 1) {
                    //unknownException 일경우는 다른 방식으로 retry 를 다시해본다.

                    if (t != null && t is java.net.UnknownHostException) {

                        mDelegate.onFailure(call, LVNetworkError(responseCode, t))

                    } else {
                        mDelegate.onFailure(call, LVNetworkError(responseCode, null))
                    }
                } else {
                    mDelegate.onFailure(call, LVNetworkError(responseCode, t))
                }
            }
        }

        private fun retryCall() {

            val time =
                (1 shl mRetries).toLong() * mBackoffMultiplier.toLong() * mCurrentTimeoutMs + random.nextInt(
                    1001
                )

            mExecutor.schedule({
                val call = mCall.clone()
                call.enqueue(
                    RetryingCallback(
                        call,
                        mDelegate,
                        mExecutor,
                        mDefaultRetryPolicy,
                        mRetries + 1
                    )
                )
            }, time, TimeUnit.MILLISECONDS)
        }

        companion object {
            private val random = Random()
        }


    }


}

