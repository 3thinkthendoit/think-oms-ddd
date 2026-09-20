package com.think.oms.domain.port.gateway;

import com.think.oms.domain.port.pl.acl.request.RiskCheckRequest;
import com.think.oms.domain.port.pl.acl.response.RiskCheckResponse;

public interface RiskCheckGateway {

    public RiskCheckResponse check(RiskCheckRequest request);
}
