package com.think.oms.infrastructure.core.rockermq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RocketMqClient  {

    //@Resource
    private RocketMQTemplate rocketMQTemplate;

    public void send(String topic, String message) {
        rocketMQTemplate.convertAndSend(topic, message);
    }

}
