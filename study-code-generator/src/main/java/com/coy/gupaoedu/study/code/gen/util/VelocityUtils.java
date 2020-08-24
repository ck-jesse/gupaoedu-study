package com.coy.gupaoedu.study.code.gen.util;

import com.alibaba.fastjson.JSONObject;
import com.coy.gupaoedu.study.code.gen.config.GenConfig;
import com.coy.gupaoedu.study.code.gen.consts.GenConstants;
import com.coy.gupaoedu.study.code.gen.domain.GenTable;
import com.coy.gupaoedu.study.code.gen.domain.GenTableColumn;
import org.apache.velocity.VelocityContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class VelocityUtils {

    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = "main/java";
    private static final String PROJECT_TEST_PATH = "test/java";

    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "main/resources/mapper";
    /**
     * proto文件路径
     */
    private static final String PROTO_PATH = "main/resources/proto";

    /**
     * 模板列表
     */
    private static List<String> templates = new ArrayList<String>();

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTable genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("serviceName", GenConfig.getServiceName());
        velocityContext.put("servicePort", GenConfig.getServicePort());
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("importList", getImportList(genTable.getColumns()));
        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", genTable);
        if (GenConstants.TPL_TREE.equals(tplCategory)) {
            setTreeVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }

    public static void setTreeVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String treeCode = getTreecode(paramsObj);
        String treeParentCode = getTreeParentCode(paramsObj);
        String treeName = getTreeName(paramsObj);

        context.put("treeCode", treeCode);
        context.put("treeParentCode", treeParentCode);
        context.put("treeName", treeName);
        context.put("expandColumn", getExpandColumn(genTable));
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE)) {
            context.put("tree_parent_code", paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
            context.put("tree_name", paramsObj.getString(GenConstants.TREE_NAME));
        }
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tplCategory) {
        if(templates.size() > 0){
            return templates;
        }
        List<String> templateList = new ArrayList<String>();
        templateList.add("vm/hsrj/proto.proto.vm");
        templateList.add("vm/hsrj/entity.java.vm");
        templateList.add("vm/hsrj/mapper.java.vm");
        templateList.add("vm/hsrj/mapper.xml.vm");
        templateList.add("vm/hsrj/service.java.vm");
        templateList.add("vm/hsrj/serviceImpl.java.vm");
        templateList.add("vm/hsrj/controller.java.vm");
        templateList.add("vm/hsrj/controllerTest.java.vm");
        return templateList;
    }

    /**
     * 添加模板
     */
    public static void addTemplate(String template) {
        templates.add(template);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTable genTable) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String packagePath = StringUtils.replace(packageName, ".", "/");
        String javaPath = PROJECT_PATH + "/" + packagePath;
        String javaTestPath = PROJECT_TEST_PATH + "/" + packagePath;
        //String mybatisPath = MYBATIS_PATH + "/" + moduleName;
        String mybatisPath = MYBATIS_PATH;
        String vuePath = "vue";

        if (template.contains("entity.java.vm")) {
            fileName = String.format("%s/entity/%s.java", javaPath, className);
        } else if (template.contains("entityDTO.java.vm")) {
            fileName = String.format("%s/feign/dto/%sDTO.java", javaPath, className);
        } else if (template.contains("entityQueryDTO.java.vm")) {
            fileName = String.format("%s/feign/dto/%sQueryDTO.java", javaPath, className);
        } else if (template.contains("mapper.java.vm")) {
            fileName = String.format("%s/dao/%sMapper.java", javaPath, className);
        } else if (template.contains("service.java.vm")) {
            fileName = String.format("%s/service/I%sService.java", javaPath, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = String.format("%s/service/impl/%sServiceImpl.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = String.format("%s/controller/%sController.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = String.format("%s/%sMapper.xml", mybatisPath, className);
        } else if (template.contains("sql.vm")) {
            fileName = businessName + "Menu.sql";
        } else if (template.contains("api.js.vm")) {
            fileName = String.format("%s/api/%s/%s.js", vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = String.format("%s/views/%s/%s/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = String.format("%s/views/%s/%s/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("proto.proto.vm")) {
            fileName = String.format("%s/%sProto.proto", PROTO_PATH, className);
        } else if (template.contains("controllerTest.java.vm")) {
            fileName = String.format("%s/%sControllerTest.java", javaTestPath, className);
        } else if (template.contains("feignClient.java.vm")) {
            fileName = String.format("%s/feign/client/%sFeignClient.java", javaPath, className);
        } else if (template.contains("feignClientHystrix.java.vm")) {
            fileName = String.format("%s/feign/hystrix/%sFeignClientHystrix.java", javaPath, className);
        }
        return fileName;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        String basePackage = StringUtils.substring(packageName, 0, lastIndex);
        return basePackage;
    }

    /**
     * 根据列类型获取导入包
     *
     * @param columns 列集合
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getImportList(List<GenTableColumn> columns) {
        HashSet<String> importList = new HashSet<String>();
        for (GenTableColumn column : columns) {
//            if (!column.isSuperColumn() && GenConstants.TYPE_DATE.equals(column.getJavaType())) {
            if (GenConstants.TYPE_DATE.equals(column.getJavaType())) {
                importList.add("java.util.Date");
            } else if (!column.isSuperColumn() && GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
                importList.add("java.math.BigDecimal");
            }
        }
        return importList;
    }

    /**
     * 获取权限前缀
     *
     * @param moduleName   模块名称
     * @param businessName 业务名称
     * @return 返回权限前缀
     */
    public static String getPermissionPrefix(String moduleName, String businessName) {
        return String.format("%s:%s", moduleName, businessName);
    }

    /**
     * 获取树编码
     *
     * @return 树编码
     */
    public static String getTreecode(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_CODE)) {
            // 下划线 转 驼峰(首字母小写)
            return StringUtils.lowerCamelCase(paramsObj.getString(GenConstants.TREE_CODE));
        }
        return "";
    }

    /**
     * 获取树父编码
     *
     * @return 树父编码
     */
    public static String getTreeParentCode(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE)) {
            // 下划线 转 驼峰(首字母小写)
            return StringUtils.lowerCamelCase(paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        return "";
    }

    /**
     * 获取树名称
     *
     * @return 树名称
     */
    public static String getTreeName(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
            // 下划线 转 驼峰(首字母小写)
            return StringUtils.lowerCamelCase(paramsObj.getString(GenConstants.TREE_NAME));
        }
        return "";
    }

    /**
     * 获取需要在哪一列上面显示展开按钮
     *
     * @param genTable 业务表对象
     * @return 展开按钮列序号
     */
    public static int getExpandColumn(GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String treeName = paramsObj.getString(GenConstants.TREE_NAME);
        int num = 0;
        for (GenTableColumn column : genTable.getColumns()) {
            if (column.isList()) {
                num++;
                String columnName = column.getColumnName();
                if (columnName.equals(treeName)) {
                    break;
                }
            }
        }
        return num;
    }
}