package com.example.loijaapp.retrofit;

import com.example.loijaapp.model.MyUser;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface HomeAPI {

    @GET("rest/validate")
    Call<String> validate (@Header("Cookie") String jsessionid);

    @GET("rest/login")
    Call<MyUser> login(@Header("Authorization") String authHeader, @Header("username") String username);

    @GET("rest/logout")
    Call<String> logout(@Header("Cookie") String jsessionid);
}
