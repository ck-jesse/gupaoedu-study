package com.coy.gupaoedu.study.io.nio.channel;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileInputDemo {

    private static Charset charset = Charset.forName("UTF-8");

    static public void main(String args[]) throws Exception {
        FileInputStream fin = new FileInputStream("E://咕泡学院.txt");


        // 获取通道
        FileChannel fc = fin.getChannel();

        // 创建缓冲区  
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            // 清除缓存
            buffer.clear();
            // 读取数据到缓冲区
            int c = fc.read(buffer);
            if (c == -1) {
                break;
            }
            buffer.flip();
            // 借助Charset来读取buffer
            CharBuffer content = charset.decode(buffer);
            System.out.println(content);

            // 上面读取以后 buffer.remaining() = 0 ，所以不会进入下面的循环
            // buffer.remaining() 表示当前是否有可读的数据，实质为 limit-position=0，所以无可读取的数据
            while (buffer.remaining() > 0) {
                byte b = buffer.get();
                System.out.print(((char) b));
            }
        }

        fin.close();
    }
}