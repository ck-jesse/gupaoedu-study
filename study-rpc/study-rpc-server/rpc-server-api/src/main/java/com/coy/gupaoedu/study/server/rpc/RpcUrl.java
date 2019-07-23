package com.coy.gupaoedu.study.server.rpc;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;

public class RpcUrl implements Serializable {

    private String protcol = "rpc";
    private String host;
    private int port;
    private String interfaceName;
    private String version;
    private String methodNames;

    /**
     * 按照url序列化
     */
    public String serialize() {
        StringBuilder url = new StringBuilder();
        url.append(protcol).append("://");
        url.append(host).append(":");
        url.append(port).append("/");
        url.append(interfaceName);

        StringBuilder params = new StringBuilder();
        if (null != version && !"".equals(version)) {
            params.append("version=").append(version).append("&");
        }
        if (null != methodNames && !"".equals(methodNames)) {
            params.append("methodNames=").append(methodNames).append("&");
        }
        if (params.length() > 0) {
            url.append("?").append(params.substring(0, params.length() - 1));
        }
        return url.toString();
    }

    /**
     * 反序列化
     */
    public void deserialize(String path) {
        try {
            System.out.println(URLDecoder.decode(path, "UTF-8"));
            String[] nodes = path.split("/");
            // 从路径中取最后一个节点
            String serviceUrl = nodes[nodes.length - 1];
            serviceUrl = URLDecoder.decode(serviceUrl, "UTF-8");
            System.out.println(serviceUrl);

            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    if ("rpc".equals(protocol)) {
                        return new URLStreamHandler() {
                            @Override
                            protected URLConnection openConnection(URL u) throws IOException {
                                System.out.println("自定义协议处理 " + u.toString());
                                return null;
                            }
                        };
                    }
                    return null;
                }
            });
            // 解析url
            URL url = new URL(serviceUrl);
            protcol = url.getProtocol();
            host = url.getHost();
            port = url.getPort();
            interfaceName = url.getPath().substring(1);
            // url参数解析
            String queryParams = url.getQuery();
            if (null != queryParams) {
                HashMap<String, String> paramMap = new HashMap<>();
                String[] params = queryParams.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    paramMap.put(keyValue[0], keyValue[1]);
                }
                version = paramMap.get("version");
                methodNames = paramMap.get("methodNames");
            }
            System.out.println("deserialize finish");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "/rpc/com.coy.gupaoedu.study.server.facade.HelloServiceFacade/providers/rpc%3A%2F%2F10.1.6.48%3A8080%2Fcom.coy.gupaoedu.study.server.facade.HelloServiceFacade%3FmethodNames%3DsaveUser%2CsayHello";
        RpcUrl rpcUrl = new RpcUrl();
        rpcUrl.deserialize(path);
    }

    public String getProtcol() {
        return protcol;
    }

    public void setProtcol(String protcol) {
        this.protcol = protcol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(String methodNames) {
        this.methodNames = methodNames;
    }
}
