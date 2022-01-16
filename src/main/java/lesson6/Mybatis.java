package lesson6;

import db.dao.CategoriesMapper;
import db.dao.ProductsMapper;
import db.model.Categories;
import db.model.CategoriesExample;
import db.model.Products;
import db.model.ProductsExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.util.List;

public class Mybatis {
    public static final String resource = "mybatis-config.xml";

    public static void main(String[] args) throws IOException {
        System.out.println("Hail");

        SqlSessionFactory sqlSessionFactory;
        sqlSessionFactory=new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resource));
        SqlSession sqlSession=sqlSessionFactory.openSession(true);

        CategoriesMapper categoriesMapper = sqlSession.getMapper(CategoriesMapper.class);
        ProductsMapper productsMapper = sqlSession.getMapper(ProductsMapper.class);

        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
        System.out.println(categoriesCount);

        long productsCount = productsMapper.countByExample(new ProductsExample());
        System.out.println(productsCount);

        List<Categories> categoriesList=categoriesMapper.selectByExample(new CategoriesExample());
        /*
        for (Categories c: categoriesList) {
            System.out.println("Название - "+ c.getTitle() + " / Id - "+c.getId());
        }

         */

        Products getFoodById =  productsMapper.selectByPrimaryKey( (long)20442);
        System.out.println("By id  20442 - "+getFoodById.getTitle());


        Categories categories = categoriesMapper.selectByPrimaryKey(1);

        System.out.println(categories.getTitle());
    }



}
