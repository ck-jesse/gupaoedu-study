package com.coy.gupaoedu.study.mybatis;

import com.coy.gupaoedu.study.mybatis.dao.BlogMapper;
import com.coy.gupaoedu.study.mybatis.model.Blog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenck
 * @date 2019/4/30 13:52
 */
public class MybatisTest {

    public static final String resource = "mybatis-config.xml";

    SqlSessionFactory sessionFactory;

    @Before
    public void initSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void selectTest() {
        SqlSession session = sessionFactory.openSession();
        // 动态代理创建Mapper接口的实现类
        BlogMapper blogMapper = session.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlog(1);
        System.out.println(blog);
    }

    /**
     * 本地缓存测试
     * MyBatis 利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。
     * 默认值为 SESSION，这种情况下会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不会共享数据
     */
    @Test
    public void localCacheTest() {
        SqlSession session = sessionFactory.openSession();

        // 第一个statement
        BlogMapper blogMapper = session.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlog(1);
        System.out.println(blog);

        // 第一个statement
        blogMapper = session.getMapper(BlogMapper.class);
        blog = blogMapper.selectBlog(1);
        System.out.println(blog);
    }

    /**
     * 缓存测试
     */
    @Test
    public void cacheTest() {
        SqlSession session = sessionFactory.openSession();
        // 第一个statement
        BlogMapper blogMapper = session.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlog(1);
        System.out.println(blog);

        SqlSession session1 = sessionFactory.openSession();
        // 第一个statement
        blogMapper = session1.getMapper(BlogMapper.class);
        blog = blogMapper.selectBlog(1);
        System.out.println(blog);
    }
}
