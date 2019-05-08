package com.coy.gupaoedu.study.mebatis;

import java.sql.ResultSet;

/**
 * @author chenck
 * @date 2019/5/7 17:00
 */
public enum GPResultSetType {
    FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
    SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
    SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);

    private final int value;

    GPResultSetType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
