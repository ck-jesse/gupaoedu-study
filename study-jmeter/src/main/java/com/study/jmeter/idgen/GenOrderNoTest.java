//package com.study.jmeter.idgen;
//
//import com.study.jmeter.common.IdConsts;
//import org.apache.jmeter.config.Arguments;
//import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
//import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
//import org.apache.jmeter.samplers.SampleResult;
//
///**
// * @author chenck
// * @date 2019/11/20 16:48
// */
//public class GenOrderNoTest extends AbstractJavaSamplerClient {
//
//    private String ip;
//    private Integer port;
//
//    @Override
//    public Arguments getDefaultParameters() {
//        Arguments params = new Arguments();
//        params.addArgument("ip", IdConsts.DEFAULT_IP);
//        params.addArgument("port", IdConsts.DEFAULT_PORT);
//        return params;
//    }
//
//    @Override
//    public void setupTest(JavaSamplerContext arg0) {
//        ip = arg0.getParameter("ip");
//        port = arg0.getIntParameter("port");
//    }
//
//    @Override
//    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
//        SampleResult result = new SampleResult();
//        /*// 开始计时
//        result.sampleStart();
//        String url = UrlUtil.buildURL(ip, port, "/idgen/genOrderNo");
//        IdGeneratorProto.GenDistributeIdResponse response = RestTemplateInstance.protoRestTemplate.postForObject(url, null,
//                IdGeneratorProto.GenDistributeIdResponse.class);
//
//        String json = RestTemplateInstance.jsonFormat.printToString(response);
//        // 计时结束
//        result.sampleEnd();
//        result.setSuccessful(true);
//        result.setResponseData(json, "utf-8");
//        result.setDataType(SampleResult.TEXT);*/
//        return result;
//    }
//
//}
