package com.coy.gupaoedu.study.mebatis.build;

import com.coy.gupaoedu.study.mebatis.ClassUtil;
import com.coy.gupaoedu.study.mebatis.GPMappedStatement;
import com.coy.gupaoedu.study.mebatis.GPSqlCommandType;
import com.coy.gupaoedu.study.mebatis.exception.MebatisException;
import com.coy.gupaoedu.study.mebatis.session.GPConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chenck
 * @date 2019/5/8 14:09
 */
public class XMLMapperBuilder {

    private GPConfiguration configuration;

    /**
     *
     */
    private String mapperXmlResource;

    public XMLMapperBuilder(GPConfiguration configuration, String mapperXmlResource) {
        this.configuration = configuration;
        this.mapperXmlResource = mapperXmlResource;
    }

    /**
     * 查找合格的Mapper映射器文件
     */
    public List<File> findEligibleMapperXmlFile() {
        try {
            int index = mapperXmlResource.lastIndexOf("/");
            String xmlPath = "";
            String xmlName = mapperXmlResource;
            if (index != -1) {
                xmlPath = mapperXmlResource.substring(0, index);
                xmlName = mapperXmlResource.substring(index + 1);
            }
            // 转换为正则表达式
            xmlName = xmlName.replaceAll("\\*", "[\\\\w\\\\s]*");
            Pattern xmlNamePattern = Pattern.compile(xmlName);

            // 获取所有的资源文件
            Enumeration<URL> resourceUrls = ClassUtil.getResources(xmlPath);
            List<File> eligibleMapperList = new ArrayList<>();
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                File classPath = new File(url.getFile());
                for (File file : classPath.listFiles()) {
                    if (file.isDirectory()) {
                        // 暂不支持下级目录中的Mapper扫描
                        continue;
                    }
                    if (xmlNamePattern.matcher(file.getName()).matches()) {
                        System.out.println(file.getName() + " 匹配成功");
                        eligibleMapperList.add(file);
                        continue;
                    }
                }
            }
            return eligibleMapperList;
        } catch (Exception e) {
            throw new MebatisException("查找合格的Mapper映射器文件失败", e);
        }
    }

    /**
     * 解析Mapper映射器
     */
    public void parse() {
        // 查找合格的Mapper映射器文件
        List<File> eligibleMapperList = findEligibleMapperXmlFile();
        if (null == eligibleMapperList || eligibleMapperList.size() == 0) {
            System.out.println("未找到合格的Mapper映射器文件");
            return;
        }

        try {
            //
            for (File file : eligibleMapperList) {
                parseXml(file);
            }
        } catch (Exception e) {
            throw new MebatisException("解析Mapper映射器文件失败", e);
        }
    }

    /**
     * 解析Mapper XML文件
     */
    public void parseXml(File xmlFile) throws Exception {
        // 1、获取工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // 2、获取dom解析器
        DocumentBuilder db = dbf.newDocumentBuilder();
        // 3、解析xml文档，获取document对象(根节点)
        Document document = db.parse(xmlFile);

        // 获取根元素
        Element root = document.getDocumentElement();
        parseMapperElement(root);
    }

    /**
     *
     */
    public void parseMapperElement(Element root) throws ClassNotFoundException {
        if (!"mapper".equals(root.getTagName())) {
            System.out.println("根元素名称必须为mapper");
            return;
        }

        String namespace = root.getAttribute("namespace");

        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            // 只处理认识的元素标签
            if (!MapperChildElementEnum.exist(node.getNodeName())) {
                // throw new MebatisException(node.getNodeName() + " element is not support");
                continue;
            }
            // System.out.println("元素名：" + node.getNodeName());
            parseSelect((Element) node, namespace);
        }
    }

    /**
     * select 元素的解析
     */
    public void parseSelect(Element select, String namespace) throws ClassNotFoundException {

        String id = select.getAttribute("id");
        String parameterType = select.getAttribute("parameterType");
        String resultType = select.getAttribute("resultType");
        String sql = select.getTextContent();

        // System.out.println("id=" + id);
        // System.out.println("parameterType=" + parameterType);
        // System.out.println("resultType=" + resultType);
        // System.out.println("sql=" + sql);

        String statementId = namespace + "." + id;
        GPMappedStatement.Builder builder = new GPMappedStatement.Builder(statementId, GPSqlCommandType.SELECT);
        if (null != parameterType && parameterType.trim().length() > 0) {
            builder.parameterType(Class.forName(parameterType));
        }
        if (null != resultType && resultType.trim().length() > 0) {
            builder.resultType(Class.forName(resultType));
        }
        if (null != sql && sql.trim().length() > 0) {
            sql = sql.replaceAll("\n", " ").trim();
            builder.sql(sql);
        }
        configuration.addMappedStatement(builder.build());

        // select 下的子元素后续再扩展

    }
}
