package com.example.loijaapp.retrofit;

import com.example.loijaapp.model.FactoryOrder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FactoryOrderAPI {

    @GET("rest/fOrders")
    public Call<List<FactoryOrder>> listOrders(@Header("Cookie") String jsessionid);

    @POST("rest/fOrders/save")
    public Call<FactoryOrder> save(@Header("Cookie") String jsessionid, @Body FactoryOrder order);

    @POST("rest/fOrders/completed")
    public Call<FactoryOrder> completed(@Header("Cookie") String jsessionid, @Body FactoryOrder order);
}
