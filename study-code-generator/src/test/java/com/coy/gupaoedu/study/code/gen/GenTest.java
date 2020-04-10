package com.coy.gupaoedu.study.code.gen;

import com.coy.gupaoedu.study.code.gen.service.GenTableService;
import com.coy.gupaoedu.study.code.gen.util.StringUtils;
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
            "t_batch_refund_apply",
            ""
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
        genTableService.generatorCode(tableNames);
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


}
