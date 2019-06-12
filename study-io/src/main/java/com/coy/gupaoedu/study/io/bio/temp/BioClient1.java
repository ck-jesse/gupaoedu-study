package com.coy.gupaoedu.study.io.bio.temp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author chenck
 * @date 2019/6/12 22:04
 */
public class BioClient1 {

    public static void main(String[] args) {
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            // 建立连接
            socket = new Socket("localhost", 8080);

            // 输出流，输出请求数据
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("Hello,i am BioClient");
            outputStream.flush();

            // TODO 为什么将socket.getInputStream()包装为ObjectInputStream，就可以获取到服务端输出的数据流呢？所谓的全双工？
            // 输入流，读取响应数据
            inputStream = new ObjectInputStream(socket.getInputStream());
            String result = (String) inputStream.readObject();
            System.out.println("BioClient:收到响应数据： " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
