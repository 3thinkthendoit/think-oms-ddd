package com.think.oms.osh.controller;

import com.google.common.collect.Maps;
import com.think.oms.domain.port.pl.osh.command.OrderCreateCommand;
import com.think.oms.local.OrderLocalService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * 订单接口 北向网关
 */
@RequestMapping("/api/order/mijia")
@RestController
public class MiJiaController {


    @Resource
    OrderLocalService orderLocalService;

    /**
     * 接受米家订单(小米主动推送)
     * @return
     */
    @RequestMapping("/create")
    public Map<String,Object> create(@RequestBody Map<String,Object> body){
        //解析米家协议 转成 CreateOrderCommand
        OrderCreateCommand command = OrderCreateCommand.builder()
                .build();
        orderLocalService.createOrder(command);
        return Maps.newHashMap();
    }

    /**
     * 订单查询
     * @param body
     * @return
     */
    @RequestMapping("/query")
    public Map<String,Object> query(@RequestBody Map<String,Object> body){

        //orderAppService.query()

        return Maps.newHashMap();
    }
}
