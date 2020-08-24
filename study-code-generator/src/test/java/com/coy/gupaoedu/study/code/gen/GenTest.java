package com.coy.gupaoedu.study.code.gen;

import com.coy.gupaoedu.study.code.gen.service.GenTableService;
import com.coy.gupaoedu.study.code.gen.util.StringUtils;
import com.coy.gupaoedu.study.code.gen.util.VelocityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author chenck
 * @date 2020/4/9 20:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GenTest {

    @Autowired
    GenTableService genTableService;

    String[] tableNames = {
//            "hx_banner_config",
//            "hx_card_catalog",
//            "hx_card_equity",
//            "hx_card_supplier",
//            "t_operate_log",
//            "t_sync_order_err_log",
//            "t_sync_order_detail",
//            "t_sync_order_delivery_address",
//            "t_sync_order_all_commission_detail",
//            "t_sync_order",
//            "after_sale_apply_main_record",
//            "t_biz_auth",
            "goods",
            "stock_change_flow",
    };

    /**
     * 第一步：导入表结构（将表信息和列信息插入DB）
     */
    @Test
    public void importTableSave() {
        genTableService.importTableSave(tableNames);
    }

    /**
     * 第二步：生成代码
     */
    @Test
    public void generatorCode() {

        VelocityUtils.addTemplate("vm/weeget/controller.java.vm");
        VelocityUtils.addTemplate("vm/weeget/controllerTest.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entity.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entityDTO.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entityQueryDTO.java.vm");
        VelocityUtils.addTemplate("vm/weeget/mapper.java.vm");
        VelocityUtils.addTemplate("vm/weeget/mapper.xml.vm");
        VelocityUtils.addTemplate("vm/weeget/service.java.vm");
        VelocityUtils.addTemplate("vm/weeget/serviceImpl.java.vm");
        VelocityUtils.addTemplate("vm/weeget/feignClient.java.vm");
        VelocityUtils.addTemplate("vm/weeget/feignClientHystrix.java.vm");

        String zipName = "code.zip";
        String outZipPath = GenTest.class.getResource("/").getPath() + zipName;
        System.out.println("代码生成路径： " + outZipPath);
        genTableService.generatorCode(tableNames, outZipPath);
    }

    /**
     * 预览
     */
    @Test
    public void previewCode() {
        for (String tableName : tableNames) {
            if (StringUtils.isBlank(tableName)) {
                continue;
            }
            Map<String, String> dataMap = genTableService.previewCode(tableName);
        }
    }

    public static void main(String[] args) {
        System.out.println("get" + StringUtils.upperCamelCase("id"));
    }

}
