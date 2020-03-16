package com.study.jmeter.seckillproduct;

import com.hs.seckill.productservice.api.proto.lockuserstock.ProductServiceApiStockService;
import com.study.jmeter.common.RestTemplateInstance;
import com.study.jmeter.common.UrlUtil;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * 库存操作
 *
 * @author chenck
 * @date 2019/11/20 16:48
 */
public class LockUserStockByListTest extends AbstractJavaSamplerClient {

    private String ip;
    private Integer port;
    private String goodId;// 商品id
    private String skuId;// skuid
    private String orderNo;// 订单号
    private Integer operate;// 库存操作类型，1-扣减库存，2-回滚库存
    private Integer num;// 数量

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("ip", "seckill-test.huasheng100.com");
        params.addArgument("port", "80");
        params.addArgument("goodId", "1165082639428517888");
        params.addArgument("skuId", "1165082639428517888");
        params.addArgument("orderNo", "123123123");
        params.addArgument("operate", "1");
        params.addArgument("num", "1");
        return params;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        ip = arg0.getParameter("ip");
        port = arg0.getIntParameter("port");
        goodId = arg0.getParameter("goodId");
        skuId = arg0.getParameter("skuId");
        orderNo = arg0.getParameter("orderNo");
        operate = arg0.getIntParameter("operate");
        num = arg0.getIntParameter("num");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        // 开始计时
        result.sampleStart();
        String url = UrlUtil.buildURL(ip, port, "/seckill/stock/lockUserStockByList");

        ProductServiceApiStockService.GoodStockOperateEnum operateEnum = ProductServiceApiStockService.GoodStockOperateEnum.forNumber(operate);
        if (null == operateEnum) {
            // 计时结束
            result.sampleEnd();
            result.setSuccessful(false);
            result.setResponseData("库存操作类型不合法，只能是1-扣减库存，2-回滚库存", "utf-8");
            result.setDataType(SampleResult.TEXT);
            return result;
        }

        ProductServiceApiStockService.LockUserStockDTO.Builder good = ProductServiceApiStockService.LockUserStockDTO.newBuilder();
        good.setGoodId(goodId);
        good.setSkuId(skuId);
        good.setOrderId(orderNo);
        good.setStockOperate(operateEnum);
        good.setNum(num);
        ProductServiceApiStockService.LockStockByListRequest.Builder request = ProductServiceApiStockService.LockStockByListRequest.newBuilder();
        request.addLockUserStockDtos(good);

        // 200成功，201库存不够，202不是秒杀商品，203库存归还过多，204库存操作类型错误
        ProductServiceApiStockService.LockStockByListResponse response = RestTemplateInstance.protoRestTemplate.postForObject(url, request.build(),
                ProductServiceApiStockService.LockStockByListResponse.class);
        String json = RestTemplateInstance.jsonFormat.printToString(response);

        // 计时结束
        result.sampleEnd();
        result.setSuccessful(true);
        result.setResponseData(json, "utf-8");
        result.setDataType(SampleResult.TEXT);
        return result;
    }

}
