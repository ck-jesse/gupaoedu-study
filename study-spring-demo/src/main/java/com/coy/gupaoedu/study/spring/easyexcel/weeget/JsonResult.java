package com.coy.gupaoedu.study.spring.easyexcel.weeget;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class JsonResult<T> implements Serializable {

    private Integer resultCode;

    private String resultMsg;

    private String detailMsg;

    private T resultData;

    public static <T> JsonResult<T> build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static <T> JsonResult<T> fromJSONObject(JSONObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.containsKey("resultCode")) {
                Integer status = jsonObject.getInteger("resultCode");
                if (status == 0)
                    status = CodeEnums.SUCCESS.getCode();
                if (status == 1)
                    status = CodeEnums.ERROR.getCode();
                String msg = jsonObject.getString("resultMsg");
                Object data = jsonObject.get("resultData");
                return new JsonResult(status, msg, data);
            } else {
                return new JsonResult(CodeEnums.ERROR.getCode(), "不正确的数据格式", null);
            }

        } else {
            return new JsonResult(CodeEnums.SYS_SERVICE_DOWN.getCode(), CodeEnums.SYS_SERVICE_DOWN.getMsg(), null);
        }

    }

    public static <T> JsonResult<T> ok(T resultData) {
        return new JsonResult(resultData);
    }

    public static <T> JsonResult<T> ok() {
        return new JsonResult(null);
    }

    public static <T> JsonResult<T> build(Integer resultCode, String resultMsg) {
        return new JsonResult(resultCode, resultMsg, null);
    }

    public static <T> JsonResult<T> build(Integer resultCode, String resultMsg, String detailMsg) {
        return new JsonResult(resultCode, resultMsg, null, detailMsg);
    }

    public static <T> JsonResult<T> build(CodeEnums codeEnums) {
        return new JsonResult(codeEnums.getCode(), codeEnums.getMsg(), null);
    }

    public JsonResult(Integer resultCode, String resultMsg, T resultData) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resultData = resultData;
    }

    public JsonResult(Integer resultCode, String resultMsg, T resultData, String detailMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resultData = resultData;
        this.detailMsg = detailMsg;
    }

    public JsonResult(T resultData) {
        this.resultCode = CodeEnums.SUCCESS.getCode();
        this.resultMsg = "OK";
        this.resultData = resultData;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isSuccess() {
        return this.resultCode.intValue() == CodeEnums.SUCCESS.getCode().intValue();
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public T getDataSuc() {
        if (isSuccess()) {
            return this.resultData;
        }
        return null;
    }

    @Override
    public String toString() {
        return "JsonResult{" + "resultCode=" + resultCode + ", resultMsg='" + resultMsg + '\'' + ", resultData=" + resultData + '}';
    }

}
