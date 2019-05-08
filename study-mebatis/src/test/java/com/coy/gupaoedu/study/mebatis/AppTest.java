package com.coy.gupaoedu.study.mebatis;

import com.coy.gupaoedu.study.mebatis.demo.Blog;
import com.coy.gupaoedu.study.mebatis.demo.BlogMapper;
import com.coy.gupaoedu.study.mebatis.executor.GPSimpleExecutor;
import com.coy.gupaoedu.study.mebatis.session.GPConfiguration;
import com.coy.gupaoedu.study.mebatis.session.GPDefaultSqlSession;
import com.coy.gupaoedu.study.mebatis.session.GPSqlSession;
import org.junit.Test;

public class AppTest {

    @Test
    public void test() {
        GPSqlSession sqlSession = new GPDefaultSqlSession(new GPConfiguration(), new GPSimpleExecutor(), false);
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlog(1);
        System.out.println(blog);
    }
}
