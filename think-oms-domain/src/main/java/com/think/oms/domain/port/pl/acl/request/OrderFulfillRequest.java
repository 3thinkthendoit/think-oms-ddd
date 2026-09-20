package com.think.oms.domain.port.pl.acl.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderFulfillRequest {

    /**
     * 发货单号(拆单后父单号)
     */
    private String omsOrderNo;

    /**
     * 原订单号
     */
    private String orderNo;

    /**
     * 仓库编码
     */
    private String warehouseCode;

    @Data
    @Builder
    public static class FulfillSku{

        private String skuCode;

        private Integer skuAmount;
    }
}
