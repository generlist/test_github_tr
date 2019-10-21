package com.linecorp.menu.validate.network.interceptor;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor of HEAD
 *
 * @author sinhyeonbong on 2017. 5. 23..
 */

 class LVResponseHeaderInterceptor implements Interceptor {

    private static final String TAG ="LVResponseHeaderInterceptor";
    private HashMap<String, String> mHeaderFields = null;

    public LVResponseHeaderInterceptor(HashMap<String, String> headerFields) {
        this.mHeaderFields = headerFields;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder newBuilder = originalRequest.newBuilder();


        try {

            if (mHeaderFields != null) {
                for (String headerName : mHeaderFields.keySet()) {
                    newBuilder.header(headerName, mHeaderFields.get(headerName));
                }
            }

            newBuilder.method(originalRequest.method(), originalRequest.body());
        } catch (Exception e) {

        }

        Response response = chain.proceed(newBuilder.build());

        return response;
    }
}