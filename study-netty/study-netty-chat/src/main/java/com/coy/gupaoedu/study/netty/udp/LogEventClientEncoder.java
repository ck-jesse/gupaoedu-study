package com.coy.gupaoedu.study.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author chenck
 * @date 2019/11/22 17:14
 */
public class LogEventClientEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;

    public LogEventClientEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent msg, List<Object> out) throws Exception {
        byte[] file = msg.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] content = msg.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf byteBuf = ctx.alloc().buffer(file.length + content.length + 1);
        byteBuf.writeBytes(file);
        byteBuf.writeByte(LogEvent.SEPARATOR);
        byteBuf.writeBytes(content);
        // UDP使用DatagramPacket发送数据
        out.add(new DatagramPacket(byteBuf, remoteAddress));
    }
}