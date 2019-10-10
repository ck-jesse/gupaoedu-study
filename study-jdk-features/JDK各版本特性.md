# JDK 各版本特性
> 参考链接地址：https://openjdk.java.net/projects/jdk

## JDK1.5
1. 自动装箱与拆箱
2. 枚举
3. 静态导入
4. 可变参数（Varargs）
5. 内省（Introspector）
6. 泛型(Generic)
7. For-Each循环

## JDK 1.6
1. Desktop类和SystemTray类
2. 使用JAXB2来实现对象与XML之间的映射
3. 理解StAX
4. 使用Compiler API
5. 轻量级Http Server API
6. 插入式注解处理API(Pluggable Annotation Processing API)
7. 用Console开发控制台程序
8. 对脚本语言的支持如: ruby, groovy, javascript
9. Common Annotations

## JDK 1.7
1. switch中可以使用字串
2. "<-“这个玩意儿的运用List tempList = new ArrayList<-(); 即泛型实例化类型自动推断
3. 自定义自动关闭类
4. 新增一些取环境信息的工具方法
5. Boolean类型反转，空指针安全，参与位运算
6. 两个char间的equals
7. 安全的加减乘除
8. 对Java集合（Collections）的增强支持
9. 数值可加下划线
10. 支持二进制文字
11. 简化了可变参数方法的调用
12. 在try catch异常扑捉中，一个catch可以写多个异常类型，用”|"隔开
13. jdk7之前，你必须用try{}finally{}在try内使用资源，在finally中关闭资源，不管try中的代码是否正常退出或者异常退出。jdk7之后，你可以不必要写finally语句来关闭资源，只要你在try()的括号内部定义要使用的资源

## JDK 1.8
1. 接口的默认方法
2. Lambda 表达式
3. 函数式接口
4. 方法与构造函数引用
5. Lambda 作用域
6. 访问局部变量
7. 访问对象字段与静态变量
8. 访问接口的默认方法
9. Date API
10. Annotation 注解

## JDK 1.9
> 给高手用的版本!!
1. Java平台级模块系统
2. 响应式编程模型Reactive
3. Linking
4. JShell：交互式Java REPL
5. 改进的Javadoc
6. 集合工厂方法
7. 改进的Stream API
8. 私有接口方法
9. HTTP/2
10. 多版本兼容JAR

## JDK10
1. 局部变量类型推断
2. 应用类数据共享(CDS)
3. 额外的 Unicode 语言标签扩展
4. 基于时间的版本控制
5. 根证书
6. 并行全垃圾回收器 G1
7. 移除 Native-Header 自动生成工具
8. 垃圾回收器接口
9. 线程-局部变量管控
10. 在备用存储装置上的堆分配
11. 试验性的基于 Java 的 JIT 编译器
12. 合并 JDK 多个代码仓库到一个单独的储存库中

## JDK11
1. 本地变量类型推断
2. 字符串加强
3. 集合加强
4. Stream 加强
5. Optional 加强
6. InputStream 加强
7. HTTP Client API
8. 化繁为简，一个命令编译运行源代码

## JDK12
1. Shenandoah：低暂停时间的 GC（实验性功能）
2. 微基准测试套件
3. Switch 表达式（预览功能）
4. JVM 常量 API
5. 只保留一个 AArch64 实现
6. 默认类数据共享归档文件
7. 可中止的 G1 混合 GC
8. G1 及时返回未使用的已分配内存

## JDK13
1. Dynamic CDS Archives
2. ZGC: Uncommit Unused Memory
3. Reimplement the Legacy Socket API
4. Switch Expressions (Preview)
5. Text Blocks (Preview)

