package com.coy.gupaoedu.study.server.rpc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.regex.Pattern;

@Data
@Slf4j
public class RpcUrl implements Serializable {

    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 协议
     */
    private String protcol = "rpc";
    /**
     * 服务IP地址
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    /**
     * 服务接口全路径
     */
    private String interfaceName;
    /**
     * 服务版本号
     */
    private String version;
    /**
     * 服务方法名称
     */
    private String methodNames;
    /**
     * 服务角色，providers 表示服务提供方；consumers 表示服务消费方
     */
    private String side;


    public String getServiceName() {
        if (null == version || "".equals(version)) {
            return interfaceName;
        }
        return interfaceName + "-" + version;
    }

    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

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
        if (null != applicationName && !"".equals(applicationName)) {
            params.append("applicationName=").append(applicationName).append("&");
        }
        if (null != version && !"".equals(version)) {
            params.append("version=").append(version).append("&");
        }
        if (null != methodNames && !"".equals(methodNames)) {
            params.append("methodNames=").append(methodNames).append("&");
        }
        if (null != side && !"".equals(side)) {
            params.append("side=").append(side).append("&");
        }
        if (params.length() > 0) {
            url.append("?").append(params.substring(0, params.length() - 1));
        }
        return url.toString();
    }

    /**
     * 反序列化
     */
    public static RpcUrl deserialize(String path) {
        try {
            String[] nodes = path.split("/");
            // 从路径中取最后一个节点
            String serviceNodeUrl = nodes[nodes.length - 1];
            serviceNodeUrl = URLDecoder.decode(serviceNodeUrl, "UTF-8");

            // 校验serviceNodeUrl是否匹配规则
            String regex = "^(https|http|rpc|dubbo)?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            Pattern pattern = Pattern.compile(regex);
            if (!pattern.matcher(serviceNodeUrl).matches()) {
                log.info("path路径中最后一个节点不是service node，不进行反序列化； serviceNodeUrl={}", serviceNodeUrl);
                return null;
            }

            // 解析url
            URL url = new URL(null, serviceNodeUrl, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) throws IOException {
                    log.info("自定义协议处理 Url={}", u.toString());
                    return null;
                }
            });

            RpcUrl rpcUrl = new RpcUrl();
            rpcUrl.setProtcol(url.getProtocol());
            rpcUrl.setHost(url.getHost());
            rpcUrl.setPort(url.getPort());
            rpcUrl.setInterfaceName(url.getPath().substring(1));

            // url参数解析
            String queryParams = url.getQuery();
            if (null != queryParams) {
                HashMap<String, String> paramMap = new HashMap<>();
                String[] params = queryParams.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    paramMap.put(keyValue[0], keyValue[1]);
                }
                rpcUrl.setApplicationName(paramMap.get("applicationName"));
                rpcUrl.setVersion(paramMap.get("version"));
                rpcUrl.setMethodNames(paramMap.get("methodNames"));
                rpcUrl.setSide(paramMap.get("side"));
            }
            return rpcUrl;
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    public static void main(String[] args) {
        String path = "/rpc/com.coy.gupaoedu.study.server.facade.HelloServiceFacade/providers/rpc%3A%2F%2F10.1.6.48%3A8080%2Fcom.coy.gupaoedu.study" +
                ".server.facade.HelloServiceFacade%3FmethodNames%3DsaveUser%2CsayHello";
        RpcUrl rpcUrl = new RpcUrl();
        System.out.println(RpcUrl.deserialize(path));
        ;
    }

}
