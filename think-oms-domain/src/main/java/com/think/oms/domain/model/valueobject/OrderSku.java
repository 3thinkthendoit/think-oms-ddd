package com.think.oms.domain.model.valueobject;

import com.think.oms.domain.model.constant.SkuType;
import com.think.oms.domain.pl.SkuInfo;
import lombok.Getter;
import java.util.List;

@Getter
public class OrderSku {

    /**
     * skuId
     */
    private String skuId;

    private String externalSkuId;

    /**
     * skuCode
     */
    private String skuCode;

    private String externalSkuCode;

    /**
     * Sku Name
     */
    private String skuName;

    /**
     * sku价格
     */
    private Long skuPrice;

    /**
     * sku实付价格
     */
    private Long skuPayPrice;

    /**
     * 下单数量
     */
    private Integer skuAmount;

    /**
     * Sku类型
     */
    private SkuType skuType;

    /**
     * 子商品信息
     */
    private List<OrderSku> subSkuInfos;


    public OrderSku(SkuInfo skuInfo, List<OrderSku> subSkuInfos){
        this.externalSkuCode = skuInfo.getExternalSkuCode();
        this.externalSkuId = skuInfo.getExternalSkuId();
        this.skuAmount = skuInfo.getSkuAmount();
        this.skuPayPrice =skuInfo.getSkuPayPrice();
        this.skuType = skuInfo.getSkuType();
        this.subSkuInfos = subSkuInfos;
    }

    /**
     * 领域方法 不允许用set形式
     * @param skuId
     * @param skuCode
     * @param skuPrice
     */
    public void modifySku(String skuId,String skuCode,Long skuPrice){
        this.skuId = skuId;
        this.skuCode = skuCode;
        this.skuPrice = skuPrice;
    }

}