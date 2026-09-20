package com.think.oms.domain.port.pl.acl.response;

import com.think.oms.domain.port.pl.acl.OrderInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderQueryResponse {

    private List<OrderInfo> orders;
}
