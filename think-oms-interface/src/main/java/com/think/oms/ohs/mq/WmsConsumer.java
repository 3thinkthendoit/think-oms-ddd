package com.think.oms.ohs.mq;

import com.alibaba.fastjson.JSONObject;
import com.think.oms.app.service.OrderAppService;
import com.think.oms.domain.pl.command.SkuShippingCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 监听订单履约系统 发货信息
 */
@Component
@Slf4j
//@RocketMQMessageListener(topic = "order-fulfillment-center", consumerGroup = "order-fulfillment-center-group")
public class WmsConsumer implements RocketMQListener<String> {

    @Resource
    OrderAppService orderAppService;

    @Override
    public void onMessage(String msg) {
       try {
           log.info("收到wms 发货信息msg={}",msg);
           //解析msg
           JSONObject json = JSONObject.parseObject(msg);
           SkuShippingCommand command = SkuShippingCommand.builder()
                   .wmsOrderNo(json.getString("orderNo"))
                   .build();
           orderAppService.shippingCallback(command);
       }catch (Exception ex){
            log.error(ex.getMessage(),ex);
       }
    }
}
