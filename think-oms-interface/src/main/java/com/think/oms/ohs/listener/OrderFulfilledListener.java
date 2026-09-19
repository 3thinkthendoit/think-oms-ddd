package com.think.oms.ohs.listener;

import com.think.oms.domain.pl.event.OrderFulfillEvent;
import com.think.oms.local.OrderLocalService;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 订单履约事件监听
 */
@Component
@Slf4j
public class OrderFulfilledListener {

    @Resource
    OrderLocalService orderLocalService;


    @EventListener(value = OrderFulfillEvent.class)
    @Async
    public void onApplicationEvent(OrderFulfillEvent event) {
        log.info("OrderFulfilledEvent :orderNo=[{}]",event.getOrderNo());
        try {
            orderLocalService.fulfillOrder(event.getOrderNo());
        }catch (Exception ex){
            log.error(ex.getMessage(),ex);
        }
    }
}
