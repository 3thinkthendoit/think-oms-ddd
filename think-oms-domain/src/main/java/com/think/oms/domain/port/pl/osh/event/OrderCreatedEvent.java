package com.think.oms.domain.port.pl.osh.event;

import lombok.Data;

@Data
public class OrderCreatedEvent extends OrderOperationEvent{

    public OrderCreatedEvent(String orderNo){
        this.setOrderNo(orderNo);
        this.setPublishType(PublishType.LOCAL);
    }
}
