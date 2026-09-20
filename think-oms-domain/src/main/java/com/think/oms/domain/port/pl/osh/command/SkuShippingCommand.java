package com.think.oms.domain.port.pl.osh.command;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.List;

@Data
@Builder
public class SkuShippingCommand {

    private String wmsOrderNo;

    private List<ShippingInfo> shippingInfos;

    @Data
    @Builder
    public static class ShippingInfo{

        private String skuCode;

        private String skuAmount;

        private String expressCode;

        private String expressNo;
    }

    public void validate(){
        Assert.notNull(wmsOrderNo,"orderNo is null !!!");
        //其他验证自行补充
    }

}
