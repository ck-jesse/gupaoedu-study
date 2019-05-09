package com.coy.gupaoedu.study.mebatis.demo;

/**
 * @author chenck
 * @date 2019/5/9 14:19
 */
public interface UserMapper {
    /**
     * 查询博客信息
     */
    User selectUserById(int id, String userId);
}
