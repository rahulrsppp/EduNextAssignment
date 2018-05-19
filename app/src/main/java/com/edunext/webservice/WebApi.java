package com.edunext.webservice;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebApi {

    private static final String API_URL = "http://edunext.biz/";

    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();


    private static final WebService SERVICE = createRetrofit().create(WebService.class);

    public static WebService getService() {
        return SERVICE;
    }

    private static Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL)
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

}
