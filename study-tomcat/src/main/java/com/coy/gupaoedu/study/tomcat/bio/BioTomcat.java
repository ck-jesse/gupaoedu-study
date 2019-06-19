package com.coy.gupaoedu.study.tomcat.bio;


import com.coy.gupaoedu.study.tomcat.WebXmlParseUtil;
import com.coy.gupaoedu.study.tomcat.bio.http.BioRequest;
import com.coy.gupaoedu.study.tomcat.bio.http.BioResponse;
import com.coy.gupaoedu.study.tomcat.bio.http.BioServlet;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 简易版的tomcat
 * 基于BIO的ServerSocket来实现
 * tomcat7中自动选取BIO来实现
 * tomcat8中自动选取NIO来实现
 *
 * @author chenck
 * @date 2019/6/18 20:28
 */
public class BioTomcat {
    private int port = 8080;
    private ServerSocket server;

    private Map<String, BioServlet> servletMapping = new HashMap<String, BioServlet>();
    // J2EE标准
    // Servlet
    // Request
    // Response

    //1、配置好启动端口，默认8080  ServerSocket  IP:localhost
    //2、配置web.xml 自己写的Servlet继承HttpServlet
    //   servlet-name
    //   servlet-class
    //   url-pattern
    //3、读取配置，url-pattern  和 Servlet建立一个映射关系
    //   Map servletMapping

    public void start() {
        //1、加载配置文件，初始化ServeltMapping
        servletMapping = WebXmlParseUtil.<BioServlet>parse("web-nio.properties");
        try {
            // 创建服务端ServerSocket
            server = new ServerSocket(this.port);

            System.out.println("BIO Tomcat 已启动，监听的端口是：" + this.port);

            //2、等待用户请求,用一个死循环来等待用户请求
            while (true) {
                // 获取客户端请求Socket
                Socket client = server.accept();
                // 4、HTTP请求，发送的数据就是字符串，有规律的字符串（HTTP协议）
                process(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(Socket client) throws Exception {

        InputStream is = client.getInputStream();
        OutputStream os = client.getOutputStream();

        //7、Request(InputStrean)/Response(OutputStrean)
        BioRequest request = new BioRequest(is);
        BioResponse response = new BioResponse(os);

        //5、从协议内容中拿到URL，把相应的Servlet用反射进行实例化
        String url = request.getUrl();

        if (servletMapping.containsKey(url)) {
            //6、调用实例化对象的service()方法，执行具体的逻辑doGet/doPost方法
            servletMapping.get(url).service(request, response);
        } else {
            response.write("404 - Not Found");
        }

        os.flush();
        os.close();

        is.close();
        client.close();
    }

    /**
     * 简易版的tomcat
     * 使用：
     * 浏览器中输入地址： http://127.0.0.1:8080/firstServlet.do
     * 浏览器中输入地址： http://127.0.0.1:8080/secondServlet.do
     */
    public static void main(String[] args) {
        new BioTomcat().start();
    }
}
