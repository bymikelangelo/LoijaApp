package com.example.loijaapp.retrofit;

import com.example.loijaapp.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ProductAPI {

    @GET("rest/products")
    Call<List<Product>> listProducts(@Header("Cookie") String jsessionid);
}
