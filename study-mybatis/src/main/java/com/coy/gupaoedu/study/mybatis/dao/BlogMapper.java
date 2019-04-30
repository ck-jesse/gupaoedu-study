package com.coy.gupaoedu.study.mybatis.dao;

import com.coy.gupaoedu.study.mybatis.model.Blog;

import java.util.List;

/**
 * @author chenck
 * @date 2019/4/27 21:14
 */
public interface BlogMapper {
    /**
     * 查询博客信息
     */
    Blog selectBlog(int id);

    List<Blog> selectBlogList(Blog blog);
}
