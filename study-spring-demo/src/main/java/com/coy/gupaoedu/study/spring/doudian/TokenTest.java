package com.coy.gupaoedu.study.spring.doudian;

import com.alibaba.fastjson.JSON;
import com.doudian.open.api.alliance_materialsProductCategory.AllianceMaterialsProductCategoryRequest;
import com.doudian.open.api.alliance_materialsProductCategory.AllianceMaterialsProductCategoryResponse;
import com.doudian.open.api.alliance_materialsProductCategory.param.AllianceMaterialsProductCategoryParam;
import com.doudian.open.api.alliance_materialsProductsSearch.AllianceMaterialsProductsSearchRequest;
import com.doudian.open.api.alliance_materialsProductsSearch.AllianceMaterialsProductsSearchResponse;
import com.doudian.open.api.alliance_materialsProductsSearch.param.AllianceMaterialsProductsSearchParam;
import com.doudian.open.api.token.AccessTokenParam;
import com.doudian.open.api.token.AccessTokenRequest;
import com.doudian.open.api.token.AccessTokenResponse;
import com.doudian.open.api.token_create.TokenCreateRequest;
import com.doudian.open.api.token_create.TokenCreateResponse;
import com.doudian.open.api.token_create.param.TokenCreateParam;
import com.doudian.open.core.AccessToken;
import com.doudian.open.core.AccessTokenBuilder;
import com.doudian.open.core.DoudianOpConfig;
import com.doudian.open.core.GlobalConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author chenck
 * @date 2022/10/10 15:53
 */
public class TokenTest {

    AccessToken accessToken;

    // 获取accessToken
    @Before
    public void accessToken() {
        //设置appKey和appSecret，全局设置一次
        GlobalConfig.initAppKey("7152772693517927936");
        GlobalConfig.initAppSecret("c713e060-2ddb-42e7-8bfb-cfa9105b3984");

        //入参为code（有效期为10分钟）
        accessToken = AccessTokenBuilder.build("54689fe4-bc60-493c-ba83-57918e40a7ac");
        System.out.println(JSON.toJSONString(accessToken));
    }

    // 类目查询
    @Test
    public void materialsProductCategory() {
        AllianceMaterialsProductCategoryRequest request = new AllianceMaterialsProductCategoryRequest();
        AllianceMaterialsProductCategoryParam param = request.getParam();
        param.setParentId(0L);
        AllianceMaterialsProductCategoryResponse response = request.execute(accessToken);
        System.out.println(response.toString());
    }

    // 检索精选联盟商品
    @Test
    public void materialsProductsSearch() {
        AllianceMaterialsProductsSearchRequest request = new AllianceMaterialsProductsSearchRequest();
        AllianceMaterialsProductsSearchParam param = request.getParam();
        param.setFirstCids(Arrays.asList(20000L, 20001L));
        param.setPriceMin(1);
        param.setPriceMax(1000);
        param.setSellNumMin(0);
        param.setSellNumMax(1000);
        param.setSearchType(0);
        param.setSortType(0);
        param.setCosFeeMin(1);
        param.setCosFeeMax(50);
        param.setCosRatioMin(1);
        param.setCosRatioMax(15);
        param.setPage(1L);
        param.setPageSize(10L);
        param.setShareStatus(1);
        AllianceMaterialsProductsSearchResponse response = request.execute(accessToken);
        System.out.println(response.toString());
    }
}
