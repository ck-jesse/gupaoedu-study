package com.coy.gupaoedu.study.io.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author chenck
 * @date 2019/6/12 22:04
 */
public class BioClient {

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            // 建立连接
            socket = new Socket("localhost", 8080);

            // 输出流，输出请求数据
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("Hello,i am BioClient");
            bufferedWriter.flush();

            // TODO 为什么将socket.getInputStream()包装为InputStreamReader，不能获取到服务端输出的数据流呢？所谓的半双工？
            // 输入流
            // bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // String context = bufferedReader.readLine();
            // System.out.println("BioClient:收到响应数据： " + context);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
