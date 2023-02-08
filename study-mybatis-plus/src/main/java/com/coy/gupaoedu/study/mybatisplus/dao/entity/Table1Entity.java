package com.coy.gupaoedu.study.mybatisplus.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author chenck
 * @date 2023/2/7 13:09
 */
@Data
@TableName("table1")
public class Table1Entity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String group_name;
    private String group_id;
    private String group_org_name;
    private String father_group_name;
    private String father_group_id;
    private Integer root;
    private Integer lvl;
}
