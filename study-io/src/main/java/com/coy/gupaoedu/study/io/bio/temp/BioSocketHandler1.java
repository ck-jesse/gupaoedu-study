package com.coy.gupaoedu.study.io.bio.temp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author chenck
 * @date 2019/6/12 21:42
 */
public class BioSocketHandler1 implements Runnable {

    Socket socket;

    public BioSocketHandler1(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            // 输入流，读取请求数据
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            String content = (String) objectInputStream.readObject();
            System.out.println("BIOServer:收到请求数据： " + content);

            // TODO 为什么将socket.getOutputStream()包装为ObjectOutputStream，就可以将数据输出给客户端呢？所谓的全双工？
            // 输出流，输出响应数据
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("Hello,i am BIOServer");
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
