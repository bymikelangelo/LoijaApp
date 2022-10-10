package com.example.loijaapp.retrofit;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.loijaapp.utils.Tags;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit;
    private static String baseUrl = "http://10.0.2.2:8080";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit;
        }
        else {
            return retrofit;
        }
    }

    public static Retrofit getInstance(Context context) {
        String IPAddress, port;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Tags.PREFERENCES,
                MODE_PRIVATE);
        IPAddress = sharedPreferences.getString(Tags.IP_ADDRESS, "localhost");
        port = sharedPreferences.getString(Tags.PORT, "8080");
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + IPAddress + ":" + port)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit;
        }
        else {
            return retrofit;
        }
    }

    public static void delete() {
        retrofit = null;
    }

}
