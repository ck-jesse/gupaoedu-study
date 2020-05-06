package com.study.jmeter.hxcard;

import com.hs.productservice.api.proto.getdetailbyid.ProductServiceApiGetDetailById;
import com.study.jmeter.common.RestTemplateInstance;
import com.study.jmeter.common.UrlUtil;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询权益卡商品详情
 *
 * @author chenck
 * @date 2020/4/30 14:24
 */
public class QykGoodDetailByIdTest extends AbstractJavaSamplerClient {

    private static final Logger logger = LoggerFactory.getLogger(QykGoodDetailByIdTest.class);
    private String ip;
    private Integer port;
    private Long id;// 商品id

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("ip", "172.18.61.145");
        params.addArgument("port", "80");
        params.addArgument("id", "0");
        return params;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        ip = arg0.getParameter("ip");
        port = arg0.getIntParameter("port");
        id = arg0.getLongParameter("id");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        // 开始计时
        result.sampleStart();
        String url = UrlUtil.buildURL(ip, port, "/productservice/productapi/qyk/goods/getDetailByIdSeckill");
        //logger.info("{}", url);
        ProductServiceApiGetDetailById.GetDetailByIdRequestDTO.Builder builder = ProductServiceApiGetDetailById.GetDetailByIdRequestDTO.newBuilder();
        if (null != id) {
            builder.setId(id);
        }
        ProductServiceApiGetDetailById.GetDetailByIdResponseJsonResult response = RestTemplateInstance.protoRestTemplate.postForObject(url,
                builder.build(), ProductServiceApiGetDetailById.GetDetailByIdResponseJsonResult.class);
        String json = RestTemplateInstance.jsonFormat.printToString(response);

        // 计时结束
        result.sampleEnd();
        result.setSuccessful(true);
        result.setResponseData(json, "utf-8");
        result.setDataType(SampleResult.TEXT);
        return result;
    }

}
