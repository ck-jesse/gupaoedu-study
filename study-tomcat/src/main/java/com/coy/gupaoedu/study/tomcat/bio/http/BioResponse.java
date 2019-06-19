package com.coy.gupaoedu.study.tomcat.bio.http;

import java.io.OutputStream;

/**
 * @author chenck
 * @date 2019/6/18 20:38
 */
public class BioResponse {
    private OutputStream out;

    public BioResponse(OutputStream out) {
        this.out = out;
    }

    public void write(String s) throws Exception {
        //用的是HTTP协议，输出也要遵循HTTP协议
        //给到一个状态码 200
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(s);
        out.write(sb.toString().getBytes());
    }
}
