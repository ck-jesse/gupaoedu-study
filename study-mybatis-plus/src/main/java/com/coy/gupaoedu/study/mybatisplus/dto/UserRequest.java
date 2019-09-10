package com.coy.gupaoedu.study.mybatisplus.dto;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVContains;
import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVLength;
import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVPattern;
import com.coy.gupaoedu.study.mybatisplus.enums.ResultEnum;
import com.coy.gupaoedu.study.mybatisplus.enums.SexEnum;
import com.coy.gupaoedu.study.mybatisplus.controller.group.GroupA;
import com.coy.gupaoedu.study.mybatisplus.controller.group.GroupB;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class UserRequest implements Serializable {

    public static final String yyyyMMddHHmmss = "^(\\d{4})(0\\d{1}|1[0-2])(0\\d{1}|[12]\\d{1}|3[01])" +
            "(0\\d{1}|1\\d{1}|2[0-3])(0\\d{1}|[1-5]\\d{1})(0\\d{1}|[1-5]\\d{1})$";
    public static final String yyyy_MM_dd_HH_mm_ss = "^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01]) " +
            "(0\\d{1}|1\\d{1}|2[0-3]):(0\\d{1}|[1-5]\\d{1}):(0\\d{1}|[1-5]\\d{1})$";

    @NotBlank(message = "姓名必须非null，且长度必须大于0")
    @Length(min = 6, max = 6, message = "GroupB 姓名长度超出限制", groups = GroupB.class)
    private String name;

    // 被注释的元素必须在合适的范围内
    @Range(min = 1, max = 100, message = "GroupB time超出限制", groups = GroupB.class)
    private Long time;

    @NotNull(message = "addr不能为null")
    @NotEmpty(message = "addr必须非空")
    @Size(min = 10, max = 300, message = "GroupA 地址大小必须在指定的范围内", groups = GroupA.class)
    private String addr;

    @NotNull(message = "map不能为null")
    @Size(min = 2, max = 20, message = "GroupB map大小必须在指定的范围内", groups = GroupB.class)
    private Map<String, String> map;

    @NotNull(message = "list不能为null")
    @Size(min = 3, max = 30, message = "GroupA list大小必须在指定的范围内", groups = GroupA.class)
    private List<String> list;

    @Min(value = 1, message = "年龄必须大于等于指定的最小值")
    @Max(value = 150, message = "年龄必须小于等于指定的最大值")
    @BVLength(min = 3, max = 3, message = "年龄长度必须为1-3", groups = GroupB.class)
    private int age;

    @Email(message = "电子邮箱地址格式不正确")
    @BVLength(min = 6, max = 32, message = "电子邮箱地址长度不合法", groups = GroupB.class)
    private String email;

    @Past(message = "生日必须是一个过去的日期")
    private Date birthday;

    @Pattern(regexp = yyyyMMddHHmmss, message = "startDate格式必须为yyyyMMddHHmmss")
    // 多值约束，对于同一个目标元素，在进行约束注解声明时可以同时使用不同的属性达到对该目标元素进行多值验证的目的。
    @Pattern.List(value = {
            @Pattern(regexp = yyyyMMddHHmmss, message = "startDate格式必须为yyyyMMddHHmmss"),
//            @Pattern(regexp = yyyy_MM_dd_HH_mm_ss, message = "startDate格式必须为yyyy_MM_dd_HH_mm_ss")
    })
    private String startDate;

    @BVPattern(regexp = yyyyMMddHHmmss, message = "endDate格式必须为yyyyMMddHHmmss")
    private String endDate;

    // 级联验证
    @Valid
    private CascadeRequest cascadeInput;

    // 自定义约束注解@Contain
    @BVContains(type = BVContains.Type.ENUM, enums = SexEnum.class, message = "性别不合法", groups = GroupA.class)
    private int sex;

    @BVContains(type = BVContains.Type.NUMBER, numberDicts = {0, 1}, message = "性别1不合法", groups = GroupA.class)
    private int sex1;

    @BVContains(type = BVContains.Type.CHAR_SEQUENCE, charDicts = {"0", "1"}, message = "性别2不合法", groups = {Default.class,
            GroupA.class})
    private String sex2;

    @BVContains(type = BVContains.Type.ENUM, enums = ResultEnum.class, message = "性别3不合法", groups = GroupA.class)
    private String sex3;

    // 验证 Number 和 String 的构成是否合法
    // 限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction
    // integer表示整数部分的精度，fraction表示小数部分的精度
    @Digits(integer = 3, fraction = 2, message = "金额格式不合法", groups = GroupB.class)
    private BigDecimal amount;// 金额

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public CascadeRequest getCascadeInput() {
        return cascadeInput;
    }

    public void setCascadeInput(CascadeRequest cascadeInput) {
        this.cascadeInput = cascadeInput;
    }

    public int getSex1() {
        return sex1;
    }

    public void setSex1(int sex1) {
        this.sex1 = sex1;
    }

    public String getSex2() {
        return sex2;
    }

    public void setSex2(String sex2) {
        this.sex2 = sex2;
    }

    public String getSex3() {
        return sex3;
    }

    public void setSex3(String sex3) {
        this.sex3 = sex3;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
