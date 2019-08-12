package com.coy.gupaoedu.study.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 缓冲区分片
 * 缓冲区片对于促进抽象非常有帮助。
 * 可以编写自己的函数处理整个缓冲区，而且如果想要将这个过程应用于子缓冲区上，只需取主缓冲区的一个片，并将它传递给您的函数。
 * 这比编写自己的函数来取额外的参数以指定要对缓冲区的哪一部分进行操作更容易。
 */
public class BufferSlice {

    static public void main(String args[]) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 缓冲区中的数据0-9  
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }

        // 创建子缓冲区  
        buffer.position(3);
        buffer.limit(7);
        // 创建一个新的缓冲区，新缓冲区与原来的缓冲区的一部分共享数据。
        ByteBuffer slice = buffer.slice();

        // 改变子缓冲区的内容  
        for (int i = 0; i < slice.capacity(); ++i) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }
    }
}