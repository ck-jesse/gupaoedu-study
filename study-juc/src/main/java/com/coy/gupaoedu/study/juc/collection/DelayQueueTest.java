package com.coy.gupaoedu.study.juc.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DelayQueue 延时队列
 * 可以使放入队列的元素在指定的延时后才被消费者取出，元素需要实现Delayed接口。
 *
 * @author chenck
 * @date 2019/8/26 12:53
 */
public class DelayQueueTest implements Serializable {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        // 快捷键 list.for => for (String s : list) {}
        for (String s12 : list) {

        }

        String json = "{\n" +
                "  \"name\":\"zhangsanfeng\",\n" +
                "  \"age\": 182345667,\n" +
                "  \"desc\": \"保持年轻\"\n" +
                "}";
        String java = "public void test(){\n" +
                "    sytstem.out.println(\"test\");\n" +
                "}";
    }
}
