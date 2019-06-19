package com.coy.gupaoedu.study.tomcat.bio.servlet;


import com.coy.gupaoedu.study.tomcat.bio.http.BioRequest;
import com.coy.gupaoedu.study.tomcat.bio.http.BioResponse;
import com.coy.gupaoedu.study.tomcat.bio.http.BioServlet;

/**
 * @author chenck
 * @date 2019/6/18 20:39
 */
public class BioFirstServlet extends BioServlet {

    public void doGet(BioRequest request, BioResponse response) throws Exception {
        this.doPost(request, response);
    }

    public void doPost(BioRequest request, BioResponse response) throws Exception {
        response.write("This is First Serlvet");
    }

}
