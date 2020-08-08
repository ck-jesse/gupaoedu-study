//package com.study.jmeter.hxcard;
//
//import com.hx.card.service.proto.PopupConfigProto;
//import com.study.jmeter.common.RestTemplateInstance;
//import com.study.jmeter.common.UrlUtil;
//import org.apache.jmeter.config.Arguments;
//import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
//import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
//import org.apache.jmeter.samplers.SampleResult;
//import org.springframework.util.StringUtils;
//
///**
// * popup 列表
// *
// * @author chenck
// * @date 2020/4/30 14:24
// */
//public class PopupQueryListTest extends AbstractJavaSamplerClient {
//
//    private String ip;
//    private Integer port;
//    private String title;// 标题
//    private Integer status;// 状态:1上架,2下架
//    private Integer useCache;// 是否使用缓存，1表示不使用缓存，0或其他值则表示使用缓存
//    private Integer pageNum;
//    private Integer pageSize;
//
//    @Override
//    public Arguments getDefaultParameters() {
//        Arguments params = new Arguments();
//        params.addArgument("ip", "172.18.219.222");
//        params.addArgument("port", "80");
//        params.addArgument("title", "");
//        params.addArgument("status", "1");
//        params.addArgument("useCache", "0");
//        params.addArgument("pageNum", "1");
//        params.addArgument("pageSize", "200");
//        return params;
//    }
//
//    @Override
//    public void setupTest(JavaSamplerContext arg0) {
//        ip = arg0.getParameter("ip");
//        port = arg0.getIntParameter("port");
//        title = arg0.getParameter("title");
//        status = arg0.getIntParameter("status");
//        useCache = arg0.getIntParameter("useCache");
//        pageNum = arg0.getIntParameter("pageNum");
//        pageSize = arg0.getIntParameter("pageSize");
//    }
//
//    @Override
//    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
//        SampleResult result = new SampleResult();
//        // 开始计时
//        result.sampleStart();
//        String url = UrlUtil.buildURL(ip, port, "/hx-card-service/popupConfig/queryPopupConfigList");
//
//        PopupConfigProto.PopupConfigQueryListRequest.Builder builder = PopupConfigProto.PopupConfigQueryListRequest.newBuilder();
//        if (!StringUtils.isEmpty(title)) {
//            builder.setTitle(title);
//        }
//        if (null != status) {
//            builder.setStatus(status);
//        }
//        if (null != useCache) {
//            builder.setUseCache(useCache);
//        }
//        builder.setPageNum(pageNum);
//        builder.setPageSize(pageSize);
//        PopupConfigProto.PopupConfigQueryListResponse response = RestTemplateInstance.protoRestTemplate.postForObject(url, builder.build(),
//                PopupConfigProto.PopupConfigQueryListResponse.class);
//        String json = RestTemplateInstance.jsonFormat.printToString(response);
//
//        // 计时结束
//        result.sampleEnd();
//        result.setSuccessful(true);
//        result.setResponseData(json, "utf-8");
//        result.setDataType(SampleResult.TEXT);
//        return result;
//    }
//
//}
