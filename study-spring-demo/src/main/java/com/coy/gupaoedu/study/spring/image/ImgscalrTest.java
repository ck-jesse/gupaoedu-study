package com.coy.gupaoedu.study.spring.image;

import com.ck.platform.common.util.RandomUtil;
import org.imgscalr.Scalr;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author chenck
 * @date 2023/10/13 18:29
 */
public class ImgscalrTest extends BaseTest {

    public static final String path = "E:/tmp/img/avatar.jpg";
    public static final String outPath = "E:/tmp/img/output1/";


    /**
     * 缩放
     */
    @Test
    public void resizeTest() throws IOException {
        // 源图片
        BufferedImage img = ImageIO.read(new File(path));

        int targetWidth = 150;
        int targetHeight = 150;

        // 模拟：基于一张图片生成多张图片
        // 生成10张，耗时0.8s左右
        // 生成15张，耗时3.3s左右
        for (int i = 0; i < 20; i++) {
            targetWidth = targetWidth + i * 50;
            targetHeight = targetHeight + i * 50;
            // 新图片，缩放到指定长宽
            BufferedImage newImage = Scalr.resize(img, Scalr.Method.SPEED, targetWidth, targetHeight);
            writeImg(newImage, outPath);
        }

        img.flush();

    }


    /**
     * 旋转
     */
    @Test
    public void rotateTest() throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        BufferedImage newImage = Scalr.rotate(img, Scalr.Rotation.CW_90);

        writeImg(newImage, outPath);

        img.flush();
    }

    /**
     * 裁剪
     */
    @Test
    public void cropTest() throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        BufferedImage newImage = Scalr.crop(img, 50, 50, 250, 245);

        writeImg(newImage, outPath);

        img.flush();
    }

    /**
     * 输出图片
     */
    public void writeImg(BufferedImage newImage, String outPath) throws IOException {
        String fileName = "avatar" + RandomUtil.genRandomNumber(3) + ".jpg";
        File thumbnailFile = new File(outPath + fileName);

        boolean write = ImageIO.write(newImage, "jpg", thumbnailFile);
        System.out.println("输出图片=" + write + ", 文件名=" + fileName + ", 线程名=" + Thread.currentThread().getName());

        newImage.flush();
    }
}
