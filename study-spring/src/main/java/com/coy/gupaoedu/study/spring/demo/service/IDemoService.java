package com.coy.gupaoedu.study.spring.demo.service;


import com.coy.gupaoedu.study.spring.framework.beans.GPInitializingBean;

public interface IDemoService extends GPInitializingBean {
	
	String get(String name);
	
}
