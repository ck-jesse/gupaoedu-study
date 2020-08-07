package com.coy.gupaoedu.study.demo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

/**
 * 基于 zxing 生成各种条码（一维码/二维码等）
 * <p>
 * 注：生成的二维码图片的格式为png，识别率更高。
 *
 * @author chenck
 * @date 2020/7/31 10:42
 */
public class ZxingConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZxingConfigUtil.class);

    /**
     * 条码格式
     */
    public static final String FORMAT_NAME = "png";

    /**
     * 矩阵到图像配置，用于解决logo变为黑白的问题
     */
    private static MatrixToImageConfig defaultMatrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);

    /**
     * 默认字体
     */
    private static Font defaultFont = new Font("宋体", Font.PLAIN, 13);

    /**
     * 默认的 EncodeHints
     */
    private static Hashtable<EncodeHintType, Object> defaultEncodeHints = new Hashtable<>();

    /**
     * 获取默认的 DecodeHints
     */
    private static Hashtable<DecodeHintType, Object> defaultDecodeHints = new Hashtable<>();

    static {
        defaultEncodeHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 指定纠错等级
        defaultEncodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 指定编码格式
        defaultEncodeHints.put(EncodeHintType.MARGIN, 1);// 设置空白边距的宽度

        defaultDecodeHints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        defaultDecodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);// 复杂模式，开启PURE_BARCODE模式
    }

    // --------- 生成二维码

    /**
     * 生成二维码
     *
     * @param contents   二维码内容
     * @param width      宽度
     * @param height     高度
     * @param qrcodePath 二维码路径
     */
    public static void createQRCode(String contents, int width, int height, String qrcodePath) {
        ZxingConfig config = new ZxingConfig();
        config.setFormat(BarcodeFormat.QR_CODE);
        config.setContents(contents);
        config.setWidth(width);
        config.setHeight(height);
        config.setQrcodePath(qrcodePath);
        encode(config);
    }

    /**
     * 生成二维码（含logo）
     */
    public static void createQRCodeLogo(String contents, int width, int height, String qrcodePath, String logoPath) {
        ZxingConfig config = new ZxingConfig();
        config.setFormat(BarcodeFormat.QR_CODE);
        config.setContents(contents);
        config.setWidth(width);
        config.setHeight(height);
        config.setQrcodePath(qrcodePath);
        config.setLogoPath(logoPath);
        encode(config);
    }

    // --------- 生成条形码

    /**
     * 生成一维码（条形码）
     * 注：一维码的内从不能为汉字
     */
    public static void createBarCode(String contents, int width, int height, String barcodePath) {
        ZxingConfig config = new ZxingConfig();
        config.setFormat(BarcodeFormat.CODE_128);
        config.setContents(contents);
        config.setWidth(width);
        config.setHeight(height);
        config.setQrcodePath(barcodePath);
        encode(config);
    }

    /**
     * 生成一维码（条形码）
     * 注：一维码的内从不能为汉字
     */
    public static void createBarCode(String contents, int width, int height, String barcodePath, String words) {
        ZxingConfig config = new ZxingConfig();
        config.setFormat(BarcodeFormat.CODE_128);
        config.setContents(contents);
        config.setWidth(width);
        config.setHeight(height);
        config.setQrcodePath(barcodePath);
        config.setShowWords(true);
        config.setWords(words);
        encode(config);
    }

    // --------- 通用的编码方法

    /**
     * 编码，生成各种码，如一维码/二维码
     */
    public static boolean encode(ZxingConfig config) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(config.getContents(), config.getFormat(), config.getWidth(), config.getHeight(),
                    defaultEncodeHints);

            // 不显示logo 和 文字时，直接输出图片
            if (!config.isShowLogo() && !config.isShowWords()) {
                MatrixToImageWriter.writeToPath(bitMatrix, FORMAT_NAME, config.getQrcodeFile().toPath());
                return true;
            }

            // 用于设置配色，解决logo变为黑白的问题
            BufferedImage matrixImage = MatrixToImageWriter.toBufferedImage(bitMatrix, defaultMatrixToImageConfig);

            if (config.isShowLogo()) {
                // 读取logo图片
                BufferedImage logoImage = ImageIO.read(config.getLogoFile());
                // 绘制logo
                drawLogo(matrixImage, logoImage);
            }

            if (config.isShowWords() && null != config.getWords()) {
                FontDesignMetrics metrics = FontDesignMetrics.getMetrics(defaultFont);
                int strWidth = metrics.stringWidth(config.getWords());
                int strHeight = metrics.getHeight();
                BufferedImage wordsImage = new BufferedImage(config.getWidth(), config.getHeight() + strHeight + 5, BufferedImage.TYPE_INT_ARGB);

                // 绘制文字
                drawWords(matrixImage, wordsImage, config, strWidth, strHeight);

                wordsImage.flush();
                ImageIO.write(wordsImage, FORMAT_NAME, config.getQrcodeFile());
                return true;
            }

            // 生成图片
            matrixImage.flush();
            ImageIO.write(matrixImage, FORMAT_NAME, config.getQrcodeFile());
            return true;
        } catch (Exception e) {
            logger.error("zxing encode error", e);
            return false;
        }
    }


    // --------- 通用的解码方法

    /**
     * 解码
     *
     * @param qrcodePath 二维码图片路径
     */
    public static String decode(String qrcodePath) {
        return decode(new File(qrcodePath));
    }

    /**
     * 解码
     *
     * @param qrcodeFile 二维码图片
     */
    public static String decode(File qrcodeFile) {
        try {
            BufferedImage image = ImageIO.read(qrcodeFile);
            if (image == null) {
                logger.error("the decode image may be not exit.");
                return null;
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap, defaultDecodeHints);

            return result.getText();
        } catch (Exception e) {
            logger.error("zxing decode error ", e);
        }
        return null;
    }


    // --------- 绘制图层相关方法，如logo，文字等

    /**
     * 绘制logo
     *
     * @param qrcodeImage 二维码Image
     * @param logoImage   logo Image
     */
    public static void drawLogo(BufferedImage qrcodeImage, BufferedImage logoImage) {

        int matrixWidth = qrcodeImage.getWidth();
        int matrixHeigh = qrcodeImage.getHeight();

        int width = matrixWidth / 5;
        int height = matrixHeigh / 5;
        int x = width * 2;
        int y = height * 2;

        // 读取二维码图片，并构建绘图对象
        Graphics2D g2 = qrcodeImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// 消除图片锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);// 消除文字锯齿
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);

        // 绘制图片
        //g2.drawImage(logoImage, x, y, width, height, null);
        // 绘制图片(缩放logo)
        g2.drawImage(logoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), x, y, null);

        // 给logo设置一道边框
        //g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));// 设置笔画粗细
        //g2.setColor(Color.gray);
        //g2.draw(new RoundRectangle2D.Float(x, y, width - 2, height - 2, 20, 20));// 绘制圆弧矩形

        g2.dispose();
    }

    /**
     * 绘制文字
     * 注：创建一个新的画布来重新绘制二维码图片和文字
     */
    public static void drawWords(BufferedImage qrcodeImage, BufferedImage wordsImage, ZxingConfig config, int strWidth, int strHeight) {
        Graphics2D wordsg2 = wordsImage.createGraphics();
        wordsg2.setColor(Color.WHITE);
        wordsg2.fillRect(0, 0, wordsImage.getWidth(), wordsImage.getHeight());//填充整个屏幕

        wordsg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// 消除图片锯齿
        wordsg2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);// 消除文字锯齿

        wordsg2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));// 设置笔画粗细
        wordsg2.setColor(Color.BLACK);
        wordsg2.setFont(defaultFont);// 字体,字型,字号
        wordsg2.drawString(config.getWords(), ((config.getWidth() - strWidth) / 2), config.getHeight() + strHeight);// 绘制文字
        wordsg2.drawImage(qrcodeImage, 0, 0, config.getWidth(), config.getHeight(), null);// 绘制图片
        wordsg2.dispose();
    }

}
