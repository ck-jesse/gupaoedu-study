package com.coy.gupaoedu.study.io.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author chenck
 * @date 2019/6/12 21:42
 */
public class BioSocketHandler implements Runnable {

    Socket socket;

    public BioSocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            // 输入流，读取请求数据
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String context = bufferedReader.readLine();
            System.out.println("BIOServer:收到请求数据： " + context);

            // TODO 将socket.getOutputStream()包装为OutputStreamWriter，是否可以将数据输出给客户端？
            // 输出流，输出响应数据
            //bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //bufferedWriter.write("Hello,i am BIOServer");
            //bufferedWriter.flush();
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
