package com.ldap_user.prj.ldap;

import com.coy.gupaoedu.study.ldap.DepartmentDTO;
import com.coy.gupaoedu.study.ldap.LdapUtil;
import com.coy.gupaoedu.study.ldap.UserDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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
     * 修改部门属性
     */
    public void updateOU(String dn, String ouName) {
        Map<String, Object> map = new HashMap<>();
        map.put("ou", ouName);
        update(dn, map);
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
        UserDTO userDTO = new UserDTO();
        userDTO.setJobNumber(jobNumber);
        userDTO.setUserId(userId);
        userDTO.setName(name);
        userDTO.setMobile(mobile);
        userDTO.setMail(mail);
        userDTO.setPassword(password);

        createUser(dn, userDTO);
    }

    /**
     * 创建用户
     */
    public void createUser(String dn, UserDTO userDTO) {
        Map<String, Object> map = userDTOToMap(userDTO);
        map.put("objectClass", Arrays.asList("top", "posixAccount", "inetOrgPerson"));
        if (StringUtils.isNotBlank(userDTO.getJobNumber())) {
            map.put("homeDirectory", "/home/user/" + userDTO.getJobNumber());//必填项
        }
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
     * 修改用户属性
     */
    public void updateUser(String dn, UserDTO userDTO) {
        Map<String, Object> map = userDTOToMap(userDTO);
        update(dn, map);
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
     * @param dn DN-识别名
     */
    public Map<String, Object> searchEntry(String dn, String[] attrIds) {
        return LdapUtil.searchEntry(dn, attrIds);
    }

    // 查询部门

    /**
     * 查询部门信息
     *
     * @param dn DN-识别名
     */
    public DepartmentDTO searchDepartment(String dn) {
        Map<String, Object> map = searchEntry(dn, DEPARTMENT_FIELDS);
        DepartmentDTO departmentDTO = convertToDepartmentDTO(map);
        return departmentDTO;
    }

    /**
     * 查询dn下所有部门列表
     *
     * @param dn          DN-识别名
     * @param searchScope 搜索范围；0-仅搜索当前节点命名的对象；1-仅搜索当前节点的下一级命名对象；2-搜索当前节点为根的整个子树
     */
    public List<DepartmentDTO> searchDepartmentList(String dn, int searchScope) {
        return searchDepartmentList(dn, searchScope, null);
    }

    /**
     * 查询部门列表
     *
     * @param dn             DN-识别名
     * @param searchScope    搜索范围；0-仅搜索当前节点命名的对象；1-仅搜索当前节点的下一级命名对象；2-搜索当前节点为根的整个子树
     * @param departmentName 部门名称
     */
    public List<DepartmentDTO> searchDepartmentList(String dn, int searchScope, String departmentName) {
        StringBuilder filter = new StringBuilder("(&");
        filter.append("(objectClass=organizationalUnit)");
        if (StringUtils.isNotBlank(departmentName)) {
            filter.append(String.format("(ou=*%s*)", departmentName));
        }
        filter.append(")");

        List<Map<String, Object>> list = LdapUtil.searchEntryList(dn, filter.toString(), searchScope, DEPARTMENT_FIELDS);

        List<DepartmentDTO> deptList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            deptList.add(convertToDepartmentDTO(map));
        }
        return deptList;
    }


    // 查询员工

    /**
     * 查询员工信息
     *
     * @param dn DN-识别名
     */
    public UserDTO searchEmployee(String dn) {
        Map<String, Object> map = searchEntry(dn, USER_FIELDS);
        UserDTO userDTO = convertToUserDTO(map);
        return userDTO;
    }

    /**
     * 查询dn下的用户列表
     *
     * @param dn          DN-识别名
     * @param searchScope 搜索范围；0-仅搜索当前节点命名的对象；1-仅搜索当前节点的下一级命名对象；2-搜索当前节点为根的整个子树
     */
    public List<UserDTO> searchEmployeeList(String dn, int searchScope) {
        return searchEmployeeList(dn, searchScope, null, null, null, null, null);
    }

    /**
     * 查询员工列表
     *
     * @param dn          DN-识别名
     * @param searchScope 搜索范围；0-仅搜索当前节点命名的对象；1-仅搜索当前节点的下一级命名对象；2-搜索当前节点为根的整个子树
     * @param jobNumber
     * @param userId
     * @param name
     * @param mobile
     * @param mail
     */
    public List<UserDTO> searchEmployeeList(String dn, int searchScope, String jobNumber, String userId, String name, String mobile, String mail) {
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
        List<Map<String, Object>> list = LdapUtil.searchEntryList(dn, filter.toString(), searchScope, USER_FIELDS);

        List<UserDTO> userDTOList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            userDTOList.add(convertToUserDTO(map));
        }
        return userDTOList;
    }

    /**
     * UserDTO 转 map
     */
    public Map<String, Object> userDTOToMap(UserDTO userDTO) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(userDTO.getJobNumber())) {
            map.put("uid", userDTO.getJobNumber());
        }
        if (StringUtils.isNotBlank(userDTO.getName())) {
            map.put("cn", userDTO.getName());//必填项
            map.put("sn", userDTO.getName());//必填项
        }
        if (StringUtils.isNotBlank(userDTO.getUserId())) {
            map.put("uidNumber", userDTO.getUserId());//必填项
            map.put("gidNumber", userDTO.getUserId());//必填项
        }
        if (StringUtils.isNotBlank(userDTO.getMobile())) {
            map.put("mobile", userDTO.getMobile());
        }
        if (StringUtils.isNotBlank(userDTO.getMail())) {
            map.put("mail", userDTO.getMail());
        }
        if (StringUtils.isNotBlank(userDTO.getPassword())) {
            map.put("userPassword", userDTO.getPassword());
        }
        return map;
    }

    /**
     * map 转 DepartmentDTO
     */
    public DepartmentDTO convertToDepartmentDTO(Map<String, Object> map) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(convert(map.get("ou")));
        return departmentDTO;
    }

    /**
     * map 转 UserDTO
     */
    public UserDTO convertToUserDTO(Map<String, Object> map) {
        UserDTO userDTO = new UserDTO();
        userDTO.setJobNumber(convert(map.get("uid")));
        userDTO.setUserId(convert(map.get("uidNumber")));
        userDTO.setName(convert(map.get("cn")));
        userDTO.setMobile(convert(map.get("mobile")));
        userDTO.setMail(convert(map.get("mail")));
        userDTO.setPassword(convert(map.get("userPassword")));
        return userDTO;
    }

    private String convert(Object field) {
        if (null == field) {
            return "";
        }
        if (field instanceof byte[]) {
            return new String((byte[]) field);
        }
        return field.toString();
    }
}
