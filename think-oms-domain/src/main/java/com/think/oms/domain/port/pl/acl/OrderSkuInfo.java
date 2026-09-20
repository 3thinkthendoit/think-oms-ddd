package com.think.oms.domain.port.pl.acl;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderSkuInfo {

    /**
     * 外部SKU ID
     */
    private String externalSkuId;

    /**
     * 外部skuCode
     */
    private String externalSkuCode;

    /**
     * Sku Name
     */
    private String skuName;

    /**
     * sku价格
     */
    private Long skuPayPrice;

    /**
     * sku下单数量
     */
    private Integer skuBuyAmount;
}
