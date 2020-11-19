package com.coy.gupaoedu.study.spring.oss;

/**
 * 阿里云OSS配置
 *
 * @author chenck
 * @date 2020/11/18 16:47
 */
public class OSSClientConstants {

    // OSS访问地址
    public static final String OSS_URL = "https://birdie.oss-cn-shenzhen.aliyuncs.com/";
    //阿里云API的外网域名
    public static final String ENDPOINT = "oss-cn-shenzhen.aliyuncs.com";
    //阿里云API的密钥Access Key ID
    public static final String ACCESS_KEY_ID = "LTAI4GGQPMcy2ZAUbQZTfH1y";
    //阿里云API的密钥Access Key Secret
    public static final String ACCESS_KEY_SECRET = "IX8hSRGeWbNFeIz9Zo7dViQmpXpKfw";
    //阿里云API的bucket名称
    public static final String BACKET_NAME = "birdie";
    //阿里云API公共图片文件夹名称
    public static final String PUBLIC_IMAGES_FOLDER = "public/";
    //阿里云API公共图片文件夹名称
    public static final String AFTERSALE_FOLDER = "aftersale/";
    //阿里云API用户最终分享海报图片文件夹名称
    public static final String USER_POSTER_FOLDER = "poster/";
    //阿里云API分享海报模板图片文件夹名称
    public static final String POSTER_TEMPLATE_FOLDER = "post_template/";
    //用户分享产品图文件夹名称
    public static final String PRODUCT_SHARE_FOLDER = "product_share/";
    //阿里云API公共视频文件夹名称
    public static final String PUBLIC_VIDEO_FOLDER = "video/";
    //京东商品小程序码存放目录，3天清理一次
    public static final String MALL_SHARE_FOLDER = "mall_share/";

    /**
     * 阿里去视频点播AccessKey与密钥
     */
    public static final String VIDEO_ACCESS_KEY_ID = "LTAIm72oGhWOMXcn";
    /**
     * 视频点播密钥
     */
    public static final String VIDEO_ACCESS_KEY_SECRET = "souYglpEtBNoQp6VUHSr0z4u70J7NG";
    /**
     * 视频点播区域
     */
    public static final String VIDEO_REGION_CN = "cn-hangzhou";
    /**
     * STS版本
     */
    public static final String VIDEO_STS_API_VERSION = "2015-04-01";
    /**
     * 视频点播 角色ID
     */
    public static final String VIDEO_ROLE_ARN = "acs:ram::1730408392959263:role/vodrole";

    /**
     * 视频点播加密模版组ID
     */
    public static final String VIDEO_TEMPLATE_GROUP_ID = "5d0fb4eb0edb8b2798193fdfb2cd02e5";

    /**
     * 视频默认模版，不转码
     */
    public static final String VIDEO_TEMPLATE_DEF_ID = "VOD_NO_TRANSCODE";

    /**
     * 视频服务KEY
     */
    public static final String VIDEO_SERVICE_KEY = "52a72116-b56f-480d-97f3-7006dc941bc8";

    /**
     * 播放域名
     */
    public static final String VIDEO_PLAY_DOMAIN = "https://video.huasheng100.com";
}
