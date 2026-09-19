package com.think.oms.ohs.rpc.impl;

import com.think.oms.app.service.OrderAppService;
import com.think.oms.domain.pl.OrderInfo;
import com.think.oms.domain.pl.query.OrderInfoQuery;
import com.think.oms.ohs.dto.req.OrderInfoQueryReq;
import com.think.oms.ohs.dto.resp.OrderInfoQueryResp;
import com.think.oms.ohs.rpc.OrderInfoIface;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class OrderInfoIfaceImpl implements OrderInfoIface {

    @Resource
    OrderAppService orderAppService;

    @Override
    public OrderInfoQueryResp query(OrderInfoQueryReq req) {
        //PL dto->bo
        OrderInfoQuery query = OrderInfoQuery.builder()
                .externalOrderNo(req.getExternalOrderNo())
                .orderSource(req.getOrderSource())
                .orderNo(req.getOrderNo())
                .build();
        List<OrderInfo> list = orderAppService.query(query);
        //bo->dto OrderInfo转 OrderInfoQueryResp
        return OrderInfoQueryResp.builder()
//                .skuInfos()
//                .skuItemInfos()
//                .externalOrderNo()
//                .orderNo()
//                .orderSource()
//                .orderStatus()
                .build();
    }
}
