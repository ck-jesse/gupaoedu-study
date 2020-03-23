package com.coy.gupaoedu.study.spring.easyexcel.web;

/**
 * @author chenck
 * @date 2020/3/23 12:01
 */

import lombok.Data;

import java.util.Date;

/**
 * 基础数据类
 *
 * @author Jiaju Zhuang
 **/
@Data
public class UploadData {
    private String string;
    private Date date;
    private Double doubleData;
}
