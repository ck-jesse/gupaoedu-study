package com.coy.gupaoedu.study.demo;

import org.junit.Test;

import java.io.File;

/**
 * 注：生成的二维码图片的格式为png，识别率更高
 *
 * @author chenck
 * @date 2020/7/31 10:34
 */
public class ZxingTest {

    String contents = "www.baidu.com";
    int width = 150;
    int height = 150;
    String qrcodePath = "E:/temp/qrcode.png";
    String logoPath = "E:/temp/logo/腾讯视频.jpg";

    // 解码
    public void decodeQRCode(String path) {
        String contents = ZxingUtil.decode(path);
        System.out.println(contents);
    }

    /**
     * 创建一维码
     */
    @Test
    public void createBarCode() {
        contents = "1000100002";
        width = 180;
        height = 80;
        qrcodePath = "E:/temp/barcode.png";
        ZxingUtil.createBarCode(contents, width, height, qrcodePath);

        decodeQRCode(qrcodePath);
    }

    /**
     * 创建二维码
     */
    @Test
    public void createQRCode() {
        ZxingUtil.createQRCode(contents, width, height, qrcodePath);

        decodeQRCode(qrcodePath);
    }

    /**
     * 创建二维码（含logo）
     */
    @Test
    public void createQRCodeLogo() {
        qrcodePath = "E:/temp/qrcodelogo.png";
        ZxingUtil.createQRCodeLogo(contents, width, height, qrcodePath, logoPath);

        decodeQRCode(qrcodePath);
    }

    /**
     * 解码
     */
    @Test
    public void decodeQRCode() {
        decodeQRCode("E:/temp/qrcodelogo.png");
        decodeQRCode("E:/temp/qrcode/qrcode0.png");
    }

    /**
     * 批量生成二维码
     * 结果：生成1000个，总耗时 11107 ms，也就是说 11s，1s种生成100个
     */
    @Test
    public void batchCreateQRCodeLogo() {
        String imgPath = "E:/temp/qrcode/qrcode";
        String name = "";
        long initstart = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            name = imgPath + i + ".png";
            long start = System.currentTimeMillis();
            ZxingUtil.createQRCodeLogo(contents + i, width, height, name, logoPath);
            long end = System.currentTimeMillis();
            System.out.println(i + " " + name + " " + (end - start) + " ms");
        }
        long end = System.currentTimeMillis();
        System.out.println("总耗时 " + (end - initstart) + " ms");
    }

    /**
     * 批量识别二维码
     * 结果：识别1000个，总耗时 2331 ms
     */
    @Test
    public void batchDecodeQRCodeLogo() {
        long initstart = System.currentTimeMillis();

        File file = new File("E:/temp/qrcode");

        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            long start = System.currentTimeMillis();

            String contents = ZxingUtil.decode(files[i]);

            long end = System.currentTimeMillis();
            System.out.println(files[i].getName() + " " + contents + " " + (end - start) + " ms");
        }
        long end = System.currentTimeMillis();
        System.out.println("总耗时 " + (end - initstart) + " ms");
    }
}
