package com.coy.gupaoedu.study.demo;

import com.google.zxing.BarcodeFormat;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 注：生成的二维码图片的格式为png，识别率更高
 *
 * @author chenck
 * @date 2020/7/31 10:34
 */
public class ZxingConfigTest {

    String contents = "www.baidu.com";
    int width = 150;
    int height = 150;
    String qrcodePath = "E:/temp/qrcode.png";
    String logoPath = "E:/temp/logo/腾讯视频.jpg";
    String words = "测试商户";

    // 解码
    public void decodeQRCode(String path) {
        String contents = ZxingConfigUtil.decode(path);
        System.out.println(contents);
    }

    /**
     * 创建一维码
     */
    @Test
    public void createBarCode() {
        contents = "10001000020003";
        width = 150;
        height = 80;
        qrcodePath = "E:/temp/barcode.png";
        ZxingConfigUtil.createBarCode(contents, width, height, qrcodePath);

        decodeQRCode(qrcodePath);
    }

    /**
     * 创建二维码
     */
    @Test
    public void createQRCode() {
        ZxingConfigUtil.createQRCode(contents, width, height, qrcodePath);

        decodeQRCode(qrcodePath);
    }

    /**
     * 创建二维码（含logo）
     */
    @Test
    public void createQRCodeLogo() {
        qrcodePath = "E:/temp/qrcodelogo.png";
        ZxingConfigUtil.createQRCodeLogo(contents, width, height, qrcodePath, logoPath);

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
        String imgPath = "E:/temp/qrcode1/qrcode";
        String name = "";
        ZxingConfig config = new ZxingConfig();
        long initstart = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            name = imgPath + i + ".png";
            long start = System.currentTimeMillis();

            config.setFormat(BarcodeFormat.QR_CODE);
            config.setContents(contents + i);
            config.setWidth(width);
            config.setHeight(height);
            config.setQrcodePath(name);
            config.setLogoPath(logoPath);
            ZxingConfigUtil.encode(config);
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

    /**
     *
     */
    @Test
    public void encode() {
        contents = "1000100002000300";
        words = "1000100002000300";
        words = "中华人民";
        width = 150;
        height = 150;
        qrcodePath = "E:/temp/barcode3.png";
        ZxingConfig config = new ZxingConfig();
        config.setFormat(BarcodeFormat.CODE_128);
        config.setFormat(BarcodeFormat.QR_CODE);
        config.setContents(contents);
        config.setWidth(width);
        config.setHeight(height);
        config.setQrcodePath(qrcodePath);
        config.setLogoPath(logoPath);
//        config.setShowWords(true);
//        config.setWords(words);

        ZxingConfigUtil.encode(config);

        decodeQRCode(qrcodePath);
    }

    /**
     * 多线程版本的测试
     */
    @Test
    public void batchCreateQRCodeLogoThread() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(8);

        int total = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(total);
        String imgPath = "E:/temp/qrcode1/qrcode";
        String name = "";
        long initstart = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            name = imgPath + i + ".png";

            int finalI = i;
            String finalName = name;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();

                    ZxingConfig config = new ZxingConfig();
                    config.setFormat(BarcodeFormat.QR_CODE);
                    config.setContents(contents + finalI);
                    config.setWidth(width);
                    config.setHeight(height);
                    config.setQrcodePath(finalName);
                    config.setLogoPath(logoPath);
                    ZxingConfigUtil.encode(config);

                    long end = System.currentTimeMillis();
                    System.out.println(Thread.currentThread().getName() + " " + finalI + " " + finalName + " " + (end - start) + " ms");
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("总耗时 " + (end - initstart) + " ms");
    }
}
