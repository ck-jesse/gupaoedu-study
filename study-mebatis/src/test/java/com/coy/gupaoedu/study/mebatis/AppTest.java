package com.coy.gupaoedu.study.mebatis;

import com.coy.gupaoedu.study.mebatis.demo.Blog;
import com.coy.gupaoedu.study.mebatis.demo.BlogMapper;
import com.coy.gupaoedu.study.mebatis.demo.User;
import com.coy.gupaoedu.study.mebatis.demo.UserMapper;
import com.coy.gupaoedu.study.mebatis.session.GPSqlSession;
import com.coy.gupaoedu.study.mebatis.session.GPSqlSessionFactory;
import org.junit.Test;

public class AppTest {

    @Test
    public void test() {
        GPSqlSessionFactory sqlSessionFactory = new GPSqlSessionFactory().build();
        GPSqlSession sqlSession = sqlSessionFactory.openSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlog(1);
        System.out.println(blog);
        System.out.println();

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectUserById(1, "1001");
        System.out.println(user);
    }
}
