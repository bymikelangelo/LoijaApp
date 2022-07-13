package com.example.loijaapp.retrofit;

import com.example.loijaapp.model.Material;
import com.example.loijaapp.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MaterialAPI {

    @GET("rest/materials")
    Call<List<Material>> listMaterials(@Header("Cookie") String jsessionid);

    @GET("rest/materials/list/productId/{productId}")
    Call<List<Material>> listMaterials(@Header("Cookie") String jsessionid, @Path("productId") int productId);

    @POST("rest/materials/listByProduct")
    Call<List<Material>> listMaterialsByProduct(@Header("Cookie") String jsessionid, @Body Product product);
}
