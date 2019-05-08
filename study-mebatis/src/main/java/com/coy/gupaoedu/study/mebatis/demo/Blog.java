package com.coy.gupaoedu.study.mebatis.demo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客
 *
 * @author chenck
 * @date 2019/4/27 21:15
 */
@Data
public class Blog implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 标题
     */
    private String blogTitle;
    /**
     * 内容
     */
    private String blogContent;
    /**
     * 作者
     */
    private String blogAuthor;
    /**
     * 状态 1 正常 2 删除
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createTime;
}
