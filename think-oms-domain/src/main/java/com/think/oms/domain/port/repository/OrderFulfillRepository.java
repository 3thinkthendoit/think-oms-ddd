package com.think.oms.domain.port.repository;

import com.think.oms.domain.model.aggregate.orderfulfill.OrderFulfillAggregate;
import com.think.oms.domain.port.pl.acl.FulfillOrderInfo;

import java.util.List;

public interface OrderFulfillRepository {

    public OrderFulfillAggregate ofByOrderNo(String orderNo);

    public void save(OrderFulfillAggregate aggregate);


    public void updateOrderFulfill(String orderNo);

    public List<FulfillOrderInfo> queryFulfillOrderInfos(String omsOrderNo);
}
