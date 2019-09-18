package com.coy.gupaoedu.study.jdk8;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2019/9/18 10:20
 */
public class StreamDemoTest {

    private List<Student> students = new ArrayList<>();

    @Before
    public void init() {
        Student s1 = new Student(1L, "肖战", 15, "浙江");
        Student s2 = new Student(2L, "王一博", 15, "湖北");
        Student s3 = new Student(3L, "杨紫", 17, "北京");
        Student s4 = new Student(4L, "李现", 17, "浙江");
        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
    }

    /**
     * 集合的过滤和筛选
     */
    @Test
    public void testFilter() {
        // filter 为中间过滤操作
        List<Student> list = students.stream().filter(student -> student.getAge() > 15).collect(Collectors.toList());
        // forEach 为终止方法操作
        list.forEach(System.out::println);
        System.out.println();

        // filter 为中间过滤操作
        list = students.stream().filter(student -> "浙江".equals(student.getAddress())).collect(Collectors.toList());
        // forEach 为终止方法操作
        list.forEach(a -> System.out.println(a));
    }


    /**
     * map就是将对应的元素按照给定的方法进行转换
     */
    @Test
    public void testMap() {
        // map 为中间操作
        List<String> list = students.stream().map(student -> "地址：" + student.getAddress()).collect(Collectors.toList());
        // forEach 为终止方法操作
        list.forEach(System.out::println);
        System.out.println();
    }

    /**
     * 集合去重（基本类型）
     */
    @Test
    public void testDistinct() {
        // 简单字符串的去重
        List<String> list = Arrays.asList("111", "222", "333", "111", "222");
        list.stream().distinct().forEach(System.out::println);
    }

    /**
     * 集合去重（引用对象）
     * 注意：实现hashCode和equal方法
     */
    @Test
    public void testDistinct2() {
        // 增加重复元素
        Student s1 = new Student(1L, "肖战", 15, "浙江");
        students.add(s1);

        // 引用对象的去重，引用对象要实现hashCode和equal方法，否则去重无效
        students.stream().distinct().forEach(System.out::println);
    }

    /**
     * 集合排序（默认排序）
     */
    @Test
    public void testSort() {
        List<String> list = Arrays.asList("333", "222", "111");
        list.stream().sorted().forEach(System.out::println);
    }

    /**
     * 集合排序（指定排序规则）
     */
    @Test
    public void testSort2() {
        // 先按照学生的id进行降序排序，再按照年龄进行降序排序
        students.stream()
                .sorted((stu1, stu2) -> Long.compare(stu2.getId(), stu1.getId()))
                .sorted((stu1, stu2) -> Integer.compare(stu2.getAge(), stu1.getAge()))
                .forEach(System.out::println);
    }

    /**
     * 集合limit，返回前几个元素
     */
    @Test
    public void testLimit() {
        List<String> list = Arrays.asList("333", "222", "111");
        list.stream().limit(2).forEach(System.out::println);
    }

    /**
     * 集合skip，删除前n个元素
     */
    @Test
    public void testSkip() {
        List<String> list = Arrays.asList("333", "222", "111");
        list.stream().skip(2).forEach(System.out::println);
    }

    /**
     * 集合reduce,将集合中每个元素聚合成一条数据
     */
    @Test
    public void testReduce() {
        List<String> list = Arrays.asList("欢", "迎", "你");
        String appendStr = list.stream().reduce("北京", (a, b) -> a + b);
        System.out.println(appendStr);
    }

    /**
     * 求集合中元素的最小值
     */
    @Test
    public void testMin() {
        Student minS = students.stream().min((stu1, stu2) -> Integer.compare(stu1.getAge(), stu2.getAge())).get();
        System.out.println(minS.toString());
    }

    /**
     * anyMatch/allMatch/noneMatch（匹配）
     * anyMatch：Stream 中任意一个元素符合传入的 predicate，返回 true
     * allMatch：Stream 中全部元素符合传入的 predicate，返回 true
     * noneMatch：Stream 中没有一个元素符合传入的 predicate，返回 true
     */
    @Test
    public void testMatch() {
        Boolean anyMatch = students.stream().anyMatch(s -> "湖北".equals(s.getAddress()));
        if (anyMatch) {
            System.out.println("有湖北人");
        }
        Boolean allMatch = students.stream().allMatch(s -> s.getAge() >= 15);
        if (allMatch) {
            System.out.println("所有学生都满15周岁");
        }
        Boolean noneMatch = students.stream().noneMatch(s -> "杨洋".equals(s.getName()));
        if (noneMatch) {
            System.out.println("没有叫杨洋的同学");
        }
    }
}