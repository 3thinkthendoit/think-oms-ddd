package com.think.oms.domain.port.gateway;

import com.think.oms.domain.port.pl.acl.request.SkuInfoQueryRequest;
import com.think.oms.domain.port.pl.acl.response.SkuInfoQueryResponse;

public interface SkuInfoQueryGateway {

    public SkuInfoQueryResponse query(SkuInfoQueryRequest request);
}
