package com.coy.gupaoedu.study.jdk8.stream;

import com.coy.gupaoedu.study.jdk8.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stream作为java8的新特性，基于lambda表达式，是对集合对象功能的增强，
 * 它专注于对集合对象进行各种高效、便利的聚合操作或者大批量的数据操作，提高了编程效率和代码可读性。
 * <p>
 * Stream的原理：
 * 将要处理的元素看做一种流，流在管道中传输，并且可以在管道的节点上处理，包括 过滤筛选、去重、排序、聚合等。
 * 元素流在管道中经过中间操作的处理，最后由最终操作得到前面处理的结果。
 * <p>
 * Stream 不是集合元素，它不是数据结构并不保存数据，它是有关算法和计算的，它更像一个高级版本的 Iterator。
 * Stream 就如同一个迭代器（Iterator），单向，不可往复，数据只能遍历一次，遍历过一次后即用尽了，就好比流水从面前流过，一去不复返。
 * 而和迭代器又不同的是，Stream 可以并行化操作，迭代器只能命令式地、串行化操作。
 * Stream 的另外一大特点是，数据源本身可以是无限的。
 *
 * @author chenck
 * @date 2019/9/18 10:20
 */
public class StreamDemoTest {

    private List<Student> students = new ArrayList<>();

    @Before
    public void init() {
        Student s1 = new Student(1L, "肖战", 15, "浙江");
        Student s11 = new Student(1L, "肖战2", 15, "浙江");
        Student s2 = new Student(2L, "王一博", 15, "湖北");
        Student s22 = new Student(2L, "王一博2", 15, "湖北");
        Student s3 = new Student(3L, "杨紫", 17, "北京");
        Student s4 = new Student(4L, "李现", 17, "浙江");
        students.add(s1);
        students.add(s11);
        students.add(s2);
        students.add(s22);
        students.add(s3);
        students.add(s4);
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
        System.out.println();

        list = students.stream().filter(student -> student.getId() == 4).collect(Collectors.toList());
        list.forEach(a -> System.out.println(a));
        System.out.println();

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

        // 分组(含去重)(list -> map)
        Map<Long, Student> map = students.stream().collect(Collectors.toMap(Student::getId, Function.identity(), (student, student2) -> student2));
        System.out.println(map);

        // (map -> list)
        List<Student> listTemp = map.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
        System.out.println(listTemp);

        // 分组
        Map<Long, List<Student>> map1 = students.stream().collect(Collectors.groupingBy(Student::getId));
        System.out.println(map1);
    }

    @Test
    public void testToMap() {
        Map<String, String> l1KeyMap = new HashMap<>();
        l1KeyMap.put("key1", "key1");
        l1KeyMap.put("key2", "key2");
        l1KeyMap.put("key3", "key3");
        l1KeyMap.put("key4", "key4");

        Map<String, Object> l2HitMap = new HashMap<>();
        l2HitMap.put("key1", 1);
        l2HitMap.put("key2", 2);
        l2HitMap.put("key3", null);
        l2HitMap.put("key4", null);// 模拟value为null

        // 使用collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) 来构建，此时可允许null值的出现
        Map<String, Object> l2HitMapTemp1 = l2HitMap.entrySet().stream()
                .filter(entry -> l1KeyMap.containsKey(entry.getKey()))
                .collect(HashMap::new, (map, entry) -> map.put(l1KeyMap.get(entry.getKey()), entry.getValue()), HashMap::putAll);
        System.out.println(l2HitMapTemp1);

        // 由于 Collectors.toMap(key,value)中的value为null时，会报 java.util.HashMap.merge NullPointerException，所以建议使用上面的方式。
        Map<String, Object> l2HitMapTemp2 = l2HitMap.entrySet().stream()
                .filter(entry -> l1KeyMap.containsKey(entry.getKey()))
                .collect(Collectors.toMap(entry -> l1KeyMap.get(entry.getKey()), entry -> entry.getValue()));
        System.out.println(l2HitMapTemp1);
    }

    @Test
    public void testMap1() {
        //将cost增加了0,05倍的大小然后输出
        List<Double> cost = Arrays.asList(10.0, 20.0, 30.0);
        cost.stream().map(x -> x + x * 0.05).forEach(x -> System.out.println(x));
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

    /**
     *
     */
    @Test
    public void testGroup() {
        // 分组
        Map<Long, List<Student>> studentsMap = students.stream()
                .collect(Collectors.groupingBy(Student::getId));
        System.out.println(studentsMap);
    }
}
