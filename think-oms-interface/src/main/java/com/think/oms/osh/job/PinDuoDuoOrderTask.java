package com.think.oms.osh.job;

import com.think.oms.domain.port.pl.osh.command.OrderCreateCommand;
import com.think.oms.infrastructure.acl.api.pdd.PinduoduoClient;
import com.think.oms.local.OrderLocalService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 拼多多定时任务拉单(北向网关)
 */
@Component
public class PinDuoDuoOrderTask {

    @Resource
    OrderLocalService orderLocalService;
    @Resource
    PinduoduoClient pinduoduoClient;

    @Scheduled(fixedRate = 1000L*60*5)
    public void pullOrder() {
        //调用pdd api接口 拉取订单 转成 CreateOrderCommand
        Date end = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date begin = new Date();
        OrderCreateCommand command = pinduoduoClient.pullOrder(begin,end);
        orderLocalService.createOrder(command);
    }
}
