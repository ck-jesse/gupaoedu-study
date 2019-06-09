package com.coy.gupaoedu.study.server.rpc;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/6/6 17:16
 */
public class RpcClientRequestHandler implements Runnable {

    Socket socket;
    Map<String, Object> handlerMap;

    public RpcClientRequestHandler(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 请求哪个类，方法名称、参数
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 通过反射调用本地服务
            Object result = invoke(rpcRequest);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 输出响应结果
            objectOutputStream.writeObject(result);
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

    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String serviceName = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();
        // 增加版本号的判断
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }
        // 获取服务bean
        Object service = handlerMap.get(serviceName);
        if (service == null) {
            throw new RuntimeException("service not found:" + serviceName);
        }
        // 拿到客户端请求的参数
        Object[] args = rpcRequest.getParameters();
        Class[] types = null;
        if (null != args) {
            // 获得每个参数的类型
            types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
        }
        // 根据请求的类进行加载
        Class clazz = Class.forName(rpcRequest.getClassName());
        // 获取对应的method
        Method method = clazz.getMethod(rpcRequest.getMethodName(), types); //sayHello, saveUser找到这个类中的方法
        // 反射调用
        Object result = method.invoke(service, args);
        return result;
    }
}
