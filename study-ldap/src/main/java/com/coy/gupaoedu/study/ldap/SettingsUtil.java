package com.coy.gupaoedu.study.ldap;

/**
 * @author chenck
 * @date 2019/12/10 18:44
 */
public class SettingsUtil {

    // LDAP 地址
    public static String LDAP_ADDRESS = "ldap://172.18.66.85:389";
    // LDAP 身份验证方式，simple简单身份验证
    public static String LDAP_AUTH = "simple";
    // LDAP 账号
    public static String LDAP_BINDDN = "cn=Manager,dc=ldapuser,dc=com";
    // LDAP 密码
    public static String LDAP_PASSWORD = "ldap@123";
}
