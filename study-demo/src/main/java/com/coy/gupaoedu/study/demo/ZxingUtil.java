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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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
public class ZxingUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZxingUtil.class);

    /**
     * 条码格式
     */
    public static final String FORMAT_NAME = "png";

    /**
     * 矩阵到图像配置，用于解决logo变为黑白的问题
     */
    private static MatrixToImageConfig defaultMatrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);

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
        encode(contents, BarcodeFormat.QR_CODE, width, height, qrcodePath, null);
    }

    /**
     * 生成二维码（含logo）
     */
    public static void createQRCodeLogo(String contents, int width, int height, String qrcodePath, String logoPath) {
        encode(contents, BarcodeFormat.QR_CODE, width, height, qrcodePath, logoPath);
    }

    // --------- 生成条形码

    /**
     * 生成一维码（条形码）
     */
    public static void createBarCode(String contents, int width, int height, String barcodePath) {
        encodeWords(contents, BarcodeFormat.CODE_128, width, height, new File(barcodePath));
    }

    // --------- 通用的编码方法

    /**
     * 编码，生成各种码，如一维码/二维码
     */
    public static void encode(String contents, BarcodeFormat format, int width, int height, String qrcodePath) {
        encode(contents, format, width, height, new File(qrcodePath), null);
    }

    /**
     * 编码，生成各种码，如一维码/二维码
     */
    public static void encode(String contents, BarcodeFormat format, int width, int height, String qrcodePath, String logoPath) {
        if (null == logoPath) {
            encode(contents, format, width, height, new File(qrcodePath), null);
        } else {
            encode(contents, format, width, height, new File(qrcodePath), new File(logoPath));
        }
    }

    /**
     * 编码，生成各种码，如一维码/二维码
     *
     * @param contents   二维码内容
     * @param format     条形码格式
     * @param width      宽度
     * @param height     高度
     * @param qrcodeFile 二维码File
     * @param logoFile   logo File
     */
    public static boolean encode(String contents, BarcodeFormat format, int width, int height, File qrcodeFile, File logoFile) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height, getDefaultEncodeHints());

            // logo不存在时直接生成
            if (null == logoFile || !logoFile.exists()) {
                MatrixToImageWriter.writeToPath(bitMatrix, FORMAT_NAME, qrcodeFile.toPath());
                return true;
            }

            // 用于设置配色，解决logo变为黑白的问题
            BufferedImage matrixImage = MatrixToImageWriter.toBufferedImage(bitMatrix, defaultMatrixToImageConfig);

            // 读取logo图片
            BufferedImage logoImage = ImageIO.read(logoFile);
            // 添加logo
            addLogo(matrixImage, logoImage);
            // 生成图片
            ImageIO.write(matrixImage, FORMAT_NAME, qrcodeFile);
            return true;
        } catch (Exception e) {
            logger.error("zxing encode error", e);
            return false;
        }
    }

    public static boolean encodeWords(String contents, BarcodeFormat format, int width, int height, File qrcodeFile) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height, getDefaultEncodeHints());

            // 用于设置配色，解决logo变为黑白的问题
            BufferedImage matrixImage = MatrixToImageWriter.toBufferedImage(bitMatrix, defaultMatrixToImageConfig);

            // 添加文字
            addWords(matrixImage, contents);
            // 生成图片
            ImageIO.write(matrixImage, FORMAT_NAME, qrcodeFile);
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

            Result result = new MultiFormatReader().decode(bitmap, getDefaultDecodeHints());

            return result.getText();
        } catch (Exception e) {
            logger.error("zxing decode error ", e);
        }
        return null;
    }


    /**
     * 添加logo
     *
     * @param qrcodeImage 二维码Image
     * @param logoImage   logo Image
     */
    public static void addLogo(BufferedImage qrcodeImage, BufferedImage logoImage) {

        int matrixWidth = qrcodeImage.getWidth();
        int matrixHeigh = qrcodeImage.getHeight();

        int width = matrixWidth / 5;
        int height = matrixHeigh / 5;
        int x = width * 2;
        int y = height * 2;

        // 读取二维码图片，并构建绘图对象
        Graphics2D g2 = qrcodeImage.createGraphics();

        // 绘制图片
        g2.drawImage(logoImage, x, y, width, height, null);

        // 给logo设置一道边框
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));// 设置笔画对象
        g2.setColor(Color.gray);
        g2.draw(new RoundRectangle2D.Float(x, y, width - 2, height - 2, 20, 20));// 绘制圆弧矩形

        g2.dispose();
        qrcodeImage.flush();
    }

    /**
     * TODO 添加文字
     *
     * @param
     */
    public static void addWords(BufferedImage qrcodeImage, String words) {

        int matrixWidth = qrcodeImage.getWidth();
        int matrixHeigh = qrcodeImage.getHeight();

        int width = matrixWidth / 4;
        int height = matrixHeigh;
        int x = width * 2;
        int y = height * 2;

        // 读取二维码图片，并构建绘图对象
        Graphics2D g2 = qrcodeImage.createGraphics();

        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));// 设置笔画对象
        g2.setColor(Color.black);
        g2.setFont(new Font(null, Font.BOLD, 12));// 字体,字型,字号
        g2.drawString(words, width, height);// 绘制字符串

        g2.dispose();
        qrcodeImage.flush();
    }

    /**
     * 获取默认的 EncodeHints
     */
    private static Hashtable<EncodeHintType, Object> getDefaultEncodeHints() {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 指定纠错等级
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 指定编码格式
        hints.put(EncodeHintType.MARGIN, 1);// 设置空白边距的宽度
        return hints;
    }

    /**
     * 获取默认的 DecodeHints
     */
    private static Hashtable<DecodeHintType, Object> getDefaultDecodeHints() {
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);// 复杂模式，开启PURE_BARCODE模式
        return hints;
    }
}
