package com.study.jmeter.idgen;

import com.hs.id.generator.proto.IdGeneratorProto;
import com.study.jmeter.common.IdConsts;
import com.study.jmeter.common.RestTemplateInstance;
import com.study.jmeter.common.UrlUtil;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.util.StringUtils;

/**
 * @author chenck
 * @date 2019/11/20 16:48
 */
public class GenDistributeIdTest extends AbstractJavaSamplerClient {

    private String ip;
    private Integer port;
    // 0-默认，1-USER_ID，2-ORDER_NO，3-COUPON_NO
    private int bizType;
    private String bizTypeStr;

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("ip", IdConsts.DEFAULT_IP);
        params.addArgument("port", IdConsts.DEFAULT_PORT);
        params.addArgument("bizType", "0");
        params.addArgument("bizTypeStr", "");
        return params;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        ip = arg0.getParameter("ip");
        port = arg0.getIntParameter("port");
        bizType = arg0.getIntParameter("bizType");
        bizTypeStr = arg0.getParameter("bizTypeStr");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        // 开始计时
        result.sampleStart();
        String url = UrlUtil.buildURL(ip, port, "/idgen/genDistributeId");
        IdGeneratorProto.GenDistributeIdRequest.Builder builder = IdGeneratorProto.GenDistributeIdRequest.newBuilder();
        builder.setBizType(IdGeneratorProto.BizType.forNumber(bizType));
        if (!StringUtils.isEmpty(bizTypeStr)) {
            builder.setBizTypeStr(bizTypeStr);
        }
        IdGeneratorProto.GenDistributeIdResponse response = RestTemplateInstance.protoRestTemplate.postForObject(url, builder.build(),
                IdGeneratorProto.GenDistributeIdResponse.class);

        String json = RestTemplateInstance.jsonFormat.printToString(response);
        // 计时结束
        result.sampleEnd();
        result.setSuccessful(true);
        result.setResponseData(json, "utf-8");
        result.setDataType(SampleResult.TEXT);
        return result;
    }

}
