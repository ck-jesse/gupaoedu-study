package com.coy.gupaoedu.study.io.bio.tomcat.servlet;

import com.coy.gupaoedu.study.io.bio.tomcat.http.GPRequest;
import com.coy.gupaoedu.study.io.bio.tomcat.http.GPResponse;
import com.coy.gupaoedu.study.io.bio.tomcat.http.GPServlet;

/**
 * @author chenck
 * @date 2019/6/18 20:39
 */
public class FirstServlet extends GPServlet {

    public void doGet(GPRequest request, GPResponse response) throws Exception {
        this.doPost(request, response);
    }

    public void doPost(GPRequest request, GPResponse response) throws Exception {
        response.write("This is First Serlvet");
    }

}
