package com.coy.gupaoedu.study.spring.loadfile;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * springboot 读取 rerources 目录下文件
 *
 * springboot打包后是一个jar包，无法直接读取jar包中的文件，读取只能通过类加载器读取文件流。
 *
 * @author chenck
 * @date 2022/6/23 12:47
 */
@Component
public class ResourceLoadDemo {

    /**
     * springboot中 可以获取到文件流
     */
    public InputStream getResource1(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream inputStream = classPathResource.getInputStream();
        if (null == inputStream) {
            System.out.println("未获取到文件流");
        } else {
            System.out.println("已获取到文件流");
        }
        return inputStream;
    }

    /**
     * springboot中 可以获取到文件流
     */
    public InputStream getResource2(String fileName) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (null == inputStream) {
            System.out.println("未获取到文件流");
        } else {
            System.out.println("已获取到文件流");
        }
        return inputStream;
    }

    /**
     * springboot中 可以获取到文件流
     */
    public InputStream getResource3(String fileName) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (null == inputStream) {
            System.out.println("未获取到文件流");
        } else {
            System.out.println("已获取到文件流");
        }
        return inputStream;
    }

    /**
     * springboot中 可以获取到文件流
     */
    public InputStream getResource4(String fileName) {
        InputStream inputStream = ResourceLoadDemo.class.getClassLoader().getResourceAsStream(fileName);
        if (null == inputStream) {
            System.out.println("未获取到文件流");
        } else {
            System.out.println("已获取到文件流");
        }
        return inputStream;
    }

}
