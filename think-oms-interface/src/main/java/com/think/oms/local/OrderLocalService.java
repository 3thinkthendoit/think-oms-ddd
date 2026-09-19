package com.think.oms.local;

import com.think.oms.app.service.OrderAppService;
import com.think.oms.domain.pl.OrderInfo;
import com.think.oms.domain.pl.command.OrderAssCommand;
import com.think.oms.domain.pl.command.OrderCreateCommand;
import com.think.oms.domain.pl.command.SkuShippingCommand;
import com.think.oms.domain.pl.query.OrderInfoQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderLocalService {

    @Resource
    OrderAppService orderAppService;

    public void createOrder(OrderCreateCommand command) {
        orderAppService.createOrder(command);
    }

    public void dispatchOrder(String orderNo) {
        orderAppService.dispatchOrder(orderNo);
    }

    public void fulfillOrder(String orderNo) {
        orderAppService.fulfillOrder(orderNo);
    }

    public void shippingCallback(SkuShippingCommand command) {
        orderAppService.shippingCallback(command);
    }

    public void orderAfterSaleService(OrderAssCommand command) {
        orderAppService.orderAfterSaleService(command);
    }

    public List<OrderInfo> query(OrderInfoQuery query){
         return orderAppService.query(query);
    }
}
