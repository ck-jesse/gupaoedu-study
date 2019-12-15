package com.coy.gupaoedu.study.ldap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * LDAP 工具类
 * <p>
 * 条目：cn用户；ou-组；dc-域
 * <p>
 * dn : 识别名，每一个条目都有自己的dn
 *
 * @author chenck
 * @date 2019/12/10 10:32
 */
public class LdapUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapUtil.class);

    /**
     * 目录服务上下文
     */
    public static DirContext context;

    /**
     * 获取目录服务上下文
     */
    public static DirContext getDirContext() {
        if (null != context) {
            return context;
        }
        // 初始化默认配置的DirContext
        return initDirContext(SettingsUtil.LDAP_ADDRESS, SettingsUtil.LDAP_BINDDN, SettingsUtil.LDAP_AUTH, SettingsUtil.LDAP_PASSWORD);
    }

    /**
     * LDAP 初始化 目录服务上下文
     * <p>
     * 注：该方法方便测试时，设置配置
     *
     * @param url      服务端地址
     * @param dnName   DN-识别名（等同于目录名），如 cn=Manager,dc=ldapuser,dc=com
     * @param auth     授权
     * @param password 密码
     */
    public static synchronized DirContext initDirContext(String url, String dnName, String auth, String password) {
        if (null != context) {
            return context;
        }
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, dnName);
        env.put(Context.SECURITY_AUTHENTICATION, auth);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try {
            context = new InitialDirContext(env);
            return context;
        } catch (NamingException e) {
            throw new RuntimeException("LDAP-初始化DirContext失败", e);
        }
    }

    /**
     * 绑定/创建LDAP条目
     * <p>
     * 注：条目可以 cn-用户，ou-组等
     *
     * @param dnName  DN-识别名（等同于目录名）
     * @param attrMap 条目属性集合
     */
    public static void create(String dnName, Map<String, Object> attrMap) {
        if (StringUtils.isEmpty(dnName)) {
            throw new IllegalArgumentException("DN-识别名不能为空");
        }
        if (attrMap == null || attrMap.isEmpty()) {
            throw new IllegalArgumentException("属性不能为空");
        }

        // LDAP实体的属性集
        BasicAttributes attributes = new BasicAttributes(true);
        for (Map.Entry<String, Object> entry : attrMap.entrySet()) {
            if (entry.getValue() instanceof List) {
                // LDAP基本属性对象，如 objectClass
                Attribute attribute = new BasicAttribute(entry.getKey());
                List<String> valueList = (List<String>) entry.getValue();
                for (String value : valueList) {
                    attribute.add(value);
                }
                attributes.put(attribute);
            } else if (entry.getValue() instanceof Map) {
                continue;
            } else {
                attributes.put(entry.getKey(), entry.getValue());
            }
        }
        try {
            // 绑定/创建LDAP条目对象
            getDirContext().createSubcontext(dnName, attributes);
        } catch (NamingException e) {
            throw new RuntimeException("LDAP-创建条目异常", e);
        }
    }

    /**
     * 修改条目属性
     *
     * @param dnName  DN-识别名（等同于目录名）
     * @param attrMap 条目属性集合
     */
    public static void update(String dnName, Map<String, Object> attrMap) {
        if (StringUtils.isEmpty(dnName)) {
            throw new IllegalArgumentException("DN-识别名不能为空");
        }
        if (attrMap == null || attrMap.isEmpty()) {
            throw new IllegalArgumentException("属性不能为空");
        }
        int i = 0;
        ModificationItem[] modificationItems = new ModificationItem[attrMap.size()];
        for (Map.Entry<String, Object> entry : attrMap.entrySet()) {
            modificationItems[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(entry.getKey(),
                    entry.getValue()));
            i++;
        }
        try {
            getDirContext().modifyAttributes(dnName, modificationItems);
        } catch (NameNotFoundException e) {
            LOGGER.warn("修改条目：DN不存在，dn={}", dnName);
            return;
        } catch (NamingException e) {
            throw new RuntimeException("LDAP-修改条目属性异常", e);
        }
    }

    /**
     * 删除条目
     *
     * @param dnName DN-识别名（等同于目录名）
     */
    public static void delete(String dnName) {
        if (StringUtils.isEmpty(dnName)) {
            throw new IllegalArgumentException("DN-识别名不能为空");
        }
        try {
            getDirContext().destroySubcontext(dnName);
        } catch (NameNotFoundException e) {
            LOGGER.warn("删除条目：DN不存在，dn={}", dnName);
            return;
        } catch (NamingException e) {
            throw new RuntimeException("LDAP-删除条目异常", e);
        }
    }

    /**
     * 查询条目
     *
     * @param dnName DN-识别名（等同于目录名）
     */
    public static Map<String, Object> searchEntry(String dnName) {
        return searchEntry(dnName, null);
    }

    /**
     * 查询条目
     *
     * @param dnName  DN-识别名（等同于目录名）
     * @param attrIds 查询的属性字段
     */
    public static Map<String, Object> searchEntry(String dnName, String[] attrIds) {
        if (StringUtils.isEmpty(dnName)) {
            throw new IllegalArgumentException("DN-识别名不能为空");
        }
        try {
            Attributes attributes = null;
            if (null == attrIds || attrIds.length == 0) {
                attributes = getDirContext().getAttributes(dnName, attrIds);
            } else {
                attributes = getDirContext().getAttributes(dnName);
            }
            if (null == attributes) {
                return null;
            }
            Map<String, Object> data = new HashMap<>();
            NamingEnumeration<String> nEnum = attributes.getIDs();
            for (; nEnum.hasMore(); ) {
                String attrID = nEnum.next();
                Attribute attr = attributes.get(attrID);
                if (attr.size() > 1) {
                    List<Object> valueList = new ArrayList<>();
                    NamingEnumeration<Object> valueEnum = (NamingEnumeration<Object>) attr.getAll();
                    for (; valueEnum.hasMore(); ) {
                        valueList.add(valueEnum.next());
                    }
                } else {
                    data.put(attrID, attr.get());
                }
            }
            return data;
        } catch (NameNotFoundException e) {
            LOGGER.warn("查询条目：DN不存在，dn={}", dnName);
            return null;
        } catch (NamingException e) {
            throw new RuntimeException("LDAP-查询条目异常", e);
        }
    }

    /**
     * 查询条目列表
     *
     * <p>
     * filter 语法：
     * |运算符   |符号|说明|
     * |等于    | =  |返回所含属性值与指定的值完全匹配的条目。例如：cn=Bob Johnson
     * |子字符串| =str*str  |返回所含属性中包含指定子字符串的条目。* 表示0个或多个字符。例如：cn=Bob* ; cn=*Johnson ;  cn=*John* ;
     * |存在   | =*  |返回所含所指定属性的一个或多个值条目。例如：cn=* ; telephonenumber=* ;
     * |AND    | &  |所有指定的过滤器必须都为true时，该语句为true。例如：(&(filter)(filter)(filter))
     * |OR     | |  |至少指定的一个过滤器为true时，该语句为true。例如：(|(filter)(filter)(filter))
     * |NOT    | !  |指定的语句必须为false，该语句为true。例如：(!(filter))
     * </p>
     *
     * @param dnName        DN-识别名（等同于目录名）
     * @param filter        数据过滤条件
     * @param searchScope   搜索范围；0-仅搜索当前节点命名的对象；1-仅搜索当前节点的下一级命名对象；2-搜索当前节点为根的整个子树
     * @param returnedAttrs 指定结果所包括的属性集
     */
    public static List<Map<String, Object>> searchEntryList(String dnName, String filter, int searchScope, String[] returnedAttrs) {
        if (StringUtils.isEmpty(dnName)) {
            throw new IllegalArgumentException("DN-识别名不能为空");
        }
        if (StringUtils.isEmpty(dnName) || null == returnedAttrs || 0 == returnedAttrs.length) {
            throw new IllegalArgumentException("DN-识别名不能为空");
        }

        // LDAP目录服务搜索控制对象
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(searchScope);
        searchCtls.setReturningAttributes(returnedAttrs);
        try {
            NamingEnumeration<SearchResult> searchResult = getDirContext().search(dnName, filter, searchCtls);
            List<Map<String, Object>> list = new ArrayList<>();
            while (searchResult.hasMoreElements()) {
                SearchResult sr = searchResult.next();
                Map<String, Object> map = new HashMap<>();
                Attributes attrs = sr.getAttributes();
                for (NamingEnumeration ne = attrs.getAll(); ne.hasMore(); ) {
                    Attribute attr = (Attribute) ne.next();// 得到下一个属性
                    map.put(attr.getID(), attr.get());
                }
                list.add(map);
            }
            return list;
        } catch (NameNotFoundException e) {
            LOGGER.warn("查询条目列表：DN不存在，dn={}", dnName);
            return null;
        } catch (NamingException e) {
            throw new RuntimeException("LDAP-查询条目列表异常", e);
        }
    }

    /**
     * 查询条目列表
     * 注：搜索当前节点为根的整个子树
     */
    public static List<Map<String, Object>> searchEntryList(String dnName, String filter, String[] returnedAttrs) {
        return searchEntryList(dnName, filter, SearchControls.SUBTREE_SCOPE, returnedAttrs);
    }

}