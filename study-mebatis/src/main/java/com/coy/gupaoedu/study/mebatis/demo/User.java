package com.coy.gupaoedu.study.mebatis.demo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenck
 * @date 2019/5/9 14:19
 */
@Data
public class User implements Serializable {

    private Long id;
    private String userId;
    private String name;
    private String addr;
    private String createTime;
}
