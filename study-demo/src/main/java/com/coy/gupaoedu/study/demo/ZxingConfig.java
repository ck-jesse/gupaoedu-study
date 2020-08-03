package com.coy.gupaoedu.study.demo;

import com.google.zxing.BarcodeFormat;

import java.io.File;

/**
 * @author chenck
 * @date 2020/8/3 11:19
 */
public class ZxingConfig {

    private BarcodeFormat format;// 二维码内容
    private String contents;// 条形码格式
    private int width = 150;// 宽度
    private int height = 150;// 高度
    private File qrcodeFile;// 二维码File
    private File logoFile;// logo File
    private boolean showWords;// 是否显示文字
    private String words;// 文字

    public BarcodeFormat getFormat() {
        return format;
    }

    public ZxingConfig setFormat(BarcodeFormat format) {
        this.format = format;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public ZxingConfig setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public ZxingConfig setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public ZxingConfig setHeight(int height) {
        this.height = height;
        return this;
    }

    public File getQrcodeFile() {
        return qrcodeFile;
    }

    public ZxingConfig setQrcodeFile(File qrcodeFile) {
        this.qrcodeFile = qrcodeFile;
        return this;
    }

    public ZxingConfig setQrcodePath(String qrcodePath) {
        if (null != qrcodePath || qrcodePath.trim().length() > 0) {
            this.qrcodeFile = new File(qrcodePath);
        }
        return this;
    }

    public File getLogoFile() {
        return logoFile;
    }

    public ZxingConfig setLogoFile(File logoFile) {
        this.logoFile = logoFile;
        return this;
    }

    public ZxingConfig setLogoPath(String logoPath) {
        if (null != logoPath || logoPath.trim().length() > 0) {
            this.logoFile = new File(logoPath);
        }
        return this;
    }

    public boolean isShowWords() {
        return showWords;
    }

    public ZxingConfig setShowWords(boolean showWords) {
        this.showWords = showWords;
        return this;
    }

    public String getWords() {
        return words;
    }

    public ZxingConfig setWords(String words) {
        this.words = words;
        return this;
    }

    public boolean isShowLogo() {
        if (null == logoFile || !logoFile.exists()) {
            return false;
        }
        return true;
    }
}
