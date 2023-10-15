package com.coy.gupaoedu.study.spring.image;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.URLUtil;
import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.element.TextElement;
import com.freewayso.image.combiner.enums.BaseLine;
import com.freewayso.image.combiner.enums.Direction;
import com.freewayso.image.combiner.enums.GradientDirection;
import com.freewayso.image.combiner.enums.LineAlign;
import com.freewayso.image.combiner.enums.OutputFormat;
import com.freewayso.image.combiner.enums.ZoomMode;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author chenck
 * @date 2023/10/15 17:09
 */
public class ImageCombinerTest extends BaseTest {

    /**
     * 简单测试
     *
     * @throws Exception
     */
    @Test
    public void simpleTest() throws Exception {

        //合成器和背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);

        //加图片元素（居中绘制，圆角，半透明）
        combiner.addImageElement("https://img.thebeastshop.com/image/20201130115835493501.png?x-oss-process=image/resize,m_pad,w_750,h_783/auto-orient,1/quality,q_90/format,jpg", 0, 300)
                .setCenter(true);

        //加文本元素
        combiner.addTextElement("周末大放送", 60, 100, 960)
                .setColor(Color.red);

        //合成图片
        combiner.combine();

        //保存文件（或getCombinedImageStream()并上传图片服务器）
        combiner.save("d://simpleTest.jpg");

        //或者获取流（并上传oss等）
        //InputStream is = combiner.getCombinedImageStream();
        //String url = ossUtil.upload(is);
    }

    /**
     * 完整功能测试
     * 执行时间：大概在1.5s
     * 小结：功能较为符合需求，但当请求量上来后，性能可能会有较大的瓶颈
     *
     * @throws Exception
     */
//    @Test
    public void FullTest(String filepath) throws Exception {
//        String bgImageUrl = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";                       //背景图（测试url形式）
//        String qrCodeUrl = "http://imgtest.thebeastshop.com/file/combine_image/qrcodef3d132b46b474fe7a9cc6e76a511dfd5.jpg";     //二维码
//        String productImageUrl = "https://img.thebeastshop.com/combine_image/funny_topic/resource/product_3x4.png";             //商品图
//        BufferedImage waterMark = ImageIO.read(new URL("https://img.thebeastshop.com/combine_image/funny_topic/resource/water_mark.png"));  //水印图（测试BufferedImage形式）
//        BufferedImage avatar = ImageIO.read(new URL("https://img.thebeastshop.com/member/privilege/level-icon/level-three.jpg"));           //头像
        String bgImageUrl = "D:/360安全浏览器下载/bg_3x4.png";                       //背景图（测试url形式）
        String qrCodeUrl = "D:/360安全浏览器下载/qrcodef3d132b46b474fe7a9cc6e76a511dfd5.jpg";     //二维码
        String productImageUrl = "D:/360安全浏览器下载/product_3x4.png";             //商品图
        BufferedImage waterMark = ImageIO.read(new File("D:/360安全浏览器下载/water_mark.png"));  //水印图（测试BufferedImage形式）
        BufferedImage avatar = ImageIO.read(new File("D:/360安全浏览器下载/level-three.jpg"));           //头像
        String title = "# 最爱的家居";                                       //标题文本
        String content = "苏格拉底说：“如果没有那个桌子，可能就没有那个水壶”";  //内容文本

        //合成器和背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
        ImageCombiner combiner = new ImageCombiner(ImageIO.read(new File(bgImageUrl)), OutputFormat.PNG);
        combiner.setBackgroundBlur(30);     //设置背景高斯模糊（毛玻璃效果）
        combiner.setCanvasRoundCorner(100); //设置整图圆角（输出格式必须为PNG）

        //商品图（设置坐标、宽高和缩放模式，若按宽度缩放，则高度按比例自动计算）
        combiner.addImageElement(ImageIO.read(new File(productImageUrl)), 0, 160, 837, 0, ZoomMode.Width)
                .setRoundCorner(46)     //设置圆角
                .setCenter(true);       //居中绘制，会忽略x坐标参数，改为自动计算

        //标题（默认字体为“阿里巴巴普惠体”，也可以自己指定字体名称或Font对象）
        combiner.addTextElement(title, 55, 150, 1400);

        //内容（设置文本自动换行，需要指定最大宽度（超出则换行）、最大行数（超出则丢弃）、行高）
        combiner.addTextElement(content, "微软雅黑", 40, 150, 1480)
                .setAutoBreakLine(837, 2, 60);

        //头像（圆角设置一定的大小，可以把头像变成圆的）
        combiner.addImageElement(avatar, 200, 1200, 130, 130, ZoomMode.WidthHeight)
                .setRoundCorner(200)
                .setBlur(5);       //高斯模糊，毛玻璃效果

        //水印（设置透明度，0.0~1.0）
        combiner.addImageElement(waterMark, 630, 1200)
                .setAlpha(.8f)      //透明度，0.0~1.0
                .setRotate(15);     //旋转，0~360，按中心点旋转

        //二维码（强制按指定宽度、高度缩放）
        combiner.addImageElement(ImageIO.read(new File(qrCodeUrl)), 138, 1707, 186, 186, ZoomMode.WidthHeight);

        //元素对象也可以直接new，然后手动加入待绘制列表
        TextElement textPrice = new TextElement("￥1290", 40, 600, 1400);
        textPrice.setStrikeThrough(true);       //删除线
        combiner.addElement(textPrice);         //加入待绘制集合

        //动态计算位置
        int offsetPrice = textPrice.getX() + textPrice.getWidth() + 10;
        combiner.addTextElement("￥999", 60, offsetPrice, 1400)
                .setColor(Color.red);

        //执行图片合并
        combiner.combine();

        //保存文件
        if (null == filepath) {
            combiner.save("d://fullTest.png");
        } else {
            combiner.save(filepath);
        }

        //或者获取流（并上传oss等）
        //InputStream is = combiner.getCombinedImageStream();
        //String url = ossUtil.upload(is);
    }

    @Test
    public void testBatch() throws Exception {
        for (int i = 0; i < 10; i++) {
            FullTest("d://fullTest" + i + ".png");
        }
    }

    /**
     * 旋转测试
     *
     * @throws Exception
     */
    @Test
    public void rotateTest() throws Exception {
        String bg = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
        ImageCombiner combiner = new ImageCombiner(bg, OutputFormat.JPG);

        combiner.addTextElement("我觉得应该可以正常显示", 80, 300, 300)
                .setCenter(true);
        combiner.addTextElement("我觉得应该可以正常显示", 80, 300, 300).setColor(Color.red)
                .setCenter(true)
                .setRotate(40);


        combiner.addTextElement("测试一下多行文本换行加旋转的动作，不知道是否能正常显示", 80, 300, 600)
                .setStrikeThrough(true)
                .setAutoBreakLine(600, 2, 80);
        combiner.addTextElement("测试一下多行文本换行加旋转的动作，不知道是否能正常显示", 80, 300, 600).setColor(Color.red)
                .setStrikeThrough(true)
                .setAutoBreakLine(600, 2, 80)
                .setRotate(40);

        combiner.addImageElement("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg", 300, 1000)
                .setCenter(true)
                .setRotate(45);

        combiner.combine();
        combiner.save("d://rotateTest.jpg");
    }

    /**
     * 动态计算宽度测试
     *
     * @throws Exception
     */
    @Test
    public void dynamicWidthDemoTest() throws Exception {
        String bg = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
        ImageCombiner combiner = new ImageCombiner(bg, OutputFormat.JPG);

        String str1 = "您出征";
        String str2 = "某城市";     //内容不定，宽度也不定
        String str3 = "，共在前线战斗了";
        String str4 = "365";     //内容不定，宽度也不定
        String str5 = "天！";
        int fontSize = 60;
        int xxxFontSize = 80;

        int offsetX = 20;
        int y = 300;

        TextElement element1 = combiner.addTextElement(str1, fontSize, offsetX, y).setBaseLine(BaseLine.Bottom);
        offsetX += element1.getWidth();

        TextElement element2 = combiner.addTextElement(str2, xxxFontSize, offsetX, y).setBaseLine(BaseLine.Bottom)
                .setColor(Color.red);
        offsetX += element2.getWidth();

        TextElement element3 = combiner.addTextElement(str3, fontSize, offsetX, y).setBaseLine(BaseLine.Bottom);
        offsetX += element3.getWidth();

        TextElement element4 = combiner.addTextElement(str4, xxxFontSize, offsetX, y).setBaseLine(BaseLine.Bottom)
                .setColor(Color.red);
        offsetX += element4.getWidth();

        combiner.addTextElement(str5, fontSize, offsetX, y).setBaseLine(BaseLine.Bottom);

        BufferedImage img = ImageIO.read(new URL("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg"));
        combiner.addImageElement(img, 20, 500);

        combiner.combine();
        combiner.save("d://demo.jpg");
    }

    /**
     * Png透明背景图测试
     *
     * @throws IOException
     */
    @Test
    public void pngTest() throws Exception {

        BufferedImage bgImage = ImageIO.read(new File("d://memberCard.png"));   //背景是圆角透明图
        String content = "2021-12-12 到期";

        //如背景包含透明部分，一定要用OutputFormat.PNG格式，否则合成后透明部分会变黑
        ImageCombiner combiner = new ImageCombiner(bgImage, 1000, 0, ZoomMode.Width, OutputFormat.PNG);

        //内容文本
        combiner.addTextElement(content, 38, 72, 260).setColor(Color.white);

        //合成图片
        combiner.combine();

        //上传oss
        combiner.save("d://pngTest.png");
    }

    /**
     * 矩形（圆形）绘制测试
     *
     * @throws Exception
     */
    @Test
    public void rectangleTest() throws Exception {
        //合成器和背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);

        //加图片元素（居中绘制，圆角，半透明）
        combiner.addImageElement("https://img.thebeastshop.com/image/20201130115835493501.png?x-oss-process=image/resize,m_pad,w_750,h_783/auto-orient,1/quality,q_90/format,jpg", 0, 300)
                .setCenter(true);

        //加文本元素
        combiner.addTextElement("周末大放送", 60, 200, 960)
                .setColor(Color.red)
                .setAlpha(0.2f);

        //加文本元素
        combiner.addTextElement("周末大放送", 60, 200, 560)
                .setColor(Color.red);

        //加入矩形元素
        combiner.addRectangleElement(200, 500, 300, 300)
                .setColor(Color.BLUE);

        //加入矩形元素（圆角）
        combiner.addRectangleElement(300, 700, 300, 300)
                .setColor(Color.YELLOW)
                .setRoundCorner(100)
                .setBorderSize(10)
                .setAlpha(.8f).setGradient(Color.yellow, Color.blue, GradientDirection.LeftRight);

        //加入矩形元素（圆）
        combiner.addRectangleElement(200, 500, 300, 300)
                .setColor(Color.RED)
                .setRoundCorner(300)        //该值大于等于宽高时，就是圆形
                .setAlpha(.8f)
                .setCenter(true);

        //合成图片
        combiner.combine();

        combiner.save("d://rectangleTest.jpg");
    }

    /**
     * 文本自动换行测试
     *
     * @throws Exception
     */
    @Test
    public void breakLineTest() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        Font font = new Font("阿里巴巴普惠体", Font.PLAIN, 62);

        //添加文字，并设置换行参数
        combiner.addTextElement("大江东去，浪淘尽，千古风流人物。故垒西边，人道是：三国周郎赤壁。乱石穿空，惊涛拍岸，卷起千堆雪。江山如画，一时多少豪杰。", font, 100, 0)
                .setColor(Color.red)
                .setCenter(true)
                .setStrikeThrough(true)
                .setAutoBreakLine(630, 8, 100, LineAlign.Right);    //不给LineAlign参数的话，默认左对齐
        //合成图片
        combiner.combine();

        combiner.save("d://breakLineTest.jpg");
    }

    /**
     * 手动指定换行符测试
     *
     * @throws Exception
     */
    @Test
    public void breakLineSplitterTest() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        Font font = new Font("阿里巴巴普惠体", Font.PLAIN, 60);

        //添加文字，并设置换行参数
        combiner.addTextElement("大江东去，浪淘尽，\r\n千古风流人物。\r\n故垒西边，\r\n人道是：三国周郎赤壁。\r\n乱石穿空，惊涛拍岸，卷起千堆雪。\r\n江山如画，\r\n一时多少豪杰。", font, 0, 150)
                .setColor(Color.red)
                //.setCenter(true)
                .setAutoBreakLine("\r\n", 100, LineAlign.Right);     //不给LineAlign参数的话，默认左对齐
        //合成图片
        combiner.combine();

        combiner.save("d://breakLineSplitterTest.jpg");
    }

    /**
     * 文字竖排测试
     */
    @Test
    public void verticalTextTest() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        //添加文字，并设置为自动换行，且行宽设为0
        combiner.addTextElement("通过自动换行功能，实现文字竖排", 50, 200, 100)
                .setAutoBreakLine(0, 20, 60).setDirection(Direction.CenterLeftRight);

        combiner.addTextElement("将文本元素设为自动换行，且行宽设为0即可", 50, 300, 100)
                .setAutoBreakLine(0, 50, 60, LineAlign.Center);
        //合成图片
        combiner.combine();

        combiner.save("d://verticalTextTest.jpg");
    }

    /**
     * 文字加粗、斜体测试
     */
    @Test
    public void boldTextTest() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        //添加文字，并设置为自动换行，且行宽设为0
        combiner.addTextElement("通过自动换行功能，实现文字竖排", Font.BOLD, 50, 200, 100)
                .setAutoBreakLine(0, 20, 60);

        combiner.addTextElement("将文本元素设为自动换行，且行宽设为0即可", Font.ITALIC, 50, 300, 100)
                .setAutoBreakLine(0, 50, 60, LineAlign.Center);
        //合成图片
        combiner.combine();

        combiner.save("d://boldTextTest.jpg");
    }

    /**
     * 测试矩形渐变色
     */
    @Test
    public void GradientTest() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);

        //四向渐变
        combiner.addRectangleElement(100, 100, 300, 300).setRoundCorner(50)
                .setGradient(Color.blue, Color.red, GradientDirection.TopBottom);
        combiner.addRectangleElement(450, 100, 300, 300)
                .setGradient(Color.blue, Color.red, GradientDirection.LeftRight);
        combiner.addRectangleElement(100, 450, 300, 300)
                .setGradient(Color.blue, Color.red, GradientDirection.LeftTopRightBottom);
        combiner.addRectangleElement(450, 450, 300, 300)
                .setGradient(Color.blue, Color.red, GradientDirection.RightTopLeftBottom);

        //渐变拉伸对比
        combiner.addRectangleElement(100, 800, 300, 300)
                .setGradient(Color.yellow, Color.magenta, GradientDirection.TopBottom);
        combiner.addRectangleElement(450, 800, 300, 300)
                .setGradient(Color.yellow, Color.magenta, 0, 100, GradientDirection.TopBottom);
        combiner.addRectangleElement(800, 800, 300, 300)
                .setGradient(Color.yellow, Color.magenta, 100, 0, GradientDirection.TopBottom);

        //渐变+透明度
        combiner.addTextElement("渐变+透明度", 30, 120, 1300);
        combiner.addRectangleElement(100, 1150, 300, 300)
                .setGradient(Color.yellow, Color.magenta, GradientDirection.TopBottom)
                .setAlpha(.6f);

        //合成图片
        combiner.combine();

        combiner.save("d://GradientTest.jpg");
    }

    /**
     * 不带背景图的空白画布，画布高度根据内容动态计算
     *
     * @throws Exception
     */
    @Test
    public void ymxkTest() throws Exception {
        BufferedImage imgTop = ImageIO.read(new File("d://ymxk/top.png"));
        BufferedImage imgStar = ImageIO.read(new File("d://ymxk/star.png"));
        BufferedImage imgGrayStar = ImageIO.read(new File("d://ymxk/star_gray.png"));
        String content = "这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度";

        //用于临时计算高度
        TextElement tempElement = new TextElement(content, 18, 0, 340);
        tempElement.setCenter(true);
        tempElement.setAutoBreakLine(320, 20, 25);
        int contentHeight = tempElement.getHeight();

        //来一个空的背景
        ImageCombiner combiner = new ImageCombiner(343, 400 + contentHeight, OutputFormat.PNG);
        combiner.setCanvasRoundCorner(60);

        //加入各种元素
        combiner.addImageElement(imgTop, 0, 0);
        combiner.addRectangleElement(0, imgTop.getHeight() - 85, imgTop.getWidth(), 85).setGradient(Color.gray, Color.red, GradientDirection.TopBottom).setAlpha(.3f);  //渐变遮罩
        combiner.addTextElement("德军总部：新血脉", 18, 10, 205).setColor(Color.white);
        combiner.addTextElement("Wolfenstein Yound Blood", 10, 10, 220).setColor(Color.white);
        combiner.addTextElement("第一人称射击", 12, 10, 240).setColor(Color.white);
        combiner.addTextElement("9.0", new Font("微软雅黑", Font.BOLD, 22), 290, 210).setColor(Color.white);
        for (int i = 0; i < 5; i++) {
            combiner.addImageElement(i < 4 ? imgStar : imgGrayStar, 280 + i * 12, 220);
        }
        combiner.addImageElement(imgTop, 10, 270, 40, 40, ZoomMode.WidthHeight).setRoundCorner(300);
        combiner.addTextElement("苏轼的早餐", "微软雅黑", 16, 60, 290);
        combiner.addElement(tempElement);

        combiner.combine();
        combiner.save("d://fonttest.jpg");
    }

    /**
     * 计算文字高度
     */
    @Test
    public void testComputeFontHeight() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        combiner.addTextElement("点击画布上方", 60, 0, 0).setLineHeight(200);

        combiner.addRectangleElement(300, 300, 300, 300).setColor(Color.red);
        combiner.combine();
        combiner.save("d://computeHeight.jpg");
    }

    /**
     * 测试绘制方向
     */
    @Test
    public void testDirection() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        //单行文本
        combiner.addTextElement("测试绘制方向1", 60, combiner.getCanvasWidth() / 2, 100).setDirection(Direction.CenterLeftRight);
        combiner.addTextElement("看看右对齐到效果", 60, 1000, 200).setDirection(Direction.RightLeft);
        //多行文本
        combiner.addTextElement("多行文本多行文本多行文本多行文本多行文本多行文本", 60, 1000, 300)
                .setAutoBreakLine(600, 5, LineAlign.Right)
                .setDirection(Direction.RightLeft);
        //图片
        combiner.addImageElement("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg", 1000, 600).setDirection(Direction.RightLeft);
        //矩形（右到左）
        combiner.addRectangleElement(1000, 900, 200, 200).setColor(100, 32, 200).setDirection(Direction.RightLeft);
        //矩形（中间到两边）
        combiner.addRectangleElement(1000, 1100, 200, 200).setColor(200, 132, 20).setDirection(Direction.CenterLeftRight);

        combiner.combine();
        combiner.save("d://testDirection.jpg");
    }

    /**
     * 自动适应最大宽度（超出则自动缩小字号，以适应之）
     *
     * @throws Exception
     */
    @Test
    public void testAutoFixWidth() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        combiner.addTextElement("测试自适应最大宽度，测试自适应最大宽度，测试自适应最大宽度", 60, 0, 200)
                .setAutoFitWidth(600, 40);

        combiner.combine();
        combiner.save("d://testAutoFixWidth.jpg");
    }

    /**
     * 绘制一个时间轴
     */
    @Test
    public void testDrawTimeLine() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        int startX = 200;
        int startY = 200;
        int stepY = 200;
        int radius = 20;
        int lineWidth = 6;

        for (int i = 0; i < 5; i++) {
            int currentY = startY + stepY * (i + 1);
            //竖线
            if (i < 4) {
                combiner.addRectangleElement(startX, startY + stepY * (i + 1) + radius / 2, lineWidth, stepY)
                        .setColor(Color.orange)
                        .setDirection(Direction.CenterLeftRight);   //用中间到两边，不用考虑限宽问题了（不用再精细的计算x坐标）
            }
            //圆圈
            combiner.addRectangleElement(startX, currentY, radius * 2, radius * 2)
                    .setRoundCorner(radius * 2)
                    .setColor(Color.green)
                    .setDirection(Direction.CenterLeftRight);
            //文本
            TextElement text = combiner.addTextElement("公司于2030年成功在火星设立了办事处", 30, startX + radius + 50, currentY);
            combiner.addTextElement("2022-02-16 18:55:26", 26, text.getX() + text.getWidth(), currentY + text.getHeight())
                    .setDirection(Direction.RightLeft)  //x坐标为上一行文本x+文本宽度，右到左绘制，达到跟随效果
                    .setColor(Color.lightGray);
        }

        combiner.combine();
        combiner.save("d://testDrawTimeLine.jpg");
    }

    /**
     * 测试字间距
     */
    @Test
    public void testSpacing() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        combiner.addTextElement("测试字间距，hello my world", 40, 100, 200).setSpace(.5f).setStrikeThrough(true);

        combiner.combine();
        combiner.save("d://testSpacing.jpg");
    }

    /**
     * 测试水印平铺
     */
    @Test
    public void testRepeat() throws Exception {
        ImageCombiner combiner = new ImageCombiner("file:///d:/pure.jpg", OutputFormat.JPG);        //采用file:///协议加载图片
        String productImageUrl = "https://img.thebeastshop.com/image/20200807160107681379.JPG?x-oss-process=image/resize,m_pad,w_374,h_390/auto-orient,1/quality,q_90/format,jpg";             //商品图

        combiner.addRectangleElement(200, 200, 300, 300)
                .setRoundCorner(60)
                .setColor(Color.lightGray)
                .setAlpha(.6f)
                .setRepeat(true, 50);

        combiner.addImageElement(productImageUrl, 380, 230, 100, 0, ZoomMode.Width)
                .setAlpha(.4f)
                .setRotate(-10)
                .setRoundCorner(30)
                .setRepeat(true, 100, 30);

        combiner.addTextElement("水印平铺效果", 40, 400, 200)
                .setSpace(.5f)
                .setRotate(30)
                .setRepeat(true, 80, 150);

        combiner.combine();
        combiner.save("d://testRepeat.jpg");
    }

    @Test
    public void testFeature1() throws Exception {

        ImageCombiner combiner = new ImageCombiner("file:///d:/pure.jpg", OutputFormat.JPG);        //采用file:///协议加载图片
        String productImageUrl = "https://img.thebeastshop.com/image/20200807160107681379.JPG?x-oss-process=image/resize,m_pad,w_374,h_390/auto-orient,1/quality,q_90/format,jpg";             //商品图

        int fontSize = 38;

        int x1 = 50;
        int x2 = 400;
        int x3 = 750;

        int y1 = 80;
        int y2 = 300;
        int y3 = 600;
        int y4 = 900;
        int y5 = 1200;
        int y6 = 1450;
        int y7 = 1700;

        //字体、字号、颜色、删除线
        combiner.addTextElement("基本：", fontSize, x1, y1);
        combiner.addTextElement("字体、字号、颜色、删除线", "微软雅黑", fontSize, x2 - 150, y1)
                .setColor(Color.red)        //用r,g,b三个参数也可以
                .setStrikeThrough(true);


        //旋转
        combiner.addTextElement("旋转：", fontSize, x1, y2);
        combiner.addTextElement("文字旋转20度", fontSize, x2 - 150, y2)
                .setRotate(20);
        combiner.addImageElement(productImageUrl, x3 - 180, y2, 200, 0, ZoomMode.Width)
                .setRotate(20);
        combiner.addImageElement(productImageUrl, x3 + 80, y2, 200, 0, ZoomMode.Width)
                .setRotate(-20);

        //圆角
        combiner.addTextElement("圆角：", fontSize, x1, y3);
        combiner.addImageElement(productImageUrl, x2 - 150, y3, 200, 0, ZoomMode.Width).setRoundCorner(50);
        combiner.addRectangleElement(x3 - 200, y3, 200, 200).setRoundCorner(50).setColor(Color.lightGray);
        combiner.addRectangleElement(x3 + 100, y3, 200, 200).setRoundCorner(200).setColor(Color.orange);

        //自动换行
        combiner.addTextElement("自动换行，超过指定宽度自动换到下一行，超过指定行数则丢弃", fontSize, x1, y4).setAutoBreakLine(320, 4);
        combiner.addTextElement("自动换行，超过指定宽度自动换到下一行，超过指定行数则丢弃", fontSize, x2, y4).setAutoBreakLine(320, 4, LineAlign.Center);
        combiner.addTextElement("自动换行，超过指定宽度自动换到下一行，超过指定行数则丢弃", fontSize, x3, y4).setAutoBreakLine(320, 4, LineAlign.Right);

        //绘制方向
        //combiner.addTextElement("绘制方向：", fontSize, x1, y5);
        combiner.addTextElement("从左到右", fontSize, x1 + 100, y5);
        combiner.addImageElement(productImageUrl, x2 + 100, y5, 200, 0, ZoomMode.Width);
        combiner.addRectangleElement(x3 + 100, y5, 200, 200).setColor(Color.lightGray);

        combiner.addTextElement("从右到左", fontSize, x1 + 100, y6).setDirection(Direction.RightLeft);
        combiner.addImageElement(productImageUrl, x2 + 100, y6, 200, 0, ZoomMode.Width).setDirection(Direction.RightLeft);
        combiner.addRectangleElement(x3 + 100, y6, 200, 200).setColor(Color.lightGray).setDirection(Direction.RightLeft);

        combiner.addTextElement("中间到两边", fontSize, x1 + 100, y7).setDirection(Direction.CenterLeftRight);
        combiner.addImageElement(productImageUrl, x2 + 100, y7, 200, 0, ZoomMode.Width).setDirection(Direction.CenterLeftRight);
        combiner.addRectangleElement(x3 + 100, y7, 200, 200).setColor(Color.lightGray).setDirection(Direction.CenterLeftRight);

        //参考线
        combiner.addRectangleElement(x1 + 98, y5 - 50, 3, 800).setColor(Color.red);
        combiner.addRectangleElement(x2 + 98, y5 - 50, 3, 800).setColor(Color.red);
        combiner.addRectangleElement(x3 + 98, y5 - 50, 3, 800).setColor(Color.red);

        combiner.combine();
        combiner.save("d://testFeatures1.jpg");
    }

    @Test
    public void testFeature2() throws Exception {

        ImageCombiner combiner = new ImageCombiner("file:///d:/pure.jpg", OutputFormat.JPG);        //采用file:///协议加载图片
        String productImageUrl = "https://img.thebeastshop.com/image/20200807160107681379.JPG?x-oss-process=image/resize,m_pad,w_374,h_390/auto-orient,1/quality,q_90/format,jpg";             //商品图

        int fontSize = 38;

        int x1 = 50;
        int x2 = 400;
        int x3 = 750;

        int y1 = 80;
        int y2 = 200;
        int y3 = 500;
        int y4 = 800;
        int y5 = 1100;
        int y6 = 1400;
        int y7 = 1600;

        //居中绘制
        combiner.addTextElement("居中绘制，忽略x坐标，改为自动计算", fontSize, x1, y1).setCenter(true);
        combiner.addImageElement(productImageUrl, x1, y2, 200, 0, ZoomMode.Width).setCenter(true);
        combiner.addRectangleElement(x1, y3, 200, 200).setCenter(true).setGradient(Color.orange, Color.pink, GradientDirection.TopBottom);

        //高斯模糊
        combiner.addTextElement("高斯模糊：", fontSize, x1, y4);
        combiner.addImageElement(productImageUrl, x2, y4, 200, 0, ZoomMode.Width).setBlur(10);

        //透明度
        combiner.addTextElement("透明度：", fontSize, x1, y5);
        combiner.addRectangleElement(x2 + 50, y5 + 40, 400, 100).setColor(Color.orange);
        combiner.addTextElement("透明度0.5", 60, x2, y5).setAlpha(.5f);
        combiner.addImageElement(productImageUrl, x3, y5, 200, 0, ZoomMode.Width).setAlpha(.5f);

        //字间距
        combiner.addTextElement("字间距：", fontSize, x1, y6);
        combiner.addTextElement("加大文字间距看起来更舒适", fontSize, x2, y6).setSpace(.5f);

        //行高
        combiner.addTextElement("行高：", fontSize, x1, y7);
        combiner.addTextElement("行高主要用于设置文字上下补白，可以和Sketch设计稿保持一致，单行文本或自动换行文本都可以设置", fontSize, x2, y7)
                .setAutoBreakLine(600, 3)        //setAutoBreakLine重载方法也可以直接传lineHeight参数
                .setLineHeight(80);

        combiner.combine();
        combiner.save("d://testFeature2.jpg");
    }

    /**
     * 测试保存文件压缩质量
     */
    @Test
    public void testQuality() throws Exception {
        ImageCombiner combiner = new ImageCombiner("file:///D:/testTuZi.jpg", OutputFormat.PNG);
        combiner.addImageElement("file:///D:/testTuZi.jpg", 0, 0);
        combiner.setQuality(0.5f);
        combiner.combine();
        combiner.save("d://testAutoFixWidth4.png");
    }

    @Test
    public void testTrans() throws Exception {
        ImageCombiner combiner = new ImageCombiner(300, 300, OutputFormat.PNG);
        combiner.addTextElement("这是一个透明背景图", 20, 100, 100);
        combiner.addImageElement("file:///D:/tuzi.png", 30, 30, 50, 50, ZoomMode.WidthHeight);
        combiner.combine();
        combiner.save("d:/透明图.png");
    }

    /**
     * 测试文本参考基线
     */
    @Test
    public void testBaseLine() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);
        combiner.addRectangleElement(100, 200, 1000, 1).setColor(Color.red);
        combiner.addRectangleElement(100, 500, 1000, 1).setColor(Color.red);

        combiner.addTextElement("Top", 60, 150, 200).setBaseLine(BaseLine.Top);         //不设置默认Top
        combiner.addTextElement("Middle", 60, 320, 200).setBaseLine(BaseLine.Middle);
        combiner.addTextElement("Bottom", 60, 590, 200).setBaseLine(BaseLine.Bottom);
        combiner.addTextElement("Base", 60, 860, 200).setBaseLine(BaseLine.Base);


        combiner.combine();
        combiner.save("d://testBaseLine.jpg");
    }

    /**
     * 测试从项目路径加载字体文件
     *
     * @throws Exception
     */
    @Test
    public void testLoadFont() throws Exception {
        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);

        //字体文件采用Launcher.class.getResourceAsStream加载，fontNameOrPath参数包含.认为是字体文件，否则认为是字体名
        combiner.addTextElement("微软雅黑", "/yahei.ttc", 60, 150, 300);
        combiner.addTextElement("微软雅黑", "/yahei.ttc", 60, 150, 400);

        combiner.combine();
        combiner.save("d://testLoadFont.jpg");
    }

    /**
     * 显示所有可用字体
     */
    @Test
    public void showFonts() throws InterruptedException {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = e.getAvailableFontFamilyNames();
        for (int i = 0; i < fontName.length; i++) {
            System.out.println(fontName[i]);
        }
    }

    /**
     * Png 文字水印九宫格位置水印测试
     *
     * @throws Exception
     */
    @Test
    public void pngTextWatermark() throws Exception {
        //合成器和背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
        ImageCombiner combiner = new ImageCombiner(ImgUtil.read(URLUtil.url("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png")), OutputFormat.JPG);

        // 设置字体和大小
        Font font = new Font("阿里巴巴普惠体", Font.PLAIN, 20);

        // 水印与图片边缘的间距
        int padding = 10;

        // 左上
        TextElement topLeft = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        topLeft.setAutoBreakLine("/n", 30).setX(padding).setAlpha(.8f);
        combiner.addElement(topLeft);

        // 上中
        TextElement topCenter = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        topCenter.setAutoBreakLine("/n", 30, LineAlign.Center)
                .setCenter(true)
                .setAlpha(.8f);
        combiner.addElement(topCenter);

        // 右上
        TextElement topRight = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        topRight.setAutoBreakLine("/n", 30, LineAlign.Right)
                .setX(combiner.getCanvasWidth() - padding)
                .setAlpha(.8f)
                .setDirection(Direction.RightLeft);
        combiner.addElement(topRight);

        // 左中
        TextElement centerLeft = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        centerLeft.setAutoBreakLine("/n", 30)
                .setX(padding)
                .setY((combiner.getCanvasHeight() - centerLeft.getHeight()) / 2)
                .setAlpha(.8f);
        combiner.addElement(centerLeft);

        // 居中
        TextElement center = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        center.setAutoBreakLine("/n", 30, LineAlign.Center)
                .setCenter(true)
                .setY((combiner.getCanvasHeight() - centerLeft.getHeight()) / 2)
                .setAlpha(.8f);
        combiner.addElement(center);

        // 右中
        TextElement centerRight = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        centerRight.setAutoBreakLine("/n", 30, LineAlign.Right)
                .setX(combiner.getCanvasWidth() - padding)
                .setY((combiner.getCanvasHeight() - centerLeft.getHeight()) / 2)
                .setAlpha(.8f)
                .setDirection(Direction.RightLeft);
        combiner.addElement(centerRight);

        // 左下
        TextElement bottomLeft = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        bottomLeft.setAutoBreakLine("/n", 30)
                .setX(padding)
                .setY(combiner.getCanvasHeight() - bottomLeft.getHeight())
                .setAlpha(.8f);
        combiner.addElement(bottomLeft);

        // 下中
        TextElement bottomCenter = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        bottomCenter.setAutoBreakLine("/n", 30, LineAlign.Center)
                .setCenter(true)
                .setY(combiner.getCanvasHeight() - bottomCenter.getHeight())
                .setAlpha(.8f);
        combiner.addElement(bottomCenter);

        // 右下
        TextElement bottomRight = new TextElement("第一行文字/n第二行文字/n第三行文字/n" + DateUtil.now(), font, 0, 0);
        bottomRight.setAutoBreakLine("/n", 30, LineAlign.Right)
                .setX(combiner.getCanvasWidth() - padding)
                .setY(combiner.getCanvasHeight() - bottomRight.getHeight())
                .setAlpha(.8f)
                .setDirection(Direction.RightLeft);
        combiner.addElement(bottomRight);

        //合成图片
        combiner.combine();
        //上传oss
        combiner.save("d://pngTextWatermark.png");
    }


}
