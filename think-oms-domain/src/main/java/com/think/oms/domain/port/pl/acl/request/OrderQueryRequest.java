package com.think.oms.domain.port.pl.acl.request;

import com.think.oms.domain.model.constant.OrderSource;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderQueryRequest {

    private String externalOrderNo;

    private OrderSource orderSource;

    private String orderNo;

}
