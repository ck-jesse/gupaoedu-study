package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author chenck
 * @date 2021/3/22 11:38
 */
@NoArgsConstructor
@Data
public class ModifyPriceInfoDTO {
    @JsonProperty("supplierName")
    private String supplierName;
    @JsonProperty("highestPrice")
    private BigDecimal highestPrice;
    @JsonProperty("brandName")
    private String brandName;
    @JsonProperty("lowestPrice")
    private BigDecimal lowestPrice;
    @JsonProperty("marketPrice")
    private BigDecimal marketPrice;
    @JsonProperty("specOne")
    private String specOne;
    @JsonProperty("supplyPrice")
    private BigDecimal supplyPrice;
    @JsonProperty("goodsId")
    private Integer goodsId;
    @JsonProperty("newLowestPrice")
    private BigDecimal newLowestPrice;
    @JsonProperty("newMarketPrice")
    private BigDecimal newMarketPrice;
    @JsonProperty("costPrice")
    private BigDecimal costPrice;
    @JsonProperty("goodsSpecId")
    private Integer goodsSpecId;
    @JsonProperty("barCode")
    private String barCode;
    @JsonProperty("newSupplyPrice")
    private BigDecimal newSupplyPrice;
    @JsonProperty("goodsNumber")
    private String goodsNumber;
    @JsonProperty("specTwo")
    private String specTwo;
    @JsonProperty("newHighestPrice")
    private BigDecimal newHighestPrice;
    @JsonProperty("goodsName")
    private String goodsName;
    @JsonProperty("shelfState")
    private String shelfState;
}
