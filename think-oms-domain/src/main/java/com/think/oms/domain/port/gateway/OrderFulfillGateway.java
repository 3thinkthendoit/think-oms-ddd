package com.think.oms.domain.port.gateway;

import com.think.oms.domain.port.pl.acl.request.OrderFulfillRequest;
import com.think.oms.domain.port.pl.acl.request.ShippingQueryRequest;
import com.think.oms.domain.port.pl.acl.response.OrderFulfillResponse;
import com.think.oms.domain.port.pl.acl.response.ShippingQueryResponse;

public interface OrderFulfillGateway {

    public OrderFulfillResponse fulfill(OrderFulfillRequest request);

    public ShippingQueryResponse query(ShippingQueryRequest request);
}
