package com.coy.gupaoedu.study.mybatisplus.common.util;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 木子
 * @version 1.0.0
 * @ClassName BeanMapper.java
 * @Description TODO
 * @createTime 2019年09月04日 16:18:00
 */
public class BeanMapper {
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	public static <T> T map(Object source, Class<T> destinationClass) {
		if (source == null) {
			return null;
		}
		return dozer.map(source, destinationClass);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
		List<T> destinationList = new ArrayList();
		if (sourceList == null) {
			return destinationList;
		}
		for (Object sourceObject : sourceList) {
			if (sourceObject != null) {
				T destinationObject = dozer.map(sourceObject, destinationClass);
				destinationList.add(destinationObject);
			}
		}
		return destinationList;
	}
}
