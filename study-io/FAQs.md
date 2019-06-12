# IO 常见问题解答

### 1、NIO流程这块怎么跟jack老师讲的不一样？
    jack讲的NIO逻辑： channel->select(轮询)-buffer
    tom讲的NIO逻辑：channel->buffer-select(轮询)
    那到底buffer是在哪个位置的呢？
    答：
    
#### 2、通过selectionKey。channel()可以获取到ServerSocketChannel和SocketChannel，原因是多路复用，能具体介绍一下吗？
    答：

#### 3、为什么将socket.getInputStream()包装为InputStreamReader，不能获取到服务端输出的数据流呢？所谓的半双工？
    答：

#### 4、为什么将socket.getInputStream()包装为ObjectInputStream，就可以获取到服务端输出的数据流呢？所谓的全双工？
    答：