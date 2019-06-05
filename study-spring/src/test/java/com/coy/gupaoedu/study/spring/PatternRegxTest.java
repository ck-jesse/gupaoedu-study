package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.demo.mvc.action.TwoAction;
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

    @Test
    public void patternTest() throws NoSuchMethodException {
        String aspectPointcut = " * com.coy.gupaoedu.study.spring.demo..*Action..*(.*)".trim();

        aspectPointcut = aspectPointcut
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        if (aspectPointcut.startsWith("public")) {
            aspectPointcut = aspectPointcut.replaceAll("public\\s+\\*\\s+", "public +\\\\w+ +");
        }
        if (aspectPointcut.startsWith("*")) {
            aspectPointcut = aspectPointcut.replaceAll("\\*\\s+", "[\\\\w\\\\s]* +");
        }

        System.out.println(aspectPointcut);

        TwoAction testAction = new TwoAction();
        String clazz = testAction.getClass().getName();
        System.out.println(clazz);
        String pointCutForClassRegex = aspectPointcut.substring(0, aspectPointcut.lastIndexOf("\\(") - 4);
        pointCutForClassRegex = pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" +") + 2);
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

        aspectPointcut = convertToRegx(aspectPointcut);
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

    /**
     * 将pointcut表达式转换为正则表达式
     */
    public String convertToRegx(String aspectPointcut) {
        String aspectPointcutRegx = aspectPointcut
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)").trim();
        // 以public开头表达式的处理
        if (aspectPointcutRegx.startsWith("public")) {
            aspectPointcutRegx = aspectPointcutRegx.replaceAll("public\\s+\\*\\s+", "public +[\\\\w\\\\s.*]* +");
        }
        // 以*开头表达式的处理
        if (aspectPointcutRegx.startsWith("*")) {
            aspectPointcutRegx = aspectPointcutRegx.replaceAll("\\*\\s+", "[\\\\w\\\\s]* +");
        }
        return aspectPointcutRegx;
    }

    @Test
    public void test1() {
        String aspectPointcut = "public * com.coy.gupaoedu.study.spring.demo..*Service..*(.*)";
        System.out.println(aspectPointcut);

        aspectPointcut = convertToRegx(aspectPointcut);
        System.out.println(aspectPointcut);
        Pattern methodPattern = Pattern.compile(aspectPointcut);

        String methodString = "public abstract java.lang.String com.coy.gupaoedu.study.spring.demo.service.DemoService.get(java.lang.String)";
        System.out.println(methodString);
        System.out.println(methodPattern.matcher(methodString).matches());

    }

}
