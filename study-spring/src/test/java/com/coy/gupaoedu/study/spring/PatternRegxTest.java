package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.demo.mvc.action.TwoAction;
import com.coy.gupaoedu.study.spring.framework.aop.aspectj.GPAspectJAdvisorFactory;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 正则表达式的测试
 *
 * @author chenck
 * @date 2019/4/19 16:10
 */
public class PatternRegxTest {

    public static String classRegexDeal(String typePattern) {
        // 获取class的正则表达式
        String classPointCutRegex = typePattern.substring(0, typePattern.lastIndexOf("\\(") - 4);
        if (classPointCutRegex.indexOf(" +") != -1) {
            classPointCutRegex = classPointCutRegex.substring(classPointCutRegex.lastIndexOf(" +") + 2);
        }
        return classPointCutRegex;
    }

    @Test
    public void patternTest() throws NoSuchMethodException {
        String aspectPointcut = " * com.coy.gupaoedu.study.spring.demo..*Action..*(.*)".trim();

        aspectPointcut = GPAspectJAdvisorFactory.convertToRegx(aspectPointcut);

        System.out.println(aspectPointcut);

        TwoAction testAction = new TwoAction();
        String clazz = testAction.getClass().getName();
        System.out.println(clazz);
        String pointCutForClassRegex = classRegexDeal(aspectPointcut);
        System.out.println("classPointcut=" + pointCutForClassRegex);
        Pattern pointCutClassPattern = Pattern.compile(pointCutForClassRegex);

        System.out.println(pointCutClassPattern.matcher(clazz).matches());
        System.out.println();


        System.out.println("methodPointcut=" + aspectPointcut);
        Pattern methodPattern = Pattern.compile(aspectPointcut);

        for (Method method : testAction.getClass().getMethods()) {
            String methodString = method.toString();
            if (methodString.contains("throws")) {
                methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
            }
            if (methodPattern.matcher(methodString).matches()) {
                System.out.println(method.getName());
                System.out.println(methodString);
                System.out.println();
            }
        }

    }

    @Test
    public void test() {
        String aspectPointcut = "public * com.coy.gupaoedu.study.spring.demo..*Action..*(.*)";
        System.out.println(aspectPointcut);

        aspectPointcut = GPAspectJAdvisorFactory.convertToRegx(aspectPointcut);
        System.out.println(aspectPointcut);
        Pattern methodPattern = Pattern.compile(aspectPointcut);

        String methodString = "public void com.coy.gupaoedu.study.spring.demo.mvc.action.TwoAction.getName(java.lang.String)";
        System.out.println(methodString);
        System.out.println(methodPattern.matcher(methodString).matches());

        // 从正则表达式中截取class的表达式
        String classPointCutRegex = aspectPointcut.substring(0, aspectPointcut.lastIndexOf("\\(") - 4);
        if (classPointCutRegex.indexOf(" +") != -1) {
            classPointCutRegex = classPointCutRegex.substring(classPointCutRegex.lastIndexOf(" +") + 2);
        }
        System.out.println(classPointCutRegex);
    }

    @Test
    public void classPatternTest() {
        String aspectPointcut = "public * com.coy.gupaoedu.study.spring.demo..*Service.*..*(.*)";
        System.out.println(aspectPointcut);

        aspectPointcut = GPAspectJAdvisorFactory.convertToRegx(aspectPointcut);
        System.out.println(aspectPointcut);
        aspectPointcut = classRegexDeal(aspectPointcut);
        System.out.println(aspectPointcut);
        Pattern classPattern = Pattern.compile(aspectPointcut);

        String classString = "public abstract java.lang.String com.coy.gupaoedu.study.spring.demo.service.DemoService";
        classString = "com.coy.gupaoedu.study.spring.demo.service.impl.UserService";
        classString = "com.coy.gupaoedu.study.spring.demo.service.DemoServiceImpl";
        System.out.println(classString);
        System.out.println(classPattern.matcher(classString).matches());

    }

    @Test
    public void methodPatternTest() {
        String aspectPointcut = "public * com.coy.gupaoedu.study.spring.demo..*Service..*(.*)";
        System.out.println(aspectPointcut);

        aspectPointcut = GPAspectJAdvisorFactory.convertToRegx(aspectPointcut);
        System.out.println(aspectPointcut);
        Pattern methodPattern = Pattern.compile(aspectPointcut);

        String methodString = "public abstract java.lang.String com.coy.gupaoedu.study.spring.demo.service.DemoService.get(java.lang.String)";
        System.out.println(methodString);
        System.out.println(methodPattern.matcher(methodString).matches());

    }

}
