package com.coy.gupaoedu.study.mybatis.generator;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 自定义注释生成
 *
 * @Author chenck
 * @Date 2019/1/16 20:32
 */
public class MyCommentGenerator implements CommentGenerator {

    private Properties properties = new Properties();
    private boolean suppressDate = false;
    private boolean suppressAllComments = false;
    private boolean addRemarkComments = false;
    private SimpleDateFormat dateFormat;

    public MyCommentGenerator() {

    }

    /**
     * Java文件加注释
     */
    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        if (this.suppressAllComments) {
            return;
        }
        compilationUnit.addFileCommentLine("/*");
        compilationUnit.addFileCommentLine(" * This file is automatically generated by MyBatis Generator");
        compilationUnit.addFileCommentLine(" */");
    }

    /**
     * Mybatis的Mapper.xml文件里面的注释
     */
    public void addComment(XmlElement xmlElement) {

    }

    /**
     * 调用此方法为根元素的第一个子节点添加注释。 此方法可用于添加
     * 一般文件注释（如版权声明）。 但是，请注意，XML文件合并功能不会处理
     * 这个注释。 如果反复运行生成器，则只保留初始运行的注释。
     */
    public void addRootComment(XmlElement rootElement) {

    }

    /**
     * 从properties配置文件中添加此实例的属性
     */
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
        this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
        String dateFormatString = properties.getProperty("dateFormat");
        if (StringUtility.stringHasValue(dateFormatString)) {
            this.dateFormat = new SimpleDateFormat(dateFormatString);
        }

    }

    /**
     * 此方法用于添加自定义javadoc标签。
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append("@mbg.generated");

        if (markAsDoNotDelete) {
            // sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
        }

        String s = this.getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }

        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * 获取时间
     */
    protected String getDateString() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
    }

    /**
     * 类注释
     */
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        if (this.suppressAllComments) {
            return;
        }
        innerClass.addJavaDocLine("/**");
        innerClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + " " + introspectedTable.getFullyQualifiedTable().toString());
        innerClass.addJavaDocLine(" * @mbg.generated " + getDateString());
        innerClass.addJavaDocLine(" */");
    }

    /**
     * 实体类注释
     */
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (this.suppressAllComments) {
            return;
        }
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + " " + introspectedTable.getFullyQualifiedTable().toString());
        this.addJavadocTag(topLevelClass, true);
        topLevelClass.addJavaDocLine(" */");
    }

    /**
     * 枚举注释
     */
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        innerEnum.addJavaDocLine("/**");
        innerEnum.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable().toString());
        innerEnum.addJavaDocLine(" */");
    }

    /**
     * 字段注释
     */
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
        field.addJavaDocLine(" */");
    }

    /**
     * 字段注释
     */
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        //field.addJavaDocLine("/**");
        //field.addJavaDocLine(" * " + field.getName() + " " + field.getType());
        //field.addJavaDocLine(" */");
    }

    /**
     * 普通方法注释,mapper接口中方法
     */
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        method.addJavaDocLine("/**");
        addJavadocTag(method, false);
        method.addJavaDocLine(" */");
    }

    /**
     * getter方法注释
     */
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    /**
     * setter方法注释
     */
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    /**
     * 类注释
     */
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }
        innerClass.addJavaDocLine("/**");
        innerClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + " " + introspectedTable.getFullyQualifiedTable().toString());
        innerClass.addJavaDocLine(" * @Author mbg.generated");
        innerClass.addJavaDocLine(" * @Date" + getDateString());
        innerClass.addJavaDocLine(" */");
    }
}
