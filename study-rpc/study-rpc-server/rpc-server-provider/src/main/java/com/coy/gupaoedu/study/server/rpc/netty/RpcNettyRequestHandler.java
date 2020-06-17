package com.coy.gupaoedu.study.server.rpc.netty;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.server.rpc.RpcInvoker;
import com.coy.gupaoedu.study.server.rpc.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于netty的客户端请求处理器
 *
 * @author chenck
 * @date 2019/6/19 15:08
 */
@Slf4j
public class RpcNettyRequestHandler extends ChannelInboundHandlerAdapter {

    private RpcInvoker rpcInvoker;

    public RpcNettyRequestHandler(RpcInvoker rpcInvoker) {
        this.rpcInvoker = rpcInvoker;
    }

    /**
     * 不管是客户端还是服务端，在收到数据后都会调用channelRead(...)方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
        RpcRequest rpcRequest = (RpcRequest) msg;
        // 通过反射调用本地服务
        log.info("请求参数：{}", JSON.toJSONString(rpcRequest));
        Object result = rpcInvoker.invoke(rpcRequest);
        log.info("响应参数：{}", JSON.toJSONString(result));

        // 往客户端写数据
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
