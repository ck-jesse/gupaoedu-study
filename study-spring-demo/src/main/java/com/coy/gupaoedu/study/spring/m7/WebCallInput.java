package com.coy.gupaoedu.study.spring.m7;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author chenck
 * @date 2023/3/17 16:17
 */
@Data
public class WebCallInput {

    @JSONField(name = "Action")
    private String Action = "Webcall";

    @JSONField(name = "ServiceNo")
    private String ServiceNo;

    @JSONField(name = "Exten")
    private String Exten;

    @JSONField(name = "Variable")
    private String Variable;
}
