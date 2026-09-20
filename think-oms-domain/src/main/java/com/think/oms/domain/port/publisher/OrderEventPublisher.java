package com.think.oms.domain.port.publisher;

import com.think.oms.domain.port.pl.osh.event.OrderOperationEvent;

public interface OrderEventPublisher {

    public void publish(OrderOperationEvent event);
}
