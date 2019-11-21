package com.study.jmeter.common;

/**
 * @author chenck
 * @date 2019/11/21 14:50
 */
public class UrlUtil {

    public static String buildURL(String protocol, String host, int port, String path) {
        StringBuilder url = new StringBuilder();
        url.append(protocol).append("://");
        url.append(host).append(":");
        url.append(port);
        if (path.startsWith("/")) {
            url.append(path);
        } else {
            url.append("/").append(path);
        }
        return url.toString();
    }

    public static String buildURL(String host, int port, String path) {
        return buildURL("http", host, port, path);
    }
}
