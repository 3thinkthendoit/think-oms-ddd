package com.think.oms.osh.controller;

import com.google.common.collect.Maps;
import com.think.oms.app.service.OrderAppService;
import com.think.oms.domain.pl.command.CreateOrderCommand;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    OrderAppService orderAppService;

    /**
     * 接受米家订单
     * @return
     */
    @RequestMapping("/create")
    public Map<String,Object> create(@RequestBody Map<String,Object> body){
        //解析米家协议 转成 CreateOrderCommand
        CreateOrderCommand command = CreateOrderCommand.builder()
                .build();
        orderAppService.createOrder(command);
        return Maps.newHashMap();
    }

    /**
     * 订单查询
     * @param body
     * @return
     */
    @RequestMapping("/query")
    public Map<String,Object> query(@RequestBody Map<String,Object> body){

        return Maps.newHashMap();
    }
}