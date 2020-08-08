//package com.study.jmeter.idgen;
//
//import com.hs.id.generator.proto.IdGeneratorProto;
//import com.study.jmeter.common.IdConsts;
//import com.study.jmeter.common.RestTemplateInstance;
//import com.study.jmeter.common.UrlUtil;
//import org.apache.jmeter.config.Arguments;
//import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
//import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
//import org.apache.jmeter.samplers.SampleResult;
//
///**
// * @author chenck
// * @date 2019/11/20 16:48
// */
//public class GenUidByParamTest extends AbstractJavaSamplerClient {
//
//    private String ip;
//    private Integer port;
//    private String prefix;
//    private String suffix;
//
//    @Override
//    public Arguments getDefaultParameters() {
//        Arguments params = new Arguments();
//        params.addArgument("ip", IdConsts.DEFAULT_IP);
//        params.addArgument("port", IdConsts.DEFAULT_PORT);
//        params.addArgument("prefix", "");
//        params.addArgument("suffix", "");
//        return params;
//    }
//
//    @Override
//    public void setupTest(JavaSamplerContext arg0) {
//        ip = arg0.getParameter("ip");
//        port = arg0.getIntParameter("port");
//        prefix = arg0.getParameter("prefix");
//        suffix = arg0.getParameter("suffix");
//    }
//
//    @Override
//    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
//        SampleResult result = new SampleResult();
//        // 开始计时
//        result.sampleStart();
//        String url = UrlUtil.buildURL(ip, port, "/idgen/genUserId");
//
//        IdGeneratorProto.GenUserIdRequest.Builder builder = IdGeneratorProto.GenUserIdRequest.newBuilder();
//        builder.setPrefix(prefix);
//        builder.setSuffix(suffix);
//        IdGeneratorProto.GenDistributeIdResponse response = RestTemplateInstance.protoRestTemplate.postForObject(url, builder.build(),
//                IdGeneratorProto.GenDistributeIdResponse.class);
//
//        String json = RestTemplateInstance.jsonFormat.printToString(response);
//        // 计时结束
//        result.sampleEnd();
//        result.setSuccessful(true);
//        result.setResponseData(json, "utf-8");
//        result.setDataType(SampleResult.TEXT);
//        return result;
//    }
//
//}
