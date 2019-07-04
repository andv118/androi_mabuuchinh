package com.htn.dovanan.mabuuchinh.res;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResClient {
        public static final String BASE_API = "http://techgarden.vn/api_mbc_update/";
//    public static final String BASE_API = "http://192.168.0.102:8080/api_mabuuchinh/json/";

    public static Retrofit retrofit;

    public static APIs getAPIs() {
        // cau hinh retrofit voi server
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            // khoi tao retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(APIs.class);
    }

}
