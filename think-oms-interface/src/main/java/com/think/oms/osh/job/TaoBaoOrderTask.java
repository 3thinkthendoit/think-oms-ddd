package com.think.oms.osh.job;

import com.think.oms.domain.port.pl.osh.command.OrderCreateCommand;
import com.think.oms.infrastructure.acl.api.taobao.TaoBaoClient;
import com.think.oms.local.OrderLocalService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class TaoBaoOrderTask {

    @Resource
    OrderLocalService orderLocalService;
    @Resource
    TaoBaoClient taoBaoClient;

    @Scheduled(fixedRate = 1000L*60*5)
    public void pullOrder() {
        //淘宝 api接口 拉取订单 转成 CreateOrderCommand
        Date end = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date begin = new Date();
        OrderCreateCommand command = taoBaoClient.pullOrder(begin,end);
        orderLocalService.createOrder(command);
    }
}
