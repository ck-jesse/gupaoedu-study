6、ChannelPipeline
Netty 在事件处理上，是通过ChannelPipeline来控制事件流，通过调用注册其上的一系列ChannelHandler来处理事件，这也是典型的拦截 器模式。下面是和ChannelPipeline相关的接口及类图：

![946424-20180831170216092-590227414](C:\Users\chenck\Desktop\946424-20180831170216092-590227414.png)

事件流有两种，upstream事件和downstream事件。在ChannelPipeline中，其可被注册的ChannelHandler既可以 是 ChannelUpstreamHandler 也可以是ChannelDownstreamHandler ，但事件在ChannelPipeline传递过程中只会调用匹配流的ChannelHandler。在事件流的过滤器链 中，ChannelUpstreamHandler或ChannelDownstreamHandler既可以终止流程，也可以通过调用 ChannelHandlerContext.sendUpstream(ChannelEvent)或 ChannelHandlerContext.sendDownstream(ChannelEvent)将事件传递下去。下面是事件流处理的图示：

![946424-20180831170259727-1690307948](C:\Users\chenck\Desktop\946424-20180831170259727-1690307948.jpg)



从上图可见，upstream event是被Upstream Handler们自底向上逐个处理，downstream event是被Downstream Handler们自顶向下逐个处理，这里的上下关系就是向ChannelPipeline里添加Handler的先后顺序关系。简单的理 解，upstream event是处理来自外部的请求的过程，而downstream event是处理向外发送请求的过程。

服务端处 理请求的过程通常就是解码请求、业务逻辑处理、编码响应，构建的ChannelPipeline也就类似下面的代码片断：

ChannelPipeline pipeline = Channels.pipeline();
pipeline.addLast("decoder", new MyProtocolDecoder());
pipeline.addLast("encoder", new MyProtocolEncoder());
pipeline.addLast("handler", new MyBusinessLogicHandler());
其中，MyProtocolDecoder是ChannelUpstreamHandler类型，MyProtocolEncoder是 ChannelDownstreamHandler类型，MyBusinessLogicHandler既可以是 ChannelUpstreamHandler类型，也可兼ChannelDownstreamHandler类型，视其是服务端程序还是客户端程序以及 应用需要而定。

补充一点，Netty对抽象和实现做了很好的解耦。像org.jboss.netty.channel.socket包， 定义了一些和socket处理相关的接口，而org.jboss.netty.channel.socket.nio、 org.jboss.netty.channel.socket.oio等包，则是和协议相关的实现。