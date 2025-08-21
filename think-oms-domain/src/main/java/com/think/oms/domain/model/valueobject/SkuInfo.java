package com.think.oms.domain.model.valueobject;

import com.think.oms.domain.model.constant.SkuCategory;
import com.think.oms.domain.model.constant.SkuType;
import com.think.oms.domain.pl.SkuFullInfo;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class SkuInfo {

    /**
     * skuId
     */
    private String skuId;

    /**
     * 外部SKU ID
     */
    private String externalSkuId;

    /**
     * skuCode
     */
    private String skuCode;

    /**
     * 外部skuCode
     */
    private String externalSkuCode;

    /**
     * Sku Name
     */
    private String skuName;

    /**
     * sku价格(如果是组合 对应的是组合包价格)
     */
    private Long skuPrice;

    /**
     * 商品类型
     */
    private SkuType skuType;

    /**
     * 商品种类
     */
    private SkuCategory skuCategory;


    public void init(SkuFullInfo skuFullInfo){
        skuFullInfo.validate();
        this.skuId = skuFullInfo.getExternalSkuId();
        this.skuType = skuFullInfo.getSkuType();
        this.externalSkuId = skuFullInfo.getExternalSkuId();
        this.externalSkuCode = skuFullInfo.getExternalSkuCode();
        this.skuCode = skuFullInfo.getSkuCode();
        this.skuName = skuFullInfo.getSkuName();
        this.skuPrice = skuFullInfo.getSkuPrice();
        this.skuCategory = skuFullInfo.getSkuCategory();
    }

    public  SkuInfo(String skuId ,String skuCode){
        this.skuId = skuId;
        this.skuCode = skuCode;
    }

    public SkuInfo(){

    }

    public static SkuInfo create(String externalSkuId ,String externalSkuCode){
        Assert.notNull(externalSkuId,"externalSkuId is not null");
        Assert.notNull(externalSkuCode,"externalSkuCode is not null");
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.externalSkuId = externalSkuId;
        skuInfo.externalSkuCode = externalSkuCode;
        return skuInfo;
    }

}
