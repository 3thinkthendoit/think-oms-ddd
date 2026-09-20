package com.think.oms.osh.dto.resp;

import com.think.oms.domain.model.constant.OrderSource;
import com.think.oms.domain.model.constant.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderInfoQueryResp {

    private String orderNo;

    private String externalOrderNo;

    private OrderSource orderSource;

    private OrderStatus orderStatus;

    //后续属性 自行补充

    /**
     * sku下单信息
     */
    private List<SkuInfo> skuInfos;

    /**
     * sku 下单 发货 售后 确认签收信息(item级别)
     */
    private List<SkuItemInfo> skuItemInfos;

    public static class SkuInfo{

    }

    public static class SkuItemInfo{

    }

}
