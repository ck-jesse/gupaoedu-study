package com.coy.gupaoedu.study.spring.doudian;

import com.alibaba.fastjson.JSON;
import com.doudian.open.api.alliance_materialsProductCategory.AllianceMaterialsProductCategoryRequest;
import com.doudian.open.api.alliance_materialsProductCategory.AllianceMaterialsProductCategoryResponse;
import com.doudian.open.api.alliance_materialsProductCategory.param.AllianceMaterialsProductCategoryParam;
import com.doudian.open.api.alliance_materialsProductsSearch.AllianceMaterialsProductsSearchRequest;
import com.doudian.open.api.alliance_materialsProductsSearch.AllianceMaterialsProductsSearchResponse;
import com.doudian.open.api.alliance_materialsProductsSearch.param.AllianceMaterialsProductsSearchParam;
import com.doudian.open.api.buyin_instituteLiveShare.BuyinInstituteLiveShareRequest;
import com.doudian.open.api.buyin_instituteLiveShare.BuyinInstituteLiveShareResponse;
import com.doudian.open.api.buyin_instituteLiveShare.param.BuyinInstituteLiveShareParam;
import com.doudian.open.api.buyin_instituteLiveShare.param.PidInfo;
import com.doudian.open.api.buyin_liveShareMaterial.BuyinLiveShareMaterialRequest;
import com.doudian.open.api.buyin_liveShareMaterial.BuyinLiveShareMaterialResponse;
import com.doudian.open.api.buyin_liveShareMaterial.param.BuyinLiveShareMaterialParam;
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
        // 生活有鱼
//        GlobalConfig.initAppKey("7152772693517927936");
//        GlobalConfig.initAppSecret("c713e060-2ddb-42e7-8bfb-cfa9105b3984");

        // 生活U鱼
        GlobalConfig.initAppKey("7152420904331445791");
        GlobalConfig.initAppSecret("8fad50d0-c62b-42e3-93cd-e86bb0629a8d");

        //入参为code（有效期为10分钟）
        accessToken = AccessTokenBuilder.build("88812c67-e77a-443c-af37-027670c26a4b");
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

    // 直播间分销物料查询
    @Test
    public void liveShareMaterial() {
        BuyinLiveShareMaterialRequest request = new BuyinLiveShareMaterialRequest();
        BuyinLiveShareMaterialParam param = request.getParam();
        param.setAuthorType(1);
//        param.setAuthorLevels(1);
//        param.setFristCids(23);
//        param.setAuthorInfo("我的昵称");
        param.setPage(1L);
        param.setPageSize(10L);
        param.setSortBy(1);
        param.setSortType(0);
        param.setLiveStatus(1L);
        BuyinLiveShareMaterialResponse response = request.execute(accessToken);
        System.out.println(response.toString());
    }


    @Test
    public void test(){
        PidInfo pidInfo = new PidInfo();
        pidInfo.setPid("6971296859097088287");
//        pidInfo.setExternalInfo("");

        BuyinInstituteLiveShareRequest request = new BuyinInstituteLiveShareRequest();
        BuyinInstituteLiveShareParam param = request.getParam();
        param.setPidInfo(pidInfo);
//        param.setOpenId("废弃字段，将下线");
        param.setBuyinId("6945996461834371365");
        param.setNeedQrCode(false);
//        param.setDyCode("6.9 gO:/【ceshi直播间】4.8:/ ÊÊn53CIbpa1SFr8ΨΨ，按长復製此消[转圈]夕条，哒揩ϓƗӢԂƠʮ[咖啡]搜缩，看TA直播##n53CIparSFr8##");
//        param.setProductId(3501844227627868700L);
        BuyinInstituteLiveShareResponse response = request.execute(accessToken);
        System.out.println(response.toString());
    }
}
