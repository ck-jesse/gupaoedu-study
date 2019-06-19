package com.coy.gupaoedu.study.tomcat.bio.http;

/**
 * @author chenck
 * @date 2019/6/18 20:35
 */
public abstract class BioServlet {

    public void service(BioRequest request, BioResponse response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public abstract void doGet(BioRequest request, BioResponse response) throws Exception;

    public abstract void doPost(BioRequest request, BioResponse response) throws Exception;
}
