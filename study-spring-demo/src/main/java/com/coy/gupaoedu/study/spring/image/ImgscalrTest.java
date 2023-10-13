package com.coy.gupaoedu.study.spring.image;

import com.ck.platform.common.util.RandomUtil;
import org.imgscalr.Scalr;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author chenck
 * @date 2023/10/13 18:29
 */
public class ImgscalrTest {

    public static final String path = "E:/tmp/img/avatar.jpg";
    public static final String outPath = "E:/tmp/img/avatar" + RandomUtil.genRandomNumber(3) + ".jpg";

    /**
     * 缩放
     */
    @Test
    public void resizeTest() throws IOException {
        int targetWidth = 150;
        int targetHeight = 150;

        // 源图片
        BufferedImage img = ImageIO.read(new File(path));

        // 新图片，缩放到指定长宽
        BufferedImage newImage = Scalr.resize(img, Scalr.Method.SPEED, targetWidth, targetHeight);

        // 新图片，按比例缩放为长, 宽都不超过150的图片
        // BufferedImage thumbnail = Scalr.resize(img, 150);

        writeImg(img, newImage, outPath);

    }

    /**
     * 旋转
     */
    @Test
    public void rotateTest() throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        BufferedImage newImage = Scalr.rotate(img, Scalr.Rotation.CW_90);

        writeImg(img, newImage, outPath);
    }

    /**
     * 裁剪
     */
    @Test
    public void cropTest() throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        BufferedImage newImage = Scalr.crop(img, 50, 50, 400, 500);

        writeImg(img, newImage, outPath);
    }

    /**
     * 输出图片
     */
    public void writeImg(BufferedImage img, BufferedImage newImage, String outPath) throws IOException {
        File thumbnailFile = new File(outPath);

        boolean write = ImageIO.write(newImage, "jpg", thumbnailFile);
        System.out.println(write);

        img.flush();
        newImage.flush();
    }
}
