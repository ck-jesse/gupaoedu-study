package com.study.jmeter.hxcard;

import com.hx.card.service.proto.CardEquityProto;
import com.study.jmeter.common.RestTemplateInstance;
import com.study.jmeter.common.UrlUtil;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.util.StringUtils;

/**
 * 花享卡权益
 *
 * @author chenck
 * @date 2020/4/30 14:24
 */
public class CardEquityQueryListTest extends AbstractJavaSamplerClient {

    private String ip;
    private Integer port;
    private Integer supplierId;// 供应商id，关联供应商表主键
    private Integer catalogId;// 分类id，关联分类表主键
    private String name;// 权益名
    private Integer isRecommended;// 是否推荐, 1:不推荐，2:推荐
    private Integer status;// 状态，1:上架，2:下架
    private Integer useCache;// 是否使用缓存，1表示不使用缓存，0或其他值则表示使用缓存
    private Integer pageNum;
    private Integer pageSize;

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("ip", "172.18.219.222");
        params.addArgument("port", "80");
        params.addArgument("supplierId", "");
        params.addArgument("catalogId", "");
        params.addArgument("name", "");
        params.addArgument("isRecommended", "2");
        params.addArgument("status", "1");
        params.addArgument("useCache", "0");
        params.addArgument("pageNum", "1");
        params.addArgument("pageSize", "200");
        return params;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        ip = arg0.getParameter("ip");
        port = arg0.getIntParameter("port");
        supplierId = arg0.getIntParameter("supplierId");
        catalogId = arg0.getIntParameter("catalogId");
        name = arg0.getParameter("name");
        isRecommended = arg0.getIntParameter("isRecommended");
        status = arg0.getIntParameter("status");
        useCache = arg0.getIntParameter("useCache");
        pageNum = arg0.getIntParameter("pageNum");
        pageSize = arg0.getIntParameter("pageSize");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        // 开始计时
        result.sampleStart();
        String url = UrlUtil.buildURL(ip, port, "/hx-card-service/cardEquity/queryCardEquityList");

        CardEquityProto.CardEquityQueryListRequest.Builder builder = CardEquityProto.CardEquityQueryListRequest.newBuilder();
        if (null != supplierId) {
            builder.setSupplierId(supplierId);
        }
        if (null != catalogId) {
            builder.setCatalogId(catalogId);
        }
        if (!StringUtils.isEmpty(name)) {
            builder.setName(name);
        }
        if (null != isRecommended) {
            builder.setIsRecommended(isRecommended);
        }
        if (null != status) {
            builder.setStatus(status);
        }
        if (null != useCache) {
            builder.setUseCache(useCache);
        }
        builder.setPageNum(pageNum);
        builder.setPageSize(pageSize);
        CardEquityProto.CardEquityQueryListResponse response = RestTemplateInstance.protoRestTemplate.postForObject(url, builder.build(),
                CardEquityProto.CardEquityQueryListResponse.class);
        String json = RestTemplateInstance.jsonFormat.printToString(response);

        // 计时结束
        result.sampleEnd();
        result.setSuccessful(true);
        result.setResponseData(json, "utf-8");
        result.setDataType(SampleResult.TEXT);
        return result;
    }

}
