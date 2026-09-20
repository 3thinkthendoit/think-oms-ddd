package com.think.oms.domain.port.pl.acl;

import com.think.oms.domain.model.constant.OrderSource;
import com.think.oms.domain.model.constant.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderInfo {

    private String orderNo;

    private String externalOrderNo;

    private OrderSource orderSource;

    private OrderStatus orderStatus;

    public List<SkuItemInfo> skuInfos;

}
