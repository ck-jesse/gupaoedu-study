package com.coy.gupaoedu.study.mebatis.build;

/**
 * @author chenck
 * @date 2019/5/8 14:45
 */
public enum MapperChildElementEnum {
    SELECT,
    UPDATE,
    INSERT,
    DELETE;

    public static MapperChildElementEnum find(String statementName) {
        for (MapperChildElementEnum statementEnum : MapperChildElementEnum.values()) {
            if (statementEnum.name().equalsIgnoreCase(statementName)) {
                return statementEnum;
            }
        }
        return null;
    }

    public static boolean exist(String statementName) {
        MapperChildElementEnum statementEnum = find(statementName);
        return null == statementEnum ? false : true;
    }
}
