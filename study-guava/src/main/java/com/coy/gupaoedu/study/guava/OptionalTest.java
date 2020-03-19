package com.coy.gupaoedu.study.guava;

import org.junit.Test;

import java.util.Optional;

/**
 * @author chenck
 * @date 2020/3/18 19:41
 */
public class OptionalTest {

    @Test
    public void ofNullTest() {
        // 创建指定引用的Optional实例，若引用为null则快速失败
        Optional.of(null);
    }

    @Test
    public void ofTest() {
        // Optional.of 创建指定引用的Optional实例，若引用为null则快速失败
        Optional<String> name = Optional.of("coy");
        name = Optional.ofNullable(null);

        // name.isPresent 如果Optional包含非null的引用（引用存在），返回true
        System.out.println(name.isPresent());

        // name.get 返回Optional所包含的引用，若引用缺失，则抛出java.lang.IllegalStateException
        System.out.println(name.get());

        // name.orElse 返回所包含的实例(如果存在);否则为默认值
        System.out.println(name.orElse("cck"));

        System.out.println(name.toString());
    }

    @Test
    public void sumTest() {
        Integer value1 = null;
        Integer value2 = new Integer(10);
        //Optional.fromNullable - 允许参数为null.
        Optional<Integer> a = Optional.ofNullable(value1);
        //Optional.of - 参数不能为null
        Optional<Integer> b = Optional.of(value2);

        System.out.println(sum(a, b));


    }

    public Integer sum(Optional<Integer> a, Optional<Integer> b) {
        //Optional.isPresent - 检查值是否存在
        System.out.println("First parameter is present: " + a.isPresent());
        System.out.println("Second parameter is present: " + b.isPresent());

        //Optional.or - 所包含的实例如果存在，则返回实例的值，否则返回默认值.
        Integer value1 = a.orElse(new Integer(0));

        //Optional.get - 返回实例的值，实例必须存在
        Integer value2 = b.get();

        return value1 + value2;
    }


}
