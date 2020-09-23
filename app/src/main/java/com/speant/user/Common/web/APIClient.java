package com.speant.user.Common.web;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.speant.user.Common.CONST.BASE_URL;


/**
 * Created by sangavi on 05/01/17.
 */


public class APIClient {

    public static String REQUEST = "REQUEST";
    public static String RESPONSE = "RESPONSE";

    public static Retrofit getClient() {

        Retrofit retrofit;

       /* LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .request(REQUEST)
                .response(RESPONSE)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).client(okHttpClient).build();

        return retrofit;*/


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;

    }

    public static Retrofit getPlacesClient() {

        Retrofit retrofit;
        String API_BASE_URL = "https://maps.googleapis.com/maps/api/";  //Global

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;


    }

}






