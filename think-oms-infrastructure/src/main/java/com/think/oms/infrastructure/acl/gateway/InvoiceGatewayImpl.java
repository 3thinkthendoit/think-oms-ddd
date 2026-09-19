package com.think.oms.infrastructure.acl.gateway;

import com.think.oms.domain.port.gateway.InvoiceGateway;
import com.think.oms.infrastructure.core.rockermq.RocketMqClient;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InvoiceGatewayImpl implements InvoiceGateway {

    @Resource
    RocketMqClient rocketMqClient;

    @Override
    public void issue(String orderNo) {

    }
}
