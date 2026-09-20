package com.think.oms.domain.port.gateway;

import com.think.oms.domain.port.pl.acl.request.ShippingCallbackRequest;
import com.think.oms.domain.port.pl.acl.response.ShippingCallbackResponse;

public interface ShippingCallbackGateway {

    public ShippingCallbackResponse callback(ShippingCallbackRequest request);
}
