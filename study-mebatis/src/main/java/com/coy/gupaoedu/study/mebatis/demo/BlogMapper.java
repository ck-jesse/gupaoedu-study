package com.coy.gupaoedu.study.mebatis.demo;

import java.util.List;

/**
 * Mapper接口
 *
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
