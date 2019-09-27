package com.coy.gupaoedu.study.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DealRedisCacheDataService implements InitializingBean {

    // inject the actual template
    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    public void afterPropertiesSet() throws Exception {

        Integer month = 201908;
        for (String cardCode : data) {
            getValue(cardCode, month);
        }
    }

    private Object getValue(String cardCode,Integer month){
        String key = ProgressCacheUtils.getMallPvCacheHashKey(cardCode);
        String field = ProgressCacheUtils.getMallPvCacheFieldKey(cardCode, month);
        // dtyunxi-perfect-prod:perfect-center-progress:integral:0
        key = "dtyunxi-perfect-prod:" + key;

        Object value = template.opsForHash().get(key, field);
        System.out.println("=> key="+key+", field="+field+",value="+value);
        return value;
    }

    public static final List<String> data = new ArrayList<String>();
    static{
        data.add("1000240074");
        data.add("1000357291");
        data.add("1000535011");
        data.add("1000123697");
        data.add("1000127510");
        data.add("1000246772");
        data.add("1000250898");
        data.add("1000746688");
        data.add("1000004880");
        data.add("1000012978");
        data.add("1000019705");
        data.add("1000283038");
        data.add("1000653419");
        data.add("1000158047");
        data.add("1000391740");
        data.add("1000599006");
        data.add("1000602899");
        data.add("1000609106");
        data.add("1000613230");
        data.add("1000615559");
        data.add("1000831549");
        data.add("1000072857");
        data.add("1000569212");
        data.add("1000576905");
        data.add("1000815791");
        data.add("1000817450");
        data.add("1000937673");
        data.add("1000028834");
        data.add("1000224814");
        data.add("1000510710");
        data.add("1000516958");
        data.add("1000710435");
        data.add("1000724989");
        data.add("1001021482");
        data.add("1001024490");
        data.add("1001048366");
        data.add("1001050079");
        data.add("1000580278");
        data.add("1000765983");
        data.add("1000766724");
        data.add("1000776178");
        data.add("1000968478");
        data.add("1000262191");
        data.add("1000628787");
        data.add("1000630111");
        data.add("1000842470");
        data.add("1000849370");
        data.add("1000855521");
        data.add("1000475322");
        data.add("1000478471");
        data.add("1000700028");

    }

}
