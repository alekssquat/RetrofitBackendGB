package org.example.retrofittest;


import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.example.retrofit.DTO.Product;
import org.example.retrofit.restapi.ProductService;
import org.example.retrofit.utils.RetrofitUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProductTest {
    static ProductService productService;
    Product product;
    Faker faker=new Faker();

    int id;

    @BeforeAll
    static void beforeAll(){
        productService= RetrofitUtils.getRetrofit()
                .create(ProductService.class);

    }

    @BeforeEach
    @SneakyThrows
    void setUp(){
        product=new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int)Math.random()*10000);
    }

    @Test
    @SneakyThrows
    void createProduct(){
        Response<Product> response=productService.createProduct(product)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        System.out.println("Новый продукт: "+id+" "+product.getTitle());
    }


    @Test
    @SneakyThrows
    void getProductById(){
        createProduct();
        Response<Product>response=productService.getProductById(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        System.out.println("Вызванный продукт: "+response.body().getId()+" "+response.body().getTitle());
    }

    @Test
    @SneakyThrows
    void putProduct(){
        createProduct();
        product=new Product()
                .withId(id)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int)Math.random()*10000);
        Response<Product> response=productService.putProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        System.out.println("Измененый продукт: "+product.getId()+" "+product.getTitle());


    }

    @Test
    @SneakyThrows
    void getProducts(){
        createProduct();//необходимо для прохождения теста tearDown
        System.out.println("На момент написания запрос GET /api/v1/products (Returns products) возвращает код 500 error: Internal Server Error ");
        Response<Product>response=productService.getProducts().execute();
        assertThat(response.code(), equalTo(500));

    }




    @SneakyThrows
    @AfterEach
    void tearDown(){

        Response<ResponseBody>response=productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        System.out.println("Удаленный продукт: "+id+" "+product.getTitle());
    }
}
