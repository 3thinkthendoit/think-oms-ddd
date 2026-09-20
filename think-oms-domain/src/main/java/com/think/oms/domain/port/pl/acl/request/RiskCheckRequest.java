package com.think.oms.domain.port.pl.acl.request;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RiskCheckRequest {

    /**
     * 下单用户
     */
    private String username;

    /**
     * 下单地址
     */
    private String address;

    /**
     * 下单手机号
     */
    private String phoneNo;

    private List<SkuInfo> skuInfos;

    public static class SkuInfo{

        private String skuCode;

        private String skuAmount;

        private String skuPrice;
    }

}
