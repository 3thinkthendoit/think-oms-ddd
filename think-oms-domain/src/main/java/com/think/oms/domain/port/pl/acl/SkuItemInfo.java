package com.think.oms.domain.port.pl.acl;

import com.think.oms.domain.model.constant.SkuType;
import lombok.Data;
import java.util.List;

@Data
public class SkuItemInfo {

    /**
     * 商品基本信息
     */
    private SkuFullInfo skuFullInfo;

    /**
     * sku实付价格
     */
    private Long skuPayPrice;

    /**
     * 下单数量
     */
    private Integer skuAmount;

    /**
     * 发货优先级
     */
    private Integer priority;

    /**
     * 已发货数量
     */
    private Integer shippingAmount;

    /**
     * 退货数量
     */
    private Integer returnAmount;

    /**
     * 签收数量
     */
    private Integer confirmAmount;

    /**
     * Sku类型
     */
    private SkuType skuType;

    /**
     * 子商品信息
     */
    private List<SkuItemInfo> subSkuList;

}
