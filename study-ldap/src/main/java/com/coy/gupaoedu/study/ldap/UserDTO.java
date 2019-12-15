package com.coy.gupaoedu.study.ldap;

import lombok.Data;

/**
 * @author chenck
 * @date 2019/12/11 14:37
 */
@Data
public class UserDTO {
    /**
     * 工号
     */
    private String jobNumber;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 密码
     */
    private String password;
}
