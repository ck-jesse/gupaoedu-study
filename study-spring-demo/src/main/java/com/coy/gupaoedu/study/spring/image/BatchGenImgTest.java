package com.coy.gupaoedu.study.spring.image;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.imgscalr.Scalr;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 批量图片处理Test
 *
 * @author chenck
 * @date 2023/10/15 16:28
 */
public class BatchGenImgTest extends BaseTest {


    /**
     * 23张图片，用同样的图片文件，来测试 Thumbnailator 和 imgscalr 的效果
     */
    File file = new File("F:\\图片\\redis");

    /**
     * 对比数据
     * 条件：相同的23张图片
     * 生成时间：
     *     Thumbnails 生成时间2.48s
     *     imgscalr 生成时间1.111s
     * 图片大小：
     *     生成的图片中，部分图片的大小 Thumbnails 比 imgscalr 要大
     * 图片质量：
     *     生成的图片中，部分图片的质量 Thumbnails 比 imgscalr 要好
     *
     * 小结：
     * 如果从性能角度出发，建议使用 imgscalr，至于质量可以调优
     * 如果从使用便捷角度出发，建议使用 Thumbnails
     */

    /**
     * Thumbnailator
     * <p>
     * 测试数据：23张图片，生成时间2.48s
     */
    @Test
    public void thumbnails() throws IOException {
        Thumbnails.fromFiles(Arrays.asList(file.listFiles()))
                .scale(1)
                .toFiles(new File("E:/tmp/img/output/"), Rename.SUFFIX_HYPHEN_THUMBNAIL);
    }


    /**
     * imgscalr
     * <p>
     * 测试数据：23张图片，生成时间1.111s，图片的大小是
     */
    @Test
    public void imgscalr() throws IOException {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            BufferedImage img = ImageIO.read(files[i]);
            BufferedImage newImage = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, img.getWidth(), img.getHeight());

            File thumbnailFile = new File("E:/tmp/img/output1/" + files[i].getName());
            ImageIO.write(newImage, "jpg", thumbnailFile);

            newImage.flush();
            img.flush();
        }
    }
}
