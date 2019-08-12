package com.coy.gupaoedu.study.io.nio.buffer;

import java.nio.*;

/**
 * 只读缓冲区
 * 只读缓冲区非常简单 — 可以读取它们，但是不能向它们写入。
 */
public class ReadOnlyBuffer {
    
    static public void main(String args[]) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 缓冲区中的数据0-9
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }

        // 创建只读缓冲区，返回一个与原缓冲区完全相同的缓冲区(并与其共享数据)，只不过它是只读的
        ByteBuffer readonly = buffer.asReadOnlyBuffer();

        // 改变原缓冲区的内容
        for (int i = 0; i < buffer.capacity(); ++i) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }

        readonly.position(0);
        readonly.limit(buffer.capacity());

        // 只读缓冲区的内容也随之改变
        while (readonly.remaining() > 0) {
            System.out.println(readonly.get());
        }
    }
}