package com.think.oms.infrastructure.acl.gateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.think.oms.domain.port.pl.acl.OrderInfo;
import com.think.oms.domain.port.pl.acl.request.OrderQueryRequest;
import com.think.oms.domain.port.pl.acl.response.OrderQueryResponse;
import com.think.oms.domain.port.gateway.OrderInfoGateway;
import com.think.oms.domain.port.gateway.SkuInfoQueryGateway;
import com.think.oms.infrastructure.core.mybatis.mapper.OrderInfoMapper;
import com.think.oms.infrastructure.core.mybatis.po.OrderBaseInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import java.util.List;


/**
 * 订单南向网关
 */
@Component
public class OrderInfoGatewayImpl implements OrderInfoGateway {

    @Resource
    OrderInfoMapper orderInfoMapper;
    @Resource
    SkuInfoQueryGateway skuInfoQueryGateway;

    /**
     * 订单信息查询
     * @param request
     * @return
     */
    public OrderQueryResponse query(OrderQueryRequest request){
        //选择具体的技术栈实现订单查询，这里选择mybatis
        LambdaQueryWrapper<OrderBaseInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderBaseInfo::getExternalOrderNo,request.getExternalOrderNo());
        queryWrapper.eq(OrderBaseInfo::getOrderSource,request.getOrderSource().getCode());
        List<OrderBaseInfo> orderList =  orderInfoMapper.selectList(queryWrapper);
        //skuInfoQueryGateway.query()
        //PL 转换
        List<OrderInfo> orders = Lists.newArrayList();
        orderList.forEach(orderBaseInfo -> {
            //orders.add(OrderPLUtil.plToOrderInfo)
        });
        return OrderQueryResponse.builder()
                .orders(orders)
                .build();
    }

}
