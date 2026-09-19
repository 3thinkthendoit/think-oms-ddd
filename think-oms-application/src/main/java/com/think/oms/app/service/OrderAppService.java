package com.think.oms.app.service;

import com.think.oms.domain.model.aggregate.create.OrderCreateAggregate;
import com.think.oms.domain.model.aggregate.orderfulfill.OrderFulfillAggregate;
import com.think.oms.domain.model.aggregate.shippingcallback.ShippingCallbackAggregate;
import com.think.oms.domain.model.constant.OrderSource;
import com.think.oms.domain.pl.FulfillOrderInfo;
import com.think.oms.domain.pl.OrderInfo;
import com.think.oms.domain.pl.command.OrderAssCommand;
import com.think.oms.domain.pl.command.OrderCreateCommand;
import com.think.oms.domain.pl.command.SkuShippingCommand;
import com.think.oms.domain.pl.event.OrderCreatedEvent;
import com.think.oms.domain.pl.event.OrderFulfillEvent;
import com.think.oms.domain.pl.query.OrderInfoQuery;
import com.think.oms.domain.pl.request.OrderFulfillRequest;
import com.think.oms.domain.pl.request.OrderQueryRequest;
import com.think.oms.domain.pl.response.OrderFulfillResponse;
import com.think.oms.domain.port.gateway.OrderFulfillGateway;
import com.think.oms.domain.port.gateway.OrderInfoGateway;
import com.think.oms.domain.port.publisher.OrderEventPublisher;
import com.think.oms.domain.port.repository.OrderCreateRepository;
import com.think.oms.domain.port.repository.OrderFulfillRepository;
import com.think.oms.domain.port.repository.SkuShippingRepository;
import com.think.oms.domain.service.OrderCreateDomainService;
import com.think.oms.domain.service.OrderFulfillDomainService;
import com.think.oms.domain.service.OrderShippingDomainService;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 订单服务APP层 CQRS 模式
 */
@Slf4j
@Service
public class OrderAppService {

    @Resource
    OrderCreateDomainService orderCreateDomainService;
    @Resource
    OrderCreateRepository orderCreateRepository;
    @Resource
    OrderFulfillRepository orderFulfillRepository;
    @Resource
    OrderEventPublisher orderEventPublisher;
    @Resource
    OrderFulfillDomainService orderFulfillDomainService;
    @Resource
    OrderShippingDomainService orderShippingDomainService;
    @Resource
    OrderInfoGateway orderInfoGateway;
    @Resource
    OrderFulfillGateway orderFulfillGateway;
    @Resource
    SkuShippingRepository shippingRepository;

    /**
     * 统一创建订单(流程编排-低耦合)
     * @param command
     */
    public void createOrder(OrderCreateCommand command){
        OrderCreateAggregate aggregate = OrderCreateAggregate.create(command);
        orderCreateDomainService.isExist(aggregate);
        orderCreateDomainService.initBaseInfo(aggregate);
        orderCreateDomainService.audit(aggregate);
        aggregate.priceCalculate();
        aggregate.priorityProcessing();
        orderCreateRepository.save(aggregate);
        orderEventPublisher.publish(new OrderCreatedEvent(aggregate.getOrderId().getOrderNo()));
    }

    /**
     * 订单分仓、拆单
     * @param orderNo
     */
    public void dispatchOrder(String orderNo){
        OrderFulfillAggregate aggregate = orderFulfillRepository.ofByOrderNo(orderNo);
        orderFulfillDomainService.initBaseInfo(aggregate);
        aggregate.check();
        aggregate.dispatch();
        aggregate.split();
        orderFulfillRepository.save(aggregate);
        orderEventPublisher.publish(new OrderFulfillEvent(orderNo));
    }

    /**
     * 推WMS履约(可做业务补偿)
     * 业务逻辑比较薄，可以直接绕过领域层逻辑直接操作南向网关
     * @param orderNo
     */
    public void fulfillOrder(String orderNo){
        List<FulfillOrderInfo> list = orderFulfillRepository.queryFulfillOrderInfos(orderNo);
        list.forEach(order->{
            OrderFulfillRequest request = OrderFulfillRequest.builder()
                    .warehouseCode(order.getWarehouseCode())
                    .omsOrderNo(order.getOmsOrderNo())
                    .build();
            OrderFulfillResponse response =  orderFulfillGateway.fulfill(request);
            if(response.isFulfill()){
                orderFulfillRepository.updateOrderFulfill(order.getOmsOrderNo());
            }
        });
    }

    /**
     * 订单发货回传处理
     * @param command
     */
    public void shippingCallback(SkuShippingCommand command){
        ShippingCallbackAggregate aggregate = ShippingCallbackAggregate.create(command);
        orderShippingDomainService.initBaseInfo(aggregate);
        aggregate.check();
        shippingRepository.save(aggregate);
        orderShippingDomainService.shippingCallback(aggregate);
    }


    /**
     * 订单售后
     * @param command
     */
    public void orderAfterSaleService(OrderAssCommand command){

    }

    /**
     * 查询订单信息
     * 查询类query可以直接绕过dmmin层 调用南向网关
     * @param query
     * @return
     */
    public List<OrderInfo> query(OrderInfoQuery query){
        OrderQueryRequest request = OrderQueryRequest
                .builder()
                .orderNo(query.getOrderNo())
                .orderSource(OrderSource.ofByCode(query.getOrderSource()))
                .externalOrderNo(query.getExternalOrderNo())
                .build();
        return orderInfoGateway.query(request).getOrders();
    }

}
