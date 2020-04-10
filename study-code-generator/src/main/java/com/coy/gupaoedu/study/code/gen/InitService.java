package com.coy.gupaoedu.study.code.gen;

import com.coy.gupaoedu.study.code.gen.config.GenConfig;
import com.coy.gupaoedu.study.code.gen.service.GenTableService;
import com.coy.gupaoedu.study.code.gen.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author chenck
 */
//@Component
@Slf4j
public class InitService implements InitializingBean {

    @Autowired
    GenTableService genTableService;

    @Autowired
    GenConfig genConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("生成代码start...");

        String tableNames = genConfig.getTableNames();
        if (StringUtils.isBlank(tableNames)) {
            log.info("未配置指定的表名");
            return;
        }

        String[] tableNameArr = tableNames.split(",");
        for (String tableName : tableNameArr) {
            Map<String, String> dataMap = genTableService.previewCode(tableName);
        }
        log.info("生成代码end...");
    }


}
