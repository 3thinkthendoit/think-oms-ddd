package com.think.oms.osh.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderInfoQueryReq {

    private String orderNo;

    private String externalOrderNo;

    private Integer orderSource;
}
