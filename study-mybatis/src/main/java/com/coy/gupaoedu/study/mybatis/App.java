package com.coy.gupaoedu.study.mybatis;

import com.coy.gupaoedu.study.mybatis.mapper.BlogMapper;
import com.coy.gupaoedu.study.mybatis.model.Blog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 */
public class App {


    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sessionFactory.openSession();
        try {
            // 动态代理创建Mapper接口的实现类
            BlogMapper blogMapper = session.getMapper(BlogMapper.class);
            Blog blog = blogMapper.selectBlog(1);
            System.out.println(blog);
        } finally {
            session.close();
        }
    }
}
