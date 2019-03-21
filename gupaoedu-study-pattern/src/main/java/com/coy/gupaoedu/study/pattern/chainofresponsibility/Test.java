package com.coy.gupaoedu.study.pattern.chainofresponsibility;

/**
 * 责任链模式：Chain Of Responsibility Pattern
 * 优点：
 * 1、降低耦合度，使请求的发送者和接收者解耦，便于灵活的、可插拔的定义请求处理过程；
 * 2、简化、封装了请求的处理过程，并且这个过程对客户端而言是透明的，以便于动态地重新组织链以及分配责任，增强请求处理的灵活性；
 * <p>
 * 责任链模式通过建立一条链来组织请求的处理者，请求将沿着链进行传递，而请求发送者无须知道请求在何时、何处以及如何被处理，实现了请求发送者与处理者的解耦。
 * <p>
 * 在实际软件开发中，如果遇到有多个对象可以处理同一请求时可以考虑使用职责链模式。
 * 最常见的例子包括在 Java Web 应用开发中创建一个过滤器（Filter）链来对请求数据进行过滤（中文字符乱码的处理）、在工作流系统中实现公文的分级审批、在Struts应用中添加不同的拦截器(常用的有类型转化、异常处理，数据校验…)以增强Struts2的功能等。
 * 开放平台Openapi应用中添加不同的过滤器（如：参数验证过滤器、签名过滤器、验签过滤器、IP白名单过滤器、权限控制过滤器、流控过滤器、时间戳校验过滤器、检查提交数据大小过滤器等）
 * <p>
 * 参考：https://blog.csdn.net/justloveyou_/article/details/68489505
 *
 * @author chenck
 * @date 2019/3/21 20:00
 */
public class Test {

    @org.junit.Test
    public void test1() {
        // 主任
        Approver director = new Director();
        // 副董事长
        Approver vicePresident = new VicePresident();
        // 董事长
        Approver president = new President();
        // 董事会
        Approver congress = new Congress();

        // 创建责任链
        director.setSuccessor(vicePresident);
        vicePresident.setSuccessor(president);
        president.setSuccessor(congress);

        // 创建采购单，并从主任开始处理
        PurchaseRequest pr1 = new PurchaseRequest(45000, 10001, "购买倚天剑");
        director.processRequest(pr1);

        PurchaseRequest pr2 = new PurchaseRequest(60000, 10002, "购买《葵花宝典》");
        director.processRequest(pr2);

        PurchaseRequest pr3 = new PurchaseRequest(160000, 10003, "购买《金刚经》");
        director.processRequest(pr3);

        PurchaseRequest pr4 = new PurchaseRequest(800000, 10004, "购买桃花岛");
        director.processRequest(pr4);
    }

    /**
     * 增加了经理类，在将经理作为主任的下家
     *
     * @param
     * @author chenck
     * @date 2019/3/21 20:49
     */
    @org.junit.Test
    public void test2() {
        // 主任
        Approver director = new Director();
        // 副董事长
        Approver vicePresident = new VicePresident();
        // 董事长
        Approver president = new President();
        // 董事会
        Approver congress = new Congress();
        // 经理
        Approver manager = new Manager();

        // 创建责任链
        director.setSuccessor(manager);
        // 将经理作为主任的下家
        manager.setSuccessor(vicePresident);
        // 将副董事长作为经理的下家
        vicePresident.setSuccessor(president);
        president.setSuccessor(congress);

        // 创建采购单，并从主任开始处理
        PurchaseRequest pr1 = new PurchaseRequest(45000, 10001, "购买倚天剑");
        director.processRequest(pr1);

        PurchaseRequest pr2 = new PurchaseRequest(60000, 10002, "购买《葵花宝典》");
        director.processRequest(pr2);

        PurchaseRequest pr3 = new PurchaseRequest(160000, 10003, "购买《金刚经》");
        director.processRequest(pr3);

        PurchaseRequest pr4 = new PurchaseRequest(800000, 10004, "购买桃花岛");
        director.processRequest(pr4);
    }
}
