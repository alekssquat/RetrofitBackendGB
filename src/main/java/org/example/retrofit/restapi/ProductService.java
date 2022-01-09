package org.example.retrofit.restapi;

import okhttp3.ResponseBody;
import org.example.retrofit.DTO.Product;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductService {
    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);


    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @PUT("products")
    Call<Product> putProduct (@Body Product createProductRequest);


    @GET("products")
    Call<Product> getProducts();

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);
}
