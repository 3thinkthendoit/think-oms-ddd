package com.think.oms.domain.port.pl.osh.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderAssCommand {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 售后单号
     */
    private String assOrderNo;
}
