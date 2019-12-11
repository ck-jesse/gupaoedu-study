package com.coy.gupaoedu.study.ldap;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/12/10 16:54
 */
public class LdapService {

    /**
     * 部门属性字段
     */
    public static final String[] DEPARTMENT_FIELDS = new String[]{"ou"};

    /**
     * 用户属性字段
     */
    public static final String[] USER_FIELDS = new String[]{"uid", "cn", "sn",
            "uidNumber", "gidNumber", "homeDirectory", "mobile", "mail", "userPassword"};

    /**
     * 创建部门
     * 注：LDAP 中 ou 对应部门
     *
     * @param dn     DN-识别名（等同于目录名），如：ou=技术中心,ou=花生日记,dc=ldapuser,dc=com
     * @param ouName 组名，可以是部门名称、部门id，如：大数据组
     */
    public void createOU(String dn, String ouName) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectClass", Arrays.asList("top", "organizationalunit"));
        map.put("ou", ouName);
        LdapUtil.create(dn, map);
    }

    /**
     * 创建用户
     *
     * @param dn        DN-识别名（等同于目录名），如：uid=1111,ou=技术中心,ou=花生日记,dc=ldapuser,dc=com
     * @param jobNumber 工号
     * @param userId    用户id
     * @param name      姓名
     * @param mobile    手机号
     * @param mail      邮箱
     * @param password  密码
     */
    public void createUser(String dn, String jobNumber, String userId, String name, String mobile, String mail, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectClass", Arrays.asList("top", "posixAccount", "inetOrgPerson"));
        map.put("uid", jobNumber);
        map.put("cn", name);//必填项
        map.put("sn", name);//必填项
        map.put("uidNumber", userId);//必填项
        map.put("gidNumber", userId);//必填项
        map.put("homeDirectory", "/home/user/" + jobNumber);//必填项
        map.put("mobile", mobile);
        map.put("mail", mail);
        map.put("userPassword", password);
        LdapUtil.create(dn, map);
    }

    /**
     * 创建用户
     */
    public void createUser(String dn, Map<String, Object> map) {
        if (!map.containsKey("objectClass")) {
            map.put("objectClass", Arrays.asList("top", "posixAccount", "inetOrgPerson"));
        }
        LdapUtil.create(dn, map);
    }

    /**
     * 修改条目属性
     * 注：此方法支持修改 部门或用户，条目是一个通用的说法。
     */
    public void update(String dn, Map<String, Object> attrMap) {
        LdapUtil.update(dn, attrMap);
    }

    /**
     * 删除条目 - 删除部门或用户
     */
    public void delete(String dn) {
        LdapUtil.delete(dn);
    }

    /**
     * 查询条目 - 可以是ou/cn/uid
     *
     * @param dn
     */
    public Map<String, Object> searchEntry(String dn, String[] attrIds) {
        return LdapUtil.searchEntry(dn, attrIds);
    }

    // 查询部门

    /**
     * 查询部门信息
     *
     * @param dn
     */
    public Map<String, Object> searchDepartment(String dn) {
        return searchEntry(dn, DEPARTMENT_FIELDS);
    }

    /**
     * 查询dn下所有部门列表
     */
    public List<Map<String, Object>> searchDepartmentList(String dn) {
        return searchDepartmentList(dn, null);
    }

    /**
     * 查询部门列表
     */
    public List<Map<String, Object>> searchDepartmentList(String dn, String departmentName) {
        StringBuilder filter = new StringBuilder("(&");
        filter.append("(objectClass=organizationalUnit)");
        if (StringUtils.isNotBlank(departmentName)) {
            filter.append(String.format("(ou=*%s*)", departmentName));
        }
        filter.append(")");

        List<Map<String, Object>> data = LdapUtil.searchEntryList(dn, filter.toString(), DEPARTMENT_FIELDS);
        System.out.println(data);
        return data;
    }


    // 查询员工

    /**
     * 查询员工信息
     *
     * @param dn
     */
    public Map<String, Object> searchEmployee(String dn) {
        return searchEntry(dn, USER_FIELDS);
    }

    /**
     * 查询dn下所有用户列表
     */
    public List<Map<String, Object>> searchEmployeeList(String dn) {
        return searchEmployeeList(dn, null, null, null, null, null);
    }

    /**
     * 根据工号查询
     */
    public List<Map<String, Object>> searchEmployeeListByJobNum(String dn, String jobNumber) {
        return searchEmployeeList(dn, jobNumber, null, null, null, null);
    }

    /**
     * 根据姓名查询
     */
    public List<Map<String, Object>> searchEmployeeListByName(String dn, String name) {
        return searchEmployeeList(dn, null, null, name, null, null);
    }

    /**
     * 根据手机号查询
     */
    public List<Map<String, Object>> searchEmployeeListByMobile(String dn, String mobile) {
        return searchEmployeeList(dn, null, null, null, mobile, null);
    }

    /**
     * 根据邮箱查询
     */
    public List<Map<String, Object>> searchEmployeeListByMail(String dn, String mail) {
        return searchEmployeeList(dn, null, null, null, null, mail);
    }

    /**
     * 查询员工列表
     *
     * @param dn
     * @param jobNumber
     * @param userId
     * @param name
     * @param mobile
     * @param mail
     */
    public List<Map<String, Object>> searchEmployeeList(String dn, String jobNumber, String userId, String name, String mobile, String mail) {
        // 组装过滤条件
        StringBuilder filter = new StringBuilder("(&");
        filter.append("(objectClass=inetOrgPerson)");
        if (StringUtils.isNotBlank(jobNumber)) {
            filter.append(String.format("(uid=*%s*)", jobNumber));
        }
        if (StringUtils.isNotBlank(userId)) {
            filter.append(String.format("(uidNumber=*%s*)", userId));
        }
        if (StringUtils.isNotBlank(name)) {
            filter.append(String.format("(cn=*%s*)", name));
        }
        if (StringUtils.isNotBlank(mobile)) {
            filter.append(String.format("(mobile=*%s*)", mobile));
        }
        if (StringUtils.isNotBlank(mail)) {
            filter.append(String.format("(mail=*%s*)", mail));
        }
        filter.append(")");
        List<Map<String, Object>> data = LdapUtil.searchEntryList(dn, filter.toString(), USER_FIELDS);
        return data;
    }

}
