package com.coy.gupaoedu.study.spring.oss;


import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * java使用阿里云OSS存储对象上传图片
 *
 * @author chenck
 * @date 2020/11/18 16:41
 */
@Slf4j
public class AliyunOSSClientUtil {

    //阿里云API的内或外网域名
    private static String ENDPOINT;
    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID;
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET;
    //阿里云API的bucket名称
    private static String BACKET_NAME;
    //阿里云API用户最终分享海报图文件夹名称
    private static String USER_POSTER_FOLDER;

    private static OSS ossClient;

    //初始化属性
    static {
        ENDPOINT = OSSClientConstants.ENDPOINT;
        ACCESS_KEY_ID = OSSClientConstants.ACCESS_KEY_ID;
        ACCESS_KEY_SECRET = OSSClientConstants.ACCESS_KEY_SECRET;
        BACKET_NAME = OSSClientConstants.BACKET_NAME;
        USER_POSTER_FOLDER = OSSClientConstants.USER_POSTER_FOLDER;
    }

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public static synchronized OSS getOSSClient() {
        if (null == ossClient) {
            ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        }
        return ossClient;
    }

    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        //存储空间
        final String bucketNames = bucketName;
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            return bucket.getName();
        }
        return bucketNames;
    }

    public static void getFileFromOSS(String key, String folder, String fileName) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(BACKET_NAME, folder + "/" + key);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        getOSSClient().getObject(getObjectRequest, new File(fileName));
    }

    /**
     * 删除存储空间buckName
     *
     * @param bucketName 存储空间
     */
    public static void deleteBucket(String bucketName) {
        getOSSClient().deleteBucket(bucketName);
    }

    /**
     * 创建模拟文件夹
     *
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public static String createFolder(String bucketName, String folder) {
        OSS ossClient = AliyunOSSClientUtil.getOSSClient();
        //文件夹名
        final String keySuffixWithSlash = folder;
        //判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param folder 模拟文件夹名 如"qj_nanjing/"
     * @param key    Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(String folder, String key) {
        getOSSClient().deleteObject(BACKET_NAME, folder + key);
    }

    /**
     * 上传文件到OSS服务器上
     *
     * @param inputStream
     * @param folder
     * @param fileName
     * @return
     */
    public static String uploadImages(InputStream inputStream, String folder, String fileName) {
        //初始化OSSClient
        OSS ossClient = AliyunOSSClientUtil.getOSSClient();
        String resultStr = null;
        try {
            int fileSize = inputStream.available();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(inputStream.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(BACKET_NAME, folder + fileName, inputStream, metadata);
            //解析结果
            resultStr = putResult.getETag();
            return folder + fileName;
        } catch (Exception e) {
            log.error("上传OSS异常", e);
        }
        return resultStr;

    }

    /**
     * 文件上传
     *
     * @param file   文件
     * @param folder 模拟文件夹名
     * @return 文件的访问url
     * @author chenck
     * @date 2020/4/27 10:18
     */
    public static String uploadFile2OSS(File file, String folder) {
        OSS ossClient = AliyunOSSClientUtil.getOSSClient();
        String fileName = file.getName();
        String key = folder + fileName;
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(BACKET_NAME, key, file);
            PutObjectResult putResult = ossClient.putObject(putObjectRequest);
            log.info("上传OSS,key={},result={}", key, JSON.toJSONString(putResult));
            return OSSClientConstants.OSS_URL + key;
        } catch (Exception e) {
            log.error("上传OSS异常", e);
            throw new RuntimeException("上传OSS异常:" + e.getMessage());
        }
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if (".mp4".equalsIgnoreCase(fileExtension)) {
            return "audio/mp4";
        }
        if (".avi".equalsIgnoreCase(fileExtension)) {
            return "video/avi";
        }
        if (".mpg".equalsIgnoreCase(fileExtension)) {
            return "video/mpg";
        }
        if (".asf".equalsIgnoreCase(fileExtension)) {
            return "video/x-ms-asf";
        }
        if (".wmv".equalsIgnoreCase(fileExtension)) {
            return "video/x-ms-wmv";
        }
        if (".movie".equalsIgnoreCase(fileExtension)) {
            return "video/x-sgi-movie";
        }
        if (".ogg".equalsIgnoreCase(fileExtension)) {
            return "audio/mp3";
        }
        if (".mp4".equalsIgnoreCase(fileExtension)) {
            return "audio/mp4";
        }
        if (".rmi".equalsIgnoreCase(fileExtension)) {
            return "audio/mid";
        }
        if (".wav".equalsIgnoreCase(fileExtension)) {
            return "audio/wav";
        }
        if (".wav".equalsIgnoreCase(fileExtension)) {
            return "audio/wav";
        }
        if (".asx".equalsIgnoreCase(fileExtension)) {
            return "video/x-ms-asf";
        }
        if (".ivf".equalsIgnoreCase(fileExtension)) {
            return "video/x-ivf";
        }
        if (".m1v".equalsIgnoreCase(fileExtension)) {
            return "video/x-mpeg";
        }
        if (".m2v".equalsIgnoreCase(fileExtension)) {
            return "video/x-mpeg";
        }
        if (".m4e".equalsIgnoreCase(fileExtension)) {
            return "video/mpeg4";
        }
        if (".wmx".equalsIgnoreCase(fileExtension)) {
            return "video/x-ms-wmx";
        }
        if (".wvx".equalsIgnoreCase(fileExtension)) {
            return "video/x-ms-wvx";
        }
        if (".m4e".equalsIgnoreCase(fileExtension)) {
            return "video/mpeg4";
        }
        if (".m4e".equalsIgnoreCase(fileExtension)) {
            return "video/mpeg4";
        }
        //默认返回类型
        return "image/jpeg";
    }

    //测试文件上传
    public static void main(String[] args) throws FileNotFoundException {
        //上传图片
        String imgFile = "C:\\Users\\Administrator\\Pictures\\userDefaultAvatar.png";
        File file = new File(imgFile);
        String fileName = System.currentTimeMillis() + imgFile.substring(imgFile.lastIndexOf("."));
        String result = AliyunOSSClientUtil.uploadFile2OSS(file, "avatar/");
        System.out.println(result);

    }
}