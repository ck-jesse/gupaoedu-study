package com.coy.gupaoedu.study.mybatis.mapper;

import com.coy.gupaoedu.study.mybatis.model.Blog;

/**
 * @author chenck
 * @date 2019/4/27 21:14
 */
public interface BlogMapper {
    Blog selectBlog(int id);
}
