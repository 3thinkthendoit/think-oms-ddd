package com.think.oms.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.think.oms.domain.model.aggregate.shippingcallback.ShippingCallbackAggregate;
import com.think.oms.domain.pl.request.OrderQueryRequest;
import com.think.oms.domain.pl.request.ShippingCallbackRequest;
import com.think.oms.domain.pl.request.ShippingQueryRequest;
import com.think.oms.domain.pl.response.OrderQueryResponse;
import com.think.oms.domain.pl.response.ShippingCallbackResponse;
import com.think.oms.domain.pl.response.ShippingQueryResponse;
import com.think.oms.domain.port.gateway.OrderFulfillGateway;
import com.think.oms.domain.port.gateway.OrderInfoGateway;
import com.think.oms.domain.port.gateway.ShippingCallbackGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class OrderShippingDomainService {

    @Autowired
    OrderInfoGateway orderInfoGateway;
    @Autowired
    ShippingCallbackGateway shippingCallbackGateway;
    @Autowired
    OrderFulfillGateway orderFulfillGateway;

    public void initBaseInfo(ShippingCallbackAggregate aggregate){
        this.initOrderInfo(aggregate);
        this.initSkuShippingInfo(aggregate);
        //完善其他信息
    }

    /**
     * 订单基本信息
     * @param aggregate
     */
    private void initOrderInfo(ShippingCallbackAggregate aggregate){
        OrderQueryRequest request = OrderQueryRequest.builder()
                .orderNo(aggregate.getOrderId().getOrderNo())
                .build();
        OrderQueryResponse response = orderInfoGateway.query(request);
        if(CollectionUtils.isEmpty(response.getOrders())){
            return;
        }
        aggregate.initBaseInfo(response.getOrders().get(0).getSkuInfos());
    }

    /**
     * sku发货信息
     * @param aggregate
     */
    private void initSkuShippingInfo(ShippingCallbackAggregate aggregate){
        //调用WMS网关获取发货信息
        ShippingQueryRequest request = ShippingQueryRequest.builder().build();
        ShippingQueryResponse response  = orderFulfillGateway.query(request);
        //aggregate.modifyShippingInfo();
    }

    /**
     * 通知上游系统发货回传
     * @param aggregate
     */
    public void shippingCallback(ShippingCallbackAggregate aggregate){
        if(!aggregate.isCallback()){
            log.info("orderNo={},无需发货回传!!!",aggregate.getOrderId().getOrderNo());
        }
        ShippingCallbackRequest request = ShippingCallbackRequest.builder()
                .externalOrderNo(aggregate.getOrderId().getExternalOrderNo())
                .orderSource(aggregate.getOrderId().getOrderSource())
                //.shippingInfos();
                .build();
        ShippingCallbackResponse response = shippingCallbackGateway.callback(request);
        aggregate.handleCallbackResult(response.getCallbackResult());

    }

}
