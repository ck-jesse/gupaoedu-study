package com.coy.gupaoedu.study.serialization.proto;

import io.protostuff.Tag;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenck
 * @date 2019/10/21 10:05
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    // 用户ID
    @Tag(1)
    private int userId;

    // 用户类型
    @Tag(2)
    private int userTypeId;

    // 用户名
    @Tag(3)
    private String userName;

    // 创建时间
    @Tag(4)
    private Date createDateTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }
}
