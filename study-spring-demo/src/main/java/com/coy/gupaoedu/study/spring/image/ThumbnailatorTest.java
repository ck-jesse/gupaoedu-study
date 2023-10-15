package com.coy.gupaoedu.study.spring.image;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * @author chenck
 * @date 2023/10/13 18:29
 */
public class ThumbnailatorTest extends BaseTest {

    public static final String path = "E:/tmp/img/avatar.jpg";
    public static final String outPath = "E:/tmp/img/output/";

    /**
     * 修改尺寸
     */
    @Test
    public void size() throws IOException {
        // size 会默认保证原始图片比例，因为受到原始图片宽高比例的限制，即便设置了宽高可能也不会引起图片的尺寸（像素）变化
        Thumbnails.of(path)
                .size(100, 100)
                .outputQuality(1)// 设置输出质量，支持小数，数字越小质量越差。
                .toFile(outPath + "o1.jpg");

        // forceSize 强制保证宽高，可能会导致图片拉伸
        Thumbnails.of(path)
                .forceSize(100, 100)
                .toFile(outPath + "o2.jpg");
    }

    /**
     * 缩放
     */
    @Test
    public void scale() throws IOException {
        // scale 是设置图片宽高比例，不大于1的数参数
        Thumbnails.of(path)
                .scale(0.8)
                .toFile(outPath + "o1.jpg");
    }


    /**
     * 旋转
     */
    @Test
    public void rotate() throws IOException {
        Thumbnails.of(path)
                .rotate(30) // 旋转30度
                .size(300, 300)// 此处size必须设置
                .toFile(outPath + "o1.jpg");
    }

    /**
     * 图片水印
     */
    @Test
    public void watermark() throws IOException {
        Thumbnails.of(path)
                // 水印放到右下角
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("input/watermark.png")), 0.5f)
                .scale(1.74)
                .toFile(outPath + "o1.jpg");

    }
}
