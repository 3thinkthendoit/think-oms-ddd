package com.think.oms.infrastructure.acl.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.think.oms.domain.model.aggregate.orderfulfill.OrderFulfillAggregate;
import com.think.oms.domain.model.constant.OrderStatus;
import com.think.oms.domain.pl.FulfillOrderInfo;
import com.think.oms.domain.port.repository.OrderFulfillRepository;
import com.think.oms.infrastructure.acl.pl.OrderPLUtil;
import com.think.oms.infrastructure.core.mybatis.mapper.OrderInfoMapper;
import com.think.oms.infrastructure.core.mybatis.mapper.OrderSkuInfoMapper;
import com.think.oms.infrastructure.core.mybatis.mapper.OrderSkuItemInfoMapper;
import com.think.oms.infrastructure.core.mybatis.po.OrderBaseInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderSkuItemInfo;
import jakarta.annotation.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import java.util.List;

public class OrderFulfillRepositoryImpl implements OrderFulfillRepository {

    @Resource
    OrderInfoMapper orderInfoMapper;
    @Resource
    OrderSkuInfoMapper orderSkuInfoMapper;
    @Resource
    OrderSkuItemInfoMapper orderSkuItemInfoMapper;

    /**
     * 还原履约订单聚合根
     * @param orderNo
     * @return
     */
    @Override
    public OrderFulfillAggregate ofByOrderNo(String orderNo) {
        Assert.notNull(orderNo,"orderNo is null!!!");
        LambdaQueryWrapper<OrderBaseInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderBaseInfo::getOrderNo,orderNo);
        queryWrapper.in(OrderBaseInfo::getOrderStatus, OrderStatus.PAYED);
        List<OrderBaseInfo> orderList =  orderInfoMapper.selectList(queryWrapper);
        Assert.isTrue(!CollectionUtils.isEmpty(orderList),String.format("根据orderNo=[{}]查询不到需要履约订单信息!!!",orderNo));
        //按业务需要查询orderSkuItem
        List<OrderSkuItemInfo> orderSkuItemInfoList = Lists.newArrayList();
        return OrderPLUtil.plToOrderDo(orderList.get(0),orderSkuItemInfoList);
    }

    /**
     * 持久化履约聚合
     * @param aggregate
     */
    @Override
    public void save(OrderFulfillAggregate aggregate) {
        Assert.notNull(aggregate,"aggregate is null!!!");
        aggregate.getSplitOrders().forEach((k,v)->{
            //DO转PO持久化
        });
    }

    @Override
    public void updateOrderFulfill(String orderNo) {
        Assert.notNull(orderNo,"orderNo is null!!!");
        //订单状态改为已发货
    }

    /**
     * 更新 omsOrderNo 对应的履约状态
     * @param omsOrderNo
     * @return
     */
    @Override
    public List<FulfillOrderInfo> queryFulfillOrderInfos(String omsOrderNo) {
        return null;
    }

}
