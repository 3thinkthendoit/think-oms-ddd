package com.think.oms.domain.model.aggregate.orderfulfill;

import com.think.oms.domain.port.pl.acl.SkuItemInfo;
import lombok.Getter;
import java.util.Map;

@Getter
public class FulfillSkuItem {

    /**
     * 商品SkuCode
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 下单数量
     */
    private Integer skuAmount;


    /**
     * 已发货数量
     */
    private Integer shippingAmount;

    /**
     * 发货优先级 0 无需发货 数字越大优先级越高
     */
    private Integer priority;

    /**
     * 分仓结果 k= warehouseCode v = amount
     */
    private Map<String,Integer> dispatchInfo;

    public FulfillSkuItem(SkuItemInfo skuItemInfo){
        this.skuCode = skuItemInfo.getSkuFullInfo().getSkuCode();
        this.skuName = skuItemInfo.getSkuFullInfo().getSkuName();
        this.skuAmount = skuItemInfo.getSkuAmount();
        this.shippingAmount = skuItemInfo.getShippingAmount();
        this.priority = skuItemInfo.getPriority();
    }

    /**
     * 分仓
     * @param warehouseCode
     * @param skuAmount
     */
    public void dispatch(String warehouseCode,Integer skuAmount){
        this.dispatchInfo.put(warehouseCode,skuAmount);
    }

}
