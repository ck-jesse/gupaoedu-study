package com.coy.gupaoedu.study.io.bio.tomcat.http;

/**
 * @author chenck
 * @date 2019/6/18 20:35
 */
public abstract class GPServlet {

    public void service(GPRequest request, GPResponse response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public abstract void doGet(GPRequest request, GPResponse response) throws Exception;

    public abstract void doPost(GPRequest request, GPResponse response) throws Exception;
}
