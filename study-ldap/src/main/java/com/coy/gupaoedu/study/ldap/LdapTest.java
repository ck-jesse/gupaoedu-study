package com.coy.gupaoedu.study.ldap;

import org.junit.Before;
import org.junit.Test;

import javax.naming.directory.SearchControls;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LDAP 测试
 * <p>
 * 参考：https://www.cnblogs.com/oolnc/p/8794684.html
 *
 * @author chenck
 * @date 2019/12/10 10:45
 */
public class LdapTest {

    // LDAP 测试环境的配置
    public static String LDAP_ADDRESS = "ldap://172.18.66.85:389";
    public static String LDAP_AUTH = "simple";
    public static String LDAP_BINDDN = "cn=Manager,dc=ldapuser,dc=com";
    public static String LDAP_PASSWORD = "ldap@123";

    private static final String BASE_DN = "ou=花生日记,dc=ldapuser,dc=com";

    LdapService ldapService = new LdapService();

    @Before
    public void before() {
        // 初始化 LDAP 目录服务上下文
        LdapUtil.initDirContext(LDAP_ADDRESS, LDAP_BINDDN, LDAP_AUTH, LDAP_PASSWORD);
    }

    // 查询部门
    @Test
    public void searchDepartment() {
        String dn = "ou=技术中心1," + BASE_DN;
        System.out.println(ldapService.searchDepartment(dn));
    }

    // 查询部门
    @Test
    public void searchDepartmentList() {
        String dn = "ou=技术中心," + BASE_DN;
        System.out.println(ldapService.searchDepartmentList(dn, SearchControls.OBJECT_SCOPE));
        System.out.println(ldapService.searchDepartmentList(dn, SearchControls.ONELEVEL_SCOPE));
        System.out.println(ldapService.searchDepartmentList(dn, SearchControls.SUBTREE_SCOPE));
        System.out.println(ldapService.searchDepartmentList(dn, SearchControls.SUBTREE_SCOPE, "基础架构组"));
    }

    // 查询员工
    @Test
    public void searchEmployeeList() {
        String dn = "ou=技术中心," + BASE_DN;
        // 查询不同范围的员工信息列表
        // searchScope 搜索范围；0-仅搜索当前节点命名的对象；1-仅搜索当前节点的下一级命名对象；2-搜索当前节点为根的整个子树
        System.out.println(ldapService.searchEmployeeList(dn, SearchControls.OBJECT_SCOPE));
        System.out.println(ldapService.searchEmployeeList(dn, SearchControls.ONELEVEL_SCOPE));
        System.out.println(ldapService.searchEmployeeList(dn, SearchControls.SUBTREE_SCOPE));
    }

    // 创建部门
    @Test
    public void createOU() {
        String dn = "ou=大数据组,ou=技术中心," + BASE_DN;
        ldapService.createOU(dn, "大数据组");
    }

    // 删除用户 - 若ou下包含有用户，则不能删除
    @Test
    public void deleteOU() {
        String dn = "ou=你好组,ou=技术中心," + BASE_DN;
        ldapService.delete(dn);
    }

    // 创建用户
    @Test
    public void createUser() {
        String dn = "uid=10005,ou=大数据组,ou=技术中心," + BASE_DN;
        ldapService.createUser(dn, "10005", "10005", "张三", "18601790002", "zhangsan@163.com", "123456");
    }

    // 创建用户
    @Test
    public void createUser1() {
        String dn = "uid=1234,ou=大数据组,ou=技术中心," + BASE_DN;

        Map<String, Object> map = new HashMap<>();
        map.put("objectClass", Arrays.asList("top", "posixAccount", "inetOrgPerson"));
        map.put("uid", "1234");
        map.put("cn", "吴明");//必填项
        map.put("sn", "吴明");//必填项
        map.put("uidNumber", "123456");//必填项
        map.put("gidNumber", "0");//必填项
        map.put("homeDirectory", "/home/user/1234");//必填项
        map.put("mobile", "18601790001");
        map.put("mail", "wuming@163.com");
        map.put("userPassword", "123456");
        ldapService.createUser(dn, map);
    }

    // 删除用户
    @Test
    public void deleteUser() {
        String dn = "uid=10006,ou=你好组,ou=技术中心," + BASE_DN;
        ldapService.delete(dn);
    }

    // 修改
    @Test
    public void update() {
        String dn = "uid=1234,ou=大数据组,ou=技术中心," + BASE_DN;
        Map<String, Object> map = new HashMap<>();
        map.put("cn", "吴明11");//必填项
        map.put("sn", "吴明11");//必填项
        ldapService.update(dn, map);
    }

    // 修改
    @Test
    public void searchEmployee() {
        String dn = "ou=大数据组,ou=技术中心," + BASE_DN;
        UserDTO data = ldapService.searchEmployee(dn);
        System.out.println(data);
    }

}
