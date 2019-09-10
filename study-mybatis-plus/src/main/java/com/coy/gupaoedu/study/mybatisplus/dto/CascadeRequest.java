package com.coy.gupaoedu.study.mybatisplus.dto;


import javax.validation.constraints.NotBlank;

public class CascadeRequest {

    @NotBlank(message = "cascade 不能为空")
    private String cascade;

    public String getCascade() {
        return cascade;
    }

    public void setCascade(String cascade) {
        this.cascade = cascade;
    }

}
