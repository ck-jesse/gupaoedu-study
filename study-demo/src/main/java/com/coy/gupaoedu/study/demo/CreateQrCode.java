package com.coy.gupaoedu.study.demo;

import com.swetake.util.Qrcode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.List;

public class CreateQrCode {
    private static final Logger logger = LoggerFactory.getLogger(CreateQrCode.class);

    /**
     * createQRCode:二维码创建及保存
     *
     * @param content       二维码内容
     * @param qrIdOrMchName 二维码id或商户名称
     * @param qrLogoImg     二维码路径/文件名
     * @return
     * @throws Exception
     * @author chenling
     */
    public static BufferedImage createQRCode(String content, String qrIdOrMchName, BufferedImage qrLogoImg, String fontPath) throws Exception {
        int width = 638;
        int height = width + 20;  //20为写二维码id或商户名称的空间
        int logowidth = 85;
        Qrcode qrcodeHandler = new Qrcode();
        qrcodeHandler.setQrcodeErrorCorrect('M');
        qrcodeHandler.setQrcodeEncodeMode('B');
        qrcodeHandler.setQrcodeVersion(7);
        byte[] contentBytes = content.getBytes("utf-8");

        // 构造一个BufferedImage对象 设置宽、高
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = bufImg.createGraphics();
        setBackround(width, height, gs);

        // 设定图像颜色 > BLACK
        gs.setColor(Color.BLACK);
        // 设置偏移量 不设置可能导致解析出错
        int pixoff = 2;
        // 输出内容 > 二维码
        if (contentBytes.length > 0) {
            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
            int scale = width / (codeOut.length + pixoff * 2);
            for (int i = 0; i < codeOut.length; i++) {
                for (int j = 0; j < codeOut.length; j++) {
                    if (codeOut[j][i]) {
                        gs.fillRect(j * scale + pixoff * scale, i * scale
                                + pixoff * scale, scale, scale);
                    }
                }
            }
        } else {
            throw new Exception("二维码内容为空 QRCode content bytes length > 0");
        }

        if (qrLogoImg != null) {//生成二维码logo
            int position = (width - logowidth) / 2;
            int radius = (int) (qrLogoImg.getWidth() > qrLogoImg.getHeight() ? qrLogoImg.getWidth() * 0.2 : qrLogoImg.getHeight() * 0.2);
            BufferedImage radiusImg = setClip(qrLogoImg, radius);

            gs.setColor(Color.WHITE);
            gs.fillRoundRect(position - 5, position - 5, logowidth + 10, logowidth + 10, 20, 20);
            gs.setColor(Color.GRAY);
            gs.drawRoundRect(position - 2, position - 2, logowidth + 4, logowidth + 4, 20, 20);
            gs.drawImage(radiusImg, position, position, logowidth, logowidth, null);
        }

        //写二维码id或商户名称
        if (StringUtils.isNotEmpty(qrIdOrMchName)) {
            gs.setColor(Color.BLACK);
            Font font = getDefultFont(28, fontPath);
            gs.setFont(font);
            drawMchNameString(gs, qrIdOrMchName,
                    width, height + 50, 0.75);
        }
        gs.dispose();
        bufImg.flush();
        return bufImg;

    }

    /**
     * createHDQRCode:高清二维码创建
     *
     * @param content       二维码内容
     * @param qrIdOrMchName 二维码id或商户名称
     * @param img           二维码logo路径
     * @param fontPath      字体文件路径
     * @return
     * @throws Exception
     * @author wuzb
     */
    public static BufferedImage createHDQRCode(String content, String qrIdOrMchName, BufferedImage img, String fontPath) throws Exception {
        int width = 1024;
        int height = width + 10;  //10为写二维码id或商户名称的空间
        int logowidth = 136;
        Qrcode qrcodeHandler = new Qrcode();
        qrcodeHandler.setQrcodeErrorCorrect('M');
        qrcodeHandler.setQrcodeEncodeMode('B');
        qrcodeHandler.setQrcodeVersion(7);
        byte[] contentBytes = content.getBytes("utf-8");

        long startTime = System.currentTimeMillis();
        logger.info("开始创建二维码");
        // 构造一个BufferedImage对象 设置宽、高
        BufferedImage bufImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = bufImg.createGraphics();
        setBackround(width, height, gs);


        // 设定图像颜色 > BLACK
        gs.setColor(Color.BLACK);
        // 设置偏移量 不设置可能导致解析出错
        int pixoff = 2;
        // 输出内容 > 二维码
        if (contentBytes.length > 0) {
            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
            int scale = width / (codeOut.length + pixoff * 2); //scale 为每个正方框的宽高，此处可能会除不尽，导致二维码不居中
            int off = (width - codeOut.length * scale) / 2; //实际偏离左边和上边的值，因scale可能出不尽，所以要计算此值

            for (int i = 0; i < codeOut.length; i++) {
                for (int j = 0; j < codeOut.length; j++) {
                    if (codeOut[j][i]) {
                        gs.fillRect(off + j * scale, off + i * scale
                                , scale, scale);
                    }
                }
            }
        } else {
            throw new Exception("二维码内容为空 QRCode content bytes length > 0");
        }

        if (img != null) {//生成二维码logo
            int position = (width - logowidth) / 2;
            int radius = (int) (img.getWidth() > img.getHeight() ? img.getWidth() * 0.2 : img.getHeight() * 0.2);
            BufferedImage radiusImg = setClip(img, radius);
            //ImageIO.write(radiusImg, "png", new File("D:\\radius44.png"));

            gs.setColor(Color.WHITE);
            gs.fillRoundRect(position - 8, position - 8, logowidth + 16, logowidth + 16, 32, 32);
            gs.setColor(Color.GRAY);
            Stroke stroke = gs.getStroke();
            gs.setStroke(new BasicStroke(3f));
            gs.drawRoundRect(position - 3, position - 3, logowidth + 6, logowidth + 6, 32, 32);
            gs.setStroke(stroke);
            gs.drawImage(radiusImg, position, position, logowidth, logowidth, null);
        }

        //写二维码id或商户名称
        if (StringUtils.isNotEmpty(qrIdOrMchName)) {
            gs.setColor(Color.BLACK);
//            Font font = getDefultFont(40, fontPath);
//            gs.setFont(font);
            drawMchNameString(gs, qrIdOrMchName,
                    width, height + 80, 0.75);
        }
        logger.info("二维码创建成功，用时：{}ms", System.currentTimeMillis() - startTime);
        gs.dispose();
        bufImg.flush();
        return bufImg;

    }

    /**
     * 图片切圆角
     *
     * @param srcImage
     * @param radius
     * @return
     */
    private static BufferedImage setClip(BufferedImage srcImage, int radius) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setComposite(AlphaComposite.Clear);
        gs.fill(new Rectangle(image.getWidth(), image.getHeight()));
        gs.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }

    /**
     * @param content       二维码内容 一般是某个链接
     * @param qrIdOrMchName 二维码id或商户名称
     * @param qrLogoImg     二维码logo
     * @param rgbHex        16进制的rgb颜色值
     * @param qrMchImg      商户logo
     * @param payTypeMap    支持的支付类型，key是名称，value是logo路径
     * @param fontPath      字体
     * @return
     * @throws Exception
     * @author zengjia
     */
    public static BufferedImage createIntegrationQRCode(String content, String qrIdOrMchName,
                                                        BufferedImage qrLogoImg, String rgbHex, BufferedImage qrMchImg, Map<String, String> payTypeMap, String fontPath) throws Exception {
        int imgWidth = 308; // 整体图片宽
        int imgHeight = 450; // 整体图片高
        int qrBGCLevelMargin = 0; // 二维码背景色水平边距
        int qrBGCVerticalMargin = 114; // 二维码背景色垂直边距
        int qrScanningTopBorderLevelMargin = 38;
        int qrScanningTopTopMargin = 137;
        int qrScanningWidth = 26;
        int qrScanningHeigth = 5;
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics(); // 创建Graphics2D对象
        //使用高质量压缩
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //开启抗锯齿
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(renderingHints);

        // 填充背景为白色：
        setBackround(imgWidth, imgHeight, g);

        // 二维码背景色
        setqrBackround(rgbHex, imgWidth, qrBGCLevelMargin, qrBGCVerticalMargin, g);

        // 设置二维码扫码框
        setQrScanning(imgWidth, qrScanningTopBorderLevelMargin,
                qrScanningTopTopMargin, qrScanningWidth, qrScanningHeigth, g);

        // 扫一扫文字
        setSysFont(g, fontPath);

        // 生成二维码
        createQrCode(content, qrLogoImg, payTypeMap, imgWidth, g, fontPath);

        // 渠道logo
        if (qrMchImg != null) {
            setMchLog(qrMchImg, imgWidth, qrBGCVerticalMargin, g);
        }

        //写二维码id或商户名称
        if (StringUtils.isNotEmpty(qrIdOrMchName)) {
            g.setColor(Color.WHITE);
//            Font font = getDefultFont(15, fontPath);
//            g.setFont(font);
            drawMchNameString(g, qrIdOrMchName, imgWidth, imgHeight - 25, 0.75);
        }

        // 绘图完成，释放资源：
        g.dispose();
        bi.flush();

        return bi;
    }

    /**
     * 设置渠道logo
     *
     * @param qrMchImg
     * @param imgWidth
     * @param qrBGCVerticalMargin
     * @param g
     * @throws IOException
     */
    private static void setMchLog(BufferedImage qrMchImg, int imgWidth,
                                  int qrBGCVerticalMargin, Graphics2D g) throws IOException {
        double d = qrMchImg.getWidth(null) / (double) qrMchImg.getHeight(null);
        int fkx = (int) (imgWidth / 2 - 25 * d);
        g.drawImage(qrMchImg, fkx, qrBGCVerticalMargin + 280, (int) (50 * d), 50, null);
    }


    /**
     * 生成精简版二维码
     *
     * @param content
     * @param qrLogoImg
     * @param payTypeMap
     * @param imgWidth
     * @param g
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static void createQrCode(String content, BufferedImage qrLogoImg,
                                     Map<String, String> payTypeMap, int imgWidth, Graphics2D g, String fontPath)
            throws Exception {
        Qrcode qrcodeHandler = new Qrcode();
        qrcodeHandler.setQrcodeErrorCorrect('M');
        qrcodeHandler.setQrcodeEncodeMode('B');
        qrcodeHandler.setQrcodeVersion(7);
        byte[] contentBytes = content.getBytes("utf-8");
        g.clearRect(156, 653, 596, 596);
        g.setColor(Color.BLACK);
        int pixoff = 2;
        // 输出内容 > 二维码
        if (contentBytes.length > 0) {
            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
            int scale = 196 / (codeOut.length + pixoff * 2);
            for (int i = 0; i < codeOut.length; i++) {
                for (int j = 0; j < codeOut.length; j++) {
                    if (codeOut[j][i]) {
                        g.fillRect(j * scale + pixoff * scale + 56, i * scale
                                + pixoff * scale + 153, scale, scale);
                    }
                }
            }
        }

        // 支付logo
        setPayLogo(payTypeMap, imgWidth, g, fontPath);

        if (qrLogoImg != null) {//生成二维码logo
            int logowidth = 336;
            int positionX = 138;
            int positionY = 635;

            int radius = (int) (qrLogoImg.getWidth() > qrLogoImg.getHeight() ? qrLogoImg.getWidth() * 0.2 : qrLogoImg.getHeight() * 0.2);
            BufferedImage radiusImg = setClip(qrLogoImg, radius);

            g.setColor(Color.WHITE);
            g.fillRoundRect(positionX - 3, positionY - 3, logowidth + 6, logowidth + 6, 10, 10);
            g.setColor(Color.GRAY);
            g.drawRoundRect(positionX - 2, positionY - 2, logowidth + 3, logowidth + 3, 10, 10);
            g.drawImage(radiusImg, positionX, positionY, logowidth, logowidth, null);
        }
    }

    /**
     * 设置支付logo
     *
     * @param payTypeMap
     * @param imgWidth
     * @param g
     * @throws IOException
     * @throws
     */
    private static void setPayLogo(Map<String, String> payTypeMap,
                                   int imgWidth, Graphics2D g, String fontPath) throws Exception {
        if (1 == 1) {
            return;
        }
        int logoWidth = 35;
        int logoHeight = 35;
        int fontY = 47;
        int offX = 50;
        if (!payTypeMap.isEmpty()) {
            Set<String> set = payTypeMap.keySet();
            if (set.size() > 2) { // 支付logo超过两个只显示logo
                int x = (imgWidth - (set.size() - 1) * offX - logoWidth) / 2;
                for (String key : set) {
                    //Image img = ImageIO.read(new File(payTypeMap.get(key)));
                    InputStream fi = CreateQrCode.class.getResourceAsStream("/QrConfIcon/" + payTypeMap.get(key));
                    Image img = ImageIO.read(fi);
                    g.drawImage(img, x, 25, logoWidth, logoHeight, null);
                    x += offX;
                }
            } else if (set.size() == 2) { // 只有两个显示log加文字
                int x = imgWidth / set.size() - 105;
                for (String key : set) {
                    //Image img = ImageIO.read(new File(payTypeMap.get(key)));
                    InputStream fi = CreateQrCode.class.getResourceAsStream("/QrConfIcon/" + payTypeMap.get(key));
                    Image img = ImageIO.read(fi);
                    g.drawImage(img, x, 25, logoWidth, logoHeight, null);

                    g.setColor(Color.BLACK);
//                    g.setFont(getDefultFont(15, fontPath));
                    g.drawString(key, x + offX, fontY);
                    x += 120;
                }
            } else { // 一个logo
                int x = imgWidth / 2 - 45;
                for (String key : set) {
                    //Image img = ImageIO.read(new File(payTypeMap.get(key)));
                    InputStream fi = CreateQrCode.class.getResourceAsStream("/QrConfIcon/" + payTypeMap.get(key));
                    Image img = ImageIO.read(fi);
                    g.drawImage(img, x, 25, logoWidth, logoHeight, null);

                    g.setColor(Color.BLACK);
//                    g.setFont(getDefultFont(15, fontPath));
                    g.drawString(key, x + offX, fontY);
                }
            }
        }
    }

    /**
     * 扫一扫文字
     *
     * @param g
     * @throws Exception
     */
    private static void setSysFont(Graphics2D g, String fontPath) throws Exception {
        g.setColor(Color.BLACK);
//        g.setFont(getDefultFont(35, fontPath));
//		g.setFont(new Font("微软雅黑", Font.BOLD, 40));
        g.drawString("扫一扫付款", 66, 96);
    }

    /**
     * 设置二维码扫码框
     *
     * @param imgWidth
     * @param qrScanningTopBorderLevelMargin
     * @param qrScanningTopTopMargin
     * @param qrScanningWidth
     * @param qrScanningHeigth
     * @param g
     */
    private static void setQrScanning(int imgWidth,
                                      int qrScanningTopBorderLevelMargin, int qrScanningTopTopMargin,
                                      int qrScanningWidth, int qrScanningHeigth, Graphics2D g) {
        // 左上
        g.fillRect(qrScanningTopBorderLevelMargin, qrScanningTopTopMargin, qrScanningWidth, qrScanningHeigth);
        g.fillRect(qrScanningTopBorderLevelMargin, qrScanningTopTopMargin, qrScanningHeigth, qrScanningWidth);
        // 右上
        g.fillRect(imgWidth - qrScanningTopBorderLevelMargin - qrScanningWidth, qrScanningTopTopMargin, qrScanningWidth, qrScanningHeigth);
        g.fillRect(imgWidth - qrScanningTopBorderLevelMargin - qrScanningHeigth, qrScanningTopTopMargin, qrScanningHeigth, qrScanningWidth);
        // 左下
        g.fillRect(qrScanningTopBorderLevelMargin, 340, qrScanningHeigth, qrScanningWidth);
        g.fillRect(qrScanningTopBorderLevelMargin, 340 + qrScanningWidth - qrScanningHeigth, qrScanningWidth, qrScanningHeigth);
        // 右上
        g.fillRect(imgWidth - qrScanningTopBorderLevelMargin - qrScanningWidth, 340 + qrScanningWidth - qrScanningHeigth, qrScanningWidth, qrScanningHeigth);
        g.fillRect(imgWidth - qrScanningTopBorderLevelMargin - qrScanningHeigth, 340, qrScanningWidth, qrScanningHeigth);
    }

    /**
     * 二维码背景色
     *
     * @param rgbHex
     * @param imgWidth
     * @param qrBGCLevelMargin
     * @param qrBGCVerticalMargin
     * @param g
     */
    private static void setqrBackround(String rgbHex, int imgWidth,
                                       int qrBGCLevelMargin, int qrBGCVerticalMargin, Graphics2D g) {
        if (StringUtils.isEmpty(rgbHex)) {
            rgbHex = "8080FF";
        }
        Color color = new Color(Integer.parseInt(rgbHex, 16));
        g.setColor(color);
        g.fillRect(qrBGCLevelMargin, qrBGCVerticalMargin, imgWidth, 1797);
    }

    private static void setMainAreaBackground(String rgbHex, int imgWidth, int imgHeight,
                                              int x, int y, Graphics2D g) {
        if (StringUtils.isEmpty(rgbHex)) {
            rgbHex = "8080FF";
        }
        Color color = new Color(Integer.parseInt(rgbHex, 16));
        g.setColor(color);
        g.fillRect(x, y, imgWidth, imgHeight);
    }


    private static void setBottomRect(String rgbHex, int imgWidth,
                                      int qrBGCLevelMargin, int qrBGCVerticalMargin, Graphics2D g) {
        if (StringUtils.isEmpty(rgbHex)) {
            rgbHex = "FFFFFF";
        }
        Color color = new Color(Integer.parseInt(rgbHex, 16));
        g.setColor(color);
        g.fillRect(66, 887, imgWidth - 66 * 2, 1040);
    }

    /**
     * 填充整体背景色白色
     *
     * @param imgWidth
     * @param imgHeight
     * @param g
     */
    private static void setBackround(int imgWidth, int imgHeight, Graphics2D g) {
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, imgWidth, imgHeight);
    }

    /**
     * 设置二维码扫码框
     *
     * @param imgWidth
     * @param qrScanningTopBorderLevelMargin
     * @param qrScanningTopTopMargin
     * @param qrScanningWidth
     * @param qrScanningHeigth
     * @param g
     */
    private static void setQrScanning2(int imgWidth,
                                       int qrScanningTopBorderLevelMargin, int qrScanningTopTopMargin,
                                       int qrScanningWidth, int qrScanningHeigth, Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRoundRect(qrScanningTopBorderLevelMargin, qrScanningTopTopMargin, qrScanningWidth, qrScanningHeigth, 30, 30);
    }

    /**
     * 二维码背景色
     *
     * @param rgbHex
     * @param imgWidth
     * @param qrBGCLevelMargin
     * @param qrBGCVerticalMargin
     * @param g
     */
    private static void setqrBackround2(String rgbHex, int imgWidth, int imgHeight,
                                        int qrBGCLevelMargin, int qrBGCVerticalMargin, Graphics2D g) {
        if (StringUtils.isEmpty(rgbHex)) {
            rgbHex = "8080FF";
        }
        Color color = new Color(Integer.parseInt(rgbHex, 16));
        g.setColor(color);
        g.fillRect(qrBGCLevelMargin, qrBGCVerticalMargin, imgWidth, imgHeight - qrBGCVerticalMargin);

        //g.setColor(new Color(Integer.parseInt("007fff",16)));
        g.setColor(Color.WHITE);
        g.fillOval(-20, 0, imgWidth + 40, 300);
    }

    /**
     * 扫一扫文字
     *
     * @param g
     * @throws Exception
     */
    private static void setSysFont2(Graphics2D g, String fontPath) throws Exception {
        g.setColor(Color.BLACK);
//        g.setFont(getDefultFont(35, fontPath));
        g.drawString("扫码付款", 80, 136);
    }

    /**
     * 设置支付logo
     *
     * @param payTypeMap
     * @param imgWidth
     * @param g
     * @throws IOException
     * @throws
     */
    private static void setPayLogo2(Map<String, String> payTypeMap,
                                    int imgWidth, int imgHeight, Graphics2D g, String fontPath) throws Exception {
        int imgY = imgHeight - 70;
        int fontY = imgHeight - 50;
        int logoWidth = 35;
        int logoHeight = 35;
        int offX = 50;
        if (!payTypeMap.isEmpty()) {
            Set<String> set = payTypeMap.keySet();
            if (set.size() > 2) { // 支付logo超过两个只显示logo
                int x = (imgWidth - (set.size() - 1) * offX - logoWidth) / 2;
                for (String key : set) {
                    InputStream fi = CreateQrCode.class.getResourceAsStream("/QrConfIcon/" + payTypeMap.get(key));
                    Image img = ImageIO.read(fi);
                    g.drawImage(img, x, imgY, logoWidth, logoHeight, null);
                    x += offX;
                }
            } else if (set.size() == 2) { // 只有两个显示log加文字
                int x = imgWidth / set.size() - 105;
                for (String key : set) {
                    InputStream fi = CreateQrCode.class.getResourceAsStream("/QrConfIcon/" + payTypeMap.get(key));
                    Image img = ImageIO.read(fi);
                    g.drawImage(img, x, imgY, logoWidth, logoHeight, null);
                    g.setColor(Color.WHITE);
//                    g.setFont(getDefultFont(15, fontPath));
                    g.drawString(key, x + offX, fontY);
                    x += 120;
                }
            } else { // 一个logo
                int x = imgWidth / 2 - 45;
                for (String key : set) {
                    //Image img = ImageIO.read(new File(payTypeMap.get(key)));
                    InputStream fi = CreateQrCode.class.getResourceAsStream("/QrConfIcon/" + payTypeMap.get(key));
                    Image img = ImageIO.read(fi);
                    g.drawImage(img, x, imgY, logoWidth, logoHeight, null);

                    g.setColor(Color.WHITE);
//                    g.setFont(getDefultFont(15, fontPath));
                    g.drawString(key, x + offX, fontY);
                }
            }
        }
    }

    /**
     * 生成精简版二维码
     *
     * @param content
     * @param qrLogoImg
     * @param payTypeMap
     * @param imgWidth
     * @param g
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static void createQrCode2(String content, BufferedImage qrLogoImg,
                                      Map<String, String> payTypeMap, int imgWidth, int imgHeight, Graphics2D g, String fontPath)
            throws Exception {
        Qrcode qrcodeHandler = new Qrcode();
        qrcodeHandler.setQrcodeErrorCorrect('M');
        qrcodeHandler.setQrcodeEncodeMode('B');
        qrcodeHandler.setQrcodeVersion(7);
        // System.out.println(content);
        byte[] contentBytes = content.getBytes("utf-8");
        g.clearRect(56, 153, 196, 196);
        g.setColor(Color.BLACK);
        int pixoff = 2;
        // 输出内容 > 二维码
        if (contentBytes.length > 0) {
            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
            int scale = 196 / (codeOut.length + pixoff * 2);
            for (int i = 0; i < codeOut.length; i++) {
                for (int j = 0; j < codeOut.length; j++) {
                    if (codeOut[j][i]) {
                        g.fillRect(j * scale + pixoff * scale + 56, i * scale
                                + pixoff * scale + 153, scale, scale);
                    }
                }
            }
        }

        // 支付logo
        setPayLogo2(payTypeMap, imgWidth, imgHeight, g, fontPath);

        if (qrLogoImg != null) {//生成二维码logo
            int logowidth = 36;
            int positionX = 138;
            int positionY = 235;

            int radius = (int) (qrLogoImg.getWidth() > qrLogoImg.getHeight() ? qrLogoImg.getWidth() * 0.2 : qrLogoImg.getHeight() * 0.2);
            BufferedImage radiusImg = setClip(qrLogoImg, radius);

            g.setColor(Color.WHITE);
            g.fillRoundRect(positionX - 3, positionY - 3, logowidth + 6, logowidth + 6, 10, 10);
            g.setColor(Color.GRAY);
            g.drawRoundRect(positionX - 2, positionY - 2, logowidth + 3, logowidth + 3, 10, 10);
            g.drawImage(radiusImg, positionX, positionY, logowidth, logowidth, null);
        }
    }

    /**
     * 设置渠道logo
     *
     * @param qrMchImg
     * @param imgWidth
     * @param qrBGCVerticalMargin
     * @param g
     * @throws IOException
     */
    private static void setMchLog2(BufferedImage qrMchImg, int imgWidth,
                                   int qrBGCVerticalMargin, Graphics2D g) throws IOException {
        int logoWidth = qrMchImg.getWidth(null);
        int maxWidth = imgWidth - 80;
        if (logoWidth > maxWidth) {
            logoWidth = maxWidth;
        }

        g.drawImage(qrMchImg, 20, 20, logoWidth, 50, null);
    }

    /**
     * @param content       二维码内容 一般是某个链接
     * @param qrIdOrMchName 二维码id或商户名称
     * @param qrLogoImg     二维码logo
     * @param rgbHex        16进制的rgb颜色值
     * @param qrMchImg      商户logo
     * @param payTypeMap    支持的支付类型，key是名称，value是logo路径
     * @param fontPath      字体
     * @return
     * @throws Exception
     * @author zengjia
     */
    public static BufferedImage createIntegrationQRCode2(String content, String qrIdOrMchName,
                                                         BufferedImage qrLogoImg, String rgbHex, BufferedImage qrMchImg, Map<String, String> payTypeMap, String fontPath) throws Exception {
        int imgWidth = 308; // 整体图片宽
        int imgHeight = 450; // 整体图片高
        int qrBGCLevelMargin = 0; // 二维码背景色水平边距
        int qrBGCVerticalMargin = 220; // 二维码背景色垂直边距
        int qrScanningTopBorderLevelMargin = 45;
        int qrScanningTopTopMargin = 137;
        int qrScanningWidth = imgWidth - qrScanningTopBorderLevelMargin * 2;
        int qrScanningHeigth = 220;
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics(); // 创建Graphics2D对象
        //使用高质量压缩
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //开启抗锯齿
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(renderingHints);

        // 填充背景为白色：
        setBackround(imgWidth, imgHeight, g);

        // 二维码背景色
        setqrBackround2(rgbHex, imgWidth, imgHeight, qrBGCLevelMargin, qrBGCVerticalMargin, g);

        // 设置二维码扫码框
        setQrScanning2(imgWidth, qrScanningTopBorderLevelMargin,
                qrScanningTopTopMargin, qrScanningWidth, qrScanningHeigth, g);

        // 扫一扫文字
        setSysFont2(g, fontPath);

        // 生成二维码
        createQrCode2(content, qrLogoImg, payTypeMap, imgWidth, imgHeight, g, fontPath);

        // 渠道logo
        if (qrMchImg != null) {
            setMchLog2(qrMchImg, imgWidth, qrBGCVerticalMargin, g);
        }

        //写二维码id或商户名称
        if (StringUtils.isNotEmpty(qrIdOrMchName)) {
            g.setColor(Color.WHITE);
//            Font font = getDefultFont(15, fontPath);
//            g.setFont(font);
            drawMchNameString(g, qrIdOrMchName,
                    imgWidth, imgHeight - 35, 0.75);
        }

        // 绘图完成，释放资源：
        g.dispose();
        bi.flush();

        return bi;
    }


    //求字符串长度
    private static int getDrawStringLengthWithInterval(String str, int interval, Graphics2D g) {
        int strLength = 0;
        for (int i = 0; i < str.length(); i++) {
            strLength += g.getFontMetrics().stringWidth(String.valueOf(str.charAt(i))) + interval;
        }
        return strLength - interval;
    }

    //根据限制长度获取字体大小
    private static int getFontSizeByLimit(String drawString, int limitWidth, int fontSize, int maxReduceNum, int interval, Graphics2D g) {
        int loopNum = 0; //循环控制数量
        int drawStrLength = getDrawStringLengthWithInterval(drawString, interval, g);
        while (drawStrLength > limitWidth && loopNum < maxReduceNum && fontSize > 0) {
//            input.setBrandNameFontSize(input.getBrandNameFontSize()-2);
            fontSize -= 2;
            g.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
            drawStrLength = getDrawStringLengthWithInterval(drawString, interval, g);//重新计算长度
            loopNum++;
        }
        return fontSize;
    }

    /**
     * 获取默认字体配置
     *
     * @return
     */
    private static Font getDefultFont(int size, String fontPath) {
        //使用输入流的方式创建字体，会在tomcat部署路径temp文件夹下创建很多临时文件，所有不能使用此方式
        //InputStream is = CreateQrCode.class.getResourceAsStream("/QrConfIcon/vista.ttf");
        try {
            //由于CreateQrCode类在commons.jar中，使用下面方式获取到的路径大致如:  xxxx.jar!/QrConfIcon/vista.ttf  这种路径不能new File
            //String pathString = CreateQrCode.class.getResource("/QrConfIcon/vista.ttf").getFile();
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "/vista.ttf"));
            //Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(Font.PLAIN, size);
            return font;
        } catch (FontFormatException e) {
            logger.error("", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 居中写文字<br>
     *
     * @param g
     * @param str
     * @param width
     * @param height
     */
    private static void drawMchNameString(Graphics2D g, String str, int width, int height, double qrScale) {
        int strWidth = g.getFontMetrics().stringWidth(str);//字符串写出来的长度
        int strHeight = g.getFontMetrics().getHeight();//字符串写出来的高度
        int qrWidth = (int) ((width > height ? width : height) * qrScale);//二维码宽度(大概比例0.75)
        int qrHeight = (int) (qrWidth + (height - qrWidth) / 2);//二维码y轴高度+顶端空白高度
        //如果 字的长度小于等于二维码长度，直接写一行
        if (strWidth <= qrWidth) {
            qrHeight = qrHeight + strHeight / 2;
            g.drawString(str, (int) ((width - strWidth) / 2), qrHeight);
        } else {
            //获取每个字的长度
            int singleWidth = strWidth / str.length();
            //计算每一行可以放几个字
            int oneLineSize = qrWidth / singleWidth;
            //截取字符个数，分行
            List<String> splitList = splitStrOfLength(str, oneLineSize);
            //循环写
            for (int i = 0; i < splitList.size(); i++) {
                String lineStr = splitList.get(i);
                //第一行离二维码半个字高,其余的离上一行一个字高
                if (i == 0) {
                    qrHeight = qrHeight + strHeight / 2;
                } else {
                    qrHeight = qrHeight + strHeight;
                }
                int lineWidth = g.getFontMetrics().stringWidth(lineStr);
                g.drawString(lineStr, (int) ((width - lineWidth) / 2), qrHeight);
            }
        }
    }

    private static List<String> splitStrOfLength(String str, int splitLen) {
        List<String> listStr = new ArrayList<String>();
        //字符串总长度
        int len = str.length();
        //总行数
        int lineNum = len % splitLen == 0 ? len / splitLen : len / splitLen + 1;
        //临时存放字符串变量
        String subStr = "";
        for (int i = 1; i <= lineNum; i++) {
            if (i < lineNum) {
                subStr = str.substring((i - 1) * splitLen, i * splitLen);
            } else {
                subStr = str.substring((i - 1) * splitLen, len);
            }
            //添加分割字符串
            listStr.add(subStr);
        }
        return listStr;
    }

    public static void main(String[] args) throws Exception {
        String fontPath = "E:/temp/iconfont.css";
        String mchName = "测试";
        BufferedImage img = ImageIO.read(new File("E:/temp/penny.png"));
        BufferedImage image = createHDQRCode("http://www.baidu.com", mchName, img, fontPath);
        ImageIO.write(image, "png", new File("E:/temp/1225.png"));

//        Map<String, String> payType = new HashMap<String, String>();
//        payType.put("微信支付", "icon_general_wechat.png");
//        BufferedImage image = createIntegrationQRCode("http://www.baidu.com", mchName, img, "007fff", img, payType, fontPath);
//        ImageIO.write(image, "png", new File("E:/temp/1226.png"));
    }
}
