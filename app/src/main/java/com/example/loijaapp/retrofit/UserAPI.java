package com.example.loijaapp.retrofit;

import com.example.loijaapp.model.MyUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPI {

    @GET("rest/users")
    Call<List<MyUser>> listUsers(@Header("Cookie") String jssesionid);

    @GET("rest/users/get/id/{id}")
    Call<MyUser> getById (@Header("Cookie") String jsessionid, @Path("id") int id);

    @GET("rest/users/get/username/{username}")
    Call<MyUser> getByUsername (@Header("Cookie") String jsessionid, @Path("username") String username);

    @POST("rest/users/save")
    Call<MyUser> save(@Header("Cookie") String jssesionid, @Body MyUser user);
}