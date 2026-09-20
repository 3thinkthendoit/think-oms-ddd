package com.think.oms.domain.model.aggregate.create;

import com.think.oms.domain.model.constant.SkuCategory;
import com.think.oms.domain.model.constant.SkuType;
import com.think.oms.domain.model.valueobject.SkuInfo;
import com.think.oms.domain.port.pl.acl.OrderSkuInfo;
import com.think.oms.domain.port.pl.acl.SkuFullInfo;
import lombok.Getter;

@Getter
public class OrderSku {

    /**
     * 商品基本信息
     */
    private SkuInfo skuInfo;

    /**
     * sku实付价格
     */
    private Long skuPayPrice;

    /**
     * 下单数量
     */
    private Integer skuBuyAmount;

    /**
     * Sku类型
     */
    private SkuType skuType;


    /**
     * 商品种类
     */
    private SkuCategory skuCategory;

    public OrderSku(OrderSkuInfo orderSkuInfo){
        this.skuInfo = SkuInfo.create(orderSkuInfo.getExternalSkuId(), orderSkuInfo.getExternalSkuCode());;
        this.skuBuyAmount = orderSkuInfo.getSkuBuyAmount();
        this.skuPayPrice =orderSkuInfo.getSkuPayPrice();
        this.skuType = skuInfo.getSkuType();
    }

    /**
     * 领域方法
     */
    public void modifySku(SkuFullInfo skuFullInfo){
        this.skuInfo.init(skuFullInfo);
    }

}
