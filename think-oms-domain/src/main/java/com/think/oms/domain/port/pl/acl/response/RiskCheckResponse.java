package com.think.oms.domain.port.pl.acl.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RiskCheckResponse {

    /**
     * 批量下单
     */
    private boolean isBatchBuy;

    /**
     * 错误收货信息
     */
    private boolean isIllegalAddress;

    /**
     * 价格欺诈
     */
    private boolean isIllegalSkuPrice;

    /**
     * 支付欺诈
     */
    private boolean isIllegalPay;

    /**
     * 错误信息
     */
    private String desc;
}
