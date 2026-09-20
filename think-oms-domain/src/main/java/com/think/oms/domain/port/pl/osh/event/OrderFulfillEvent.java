package com.think.oms.domain.port.pl.osh.event;

import lombok.Data;

@Data
public class OrderFulfillEvent extends OrderOperationEvent{

    public OrderFulfillEvent(String orderNo){
        this.setOrderNo(orderNo);
        this.setPublishType(PublishType.LOCAL);
    }
}
