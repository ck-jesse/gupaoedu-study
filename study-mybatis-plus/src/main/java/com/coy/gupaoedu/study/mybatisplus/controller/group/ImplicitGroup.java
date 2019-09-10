package com.coy.gupaoedu.study.mybatisplus.controller.group;


import javax.validation.constraints.NotBlank;

/**
 * 隐式定义的组接口<br>
 * 注：由于组必须是接口，所以隐式组只能针对方法，如get方法<br>
 * 
 * @author chenck 2016年11月17日
 */
public interface ImplicitGroup {

    @NotBlank String getName();

}
