package com.linecorp.menu.validate.network.factory;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author hyunbung.shin@navercorp.com
 */

public class ToStringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain;charset=utf-8");

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

         boolean httpPost = false;
        if(annotations !=null ){

            for (Annotation annotation : annotations) {
                if (annotation instanceof retrofit2.http.FieldMap) {
                    httpPost = true;
                    break;
                }
            }
        }

        if (httpPost) {
            return null;
        } else {
            if (String.class.equals(type)) {
                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        return value.string();
                    }
                };
            }

        }
        return null;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {


        boolean isgZip = false;
        for (Annotation annotation : annotations) {
            if (annotation instanceof retrofit2.http.Headers) {
                String[] headerValue = ((retrofit2.http.Headers) annotation).value();
                for(String value: headerValue){
                    if(value.contains("gzip")){
                        isgZip= true;
                        break;
                    }
                }

            }
        }
        if (String.class.equals(type)) {

                return new Converter<ResponseBody, Object>() {

                    @Override
                    public Object convert(ResponseBody responseBody) throws IOException {
                        return responseBody.string();
                    }
                };
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

        if (String.class.equals(type)) {
            return new Converter<String, RequestBody>() {

                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        }
        return null;
    }


}

