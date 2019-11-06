package com.coy.gupaoedu.study.serialization.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author chenck
 * @date 2019/10/30 18:56
 */
public class ReadJsonTest {

    public static void main(String[] args) throws IOException {
        String jsonFile = "E:\\项目\\01 打通钉钉组织架构\\生产LDAP用户数据备份20191031-01";

        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(jsonFile)));
        String line;
        StringBuilder json = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            //System.out.print(line + "\n");
            json.append(line);
        }
        bufferedReader.close();
        JSONObject jsonObject = JSON.parseObject(json.toString());

        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONArray rows = (JSONArray) data.get("rows");
        System.out.println();

        StringBuilder sql = new StringBuilder("INSERT INTO `test`.`hs_dingtalk_user_info_copy1` ");
        sql.append("(");
        sql.append("`user_id`, `login_name`, `user_name`, `password`, `job_number`, `position`, `mobile`, `email`");
        sql.append(") VALUES");
        System.out.println(sql.toString());
        for (Object row : rows) {
            //INSERT INTO `test`.`hs_dingtalk_user_info`(`id`, `user_id`, `login_name`, `user_name`, `password`, `job_number`, `position`,
            // `mobile`, `email`, `status`, `is_leader`, `is_delete`, `create_time`, `update_time`) VALUES (27, '124723086935152773', 'xujianwen',
            // '许建文', '{MD5}UYpdO/EuUebH54Sv3aEyBQ==', '027', '运维工程师', '18001293092', 'jianwen.xu@drgou.com', 0, '0', '0', '2019-10-31 10:27:29',
            // NULL);
            //System.out.println(row);

            String sqlStr = buildInsertSQL(sql, (JSONObject) row);
            //System.out.println(sqlStr);
        }

    }

    public static String buildInsertSQL(StringBuilder sql, JSONObject row) {
        StringBuilder field = new StringBuilder();
        field.append(" (");
        buildField(field, row, "user_id", "", ",");
        buildField(field, row, "username", "", ",");
        buildField(field, row, "realname", "", ",");
        buildField(field, row, "password", "", ",");
        buildField(field, row, "userId", "", ",");
        buildField(field, row, "position", "", ",");
        buildField(field, row, "mobile", "", ",");
        buildField(field, row, "email", "", "");
        field.append(" ),");
        System.out.println(field.toString());
        sql.append(field);
        return sql.toString();
    }

    public static void buildField(StringBuilder sql, JSONObject row, String key, String defaultValue, String split) {
        Object value = row.get(key);
        if (null == value) {
            sql.append("'").append(defaultValue).append("'");
        } else {
            sql.append("'").append(value).append("'");
        }
        sql.append(split);
    }

}
