package com.think.oms.osh.listener;

import com.think.oms.domain.port.pl.osh.event.OrderCreatedEvent;
import com.think.oms.local.OrderLocalService;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 订单创建事件监听
 */
@Component
@Slf4j
public class OrderCreatedListener  {

    @Resource
    OrderLocalService orderLocalService;

    @EventListener(value = OrderCreatedEvent.class)
    @Async
    public void onApplicationEvent(OrderCreatedEvent event) {
        log.info("收到OrderCreatedEvent :orderNo=[{}]",event.getOrderNo());
        try {
            orderLocalService.dispatchOrder(event.getOrderNo());
        }catch (Exception ex){
            log.error(ex.getMessage(),ex);
        }
    }
}
