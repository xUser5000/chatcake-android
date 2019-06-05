package com.stem.chatcake.service;

import android.content.Context;
import android.widget.Toast;

import com.stem.chatcake.http.Api;
import com.stem.chatcake.http.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpService {

    private static HttpService instance = null;

    private Api api;
    private final String BASE_URL = "http://192.168.1.4:3000/api/";
    private Retrofit retrofit;

    private HttpService () {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public static HttpService getInstance() {
        if (instance == null)
            instance = new HttpService();
        return instance;
    }

    private ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }

        return error;
    }

    public void showErrors (Context context, Response<?> response) {
        ApiError apiError = parseError(response);
        Toast.makeText(context, apiError.getErrors().get(0), Toast.LENGTH_SHORT).show();
    }

    public void showClientErrors (Context context, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, "Something went wrong.....", Toast.LENGTH_SHORT).show();
    }

    public Api getApi () {
        return api;
    }

}
