package org.example.retrofittest;

import db.dao.CategoriesMapper;
import db.dao.ProductsMapper;
import db.model.Categories;
import db.model.CategoriesExample;
import db.model.Products;
import db.model.ProductsExample;
import lesson6.Mybatis;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.checkerframework.checker.index.qual.LessThan;
import org.example.retrofit.DTO.GetCategoryResponse;
import org.example.retrofit.restapi.CategoryService;
import org.example.retrofit.utils.RetrofitUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class MyBatisTest {


    static CategoryService categoryService;

    static CategoriesMapper categoriesMapper;
    static ProductsMapper productsMapper;

    Products product;

    @BeforeAll
    static void beforeAll() throws IOException {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);

        SqlSessionFactory sqlSessionFactory;
        sqlSessionFactory=new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(Mybatis.resource));
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        categoriesMapper = sqlSession.getMapper(CategoriesMapper.class);
        productsMapper = sqlSession.getMapper(ProductsMapper.class);
    }

    @SneakyThrows
    @Test
    void getCategoryByIdPositiveTest(){

        Categories categories = categoriesMapper.selectByPrimaryKey(1);

        Response<GetCategoryResponse>response=categoryService.getCategory(1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(categories.getTitle(), equalTo("Food"));
        System.out.println("Category id - 1 / category title - "+categories.getTitle());
    }

    @Test
    void getCategoryList(){

        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
        System.out.println("Total number of categories - "+categoriesCount);
/*
        List<Categories> categoriesList=categoriesMapper.selectByExample(new CategoriesExample());
        for (Categories c: categoriesList) {
            System.out.println("Category - "+ c.getTitle() + " : Id - "+c.getId());
        }

 */
        assertThat(categoriesCount, equalTo(52L));

    }

    @Test
    void getBananaById(){
        product =  productsMapper.selectByPrimaryKey((long)17705);
        System.out.println("By id  17705 - "+product.getTitle()+" Price - "+product.getPrice());
        assertThat(product.getTitle(), equalTo("Banana"));

    }

    @Test
    void getProductList(){
        long productsCount = productsMapper.countByExample(new ProductsExample());
        System.out.println("Total products - "+productsCount);
        assert(productsCount>1500);
/*
        List<Products> categoriesList=productsMapper.selectByExample(new ProductsExample());
        for (Products c: categoriesList) {
            System.out.println("Category - "+ c.getTitle() + " : Id - "+c.getId());
        }

 */
    }
    @Test
    void deleteById(){
        long productId=18547; //пока харкод, так и не разобрался как создавать новые записи

        long productsCountBefore = productsMapper.countByExample(new ProductsExample());
        System.out.println("Total products before - "+productsCountBefore);
        product =  productsMapper.selectByPrimaryKey(productId);
        System.out.println("Title - "+product.getTitle()+" Price - "+product.getPrice());
        int item =  productsMapper.deleteByPrimaryKey(productId);
        System.out.println(item);

        long productsCountAfter = productsMapper.countByExample(new ProductsExample());
        System.out.println("Total products after - "+productsCountAfter);

        assert (productsCountBefore>productsCountAfter);
    }

}
