package com.think.oms.infrastructure.acl.repository;

import com.alibaba.fastjson.JSONObject;
import com.think.oms.domain.model.aggregate.create.OrderCreateAggregate;
import com.think.oms.domain.model.aggregate.shippingcallback.ShippingCallbackAggregate;
import com.think.oms.domain.port.repository.OrderCreateRepository;
import com.think.oms.infrastructure.acl.pl.OrderPLUtil;
import com.think.oms.infrastructure.core.mybatis.mapper.OrderInfoMapper;
import com.think.oms.infrastructure.core.mybatis.mapper.OrderSkuInfoMapper;
import com.think.oms.infrastructure.core.mybatis.mapper.OrderSkuItemInfoMapper;
import com.think.oms.infrastructure.core.mybatis.po.OrderBaseInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderSkuInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderSkuItemInfo;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Slf4j
public class OrderCreateRepositoryImpl implements OrderCreateRepository {

    @Resource
    OrderInfoMapper orderInfoMapper;
    @Resource
    OrderSkuInfoMapper orderSkuInfoMapper;
    @Resource
    OrderSkuItemInfoMapper orderSkuItemInfoMapper;

    /**
     * 持久化订单创建聚合
     * @param aggregate
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(OrderCreateAggregate aggregate){
        OrderBaseInfo orderBaseInfo = OrderPLUtil.plToOrderPo(aggregate);
        List<OrderSkuInfo> orderSkuInfoList = OrderPLUtil.plToOrderSkuPo(aggregate);
        List<OrderSkuItemInfo> orderSkuItemInfoList = OrderPLUtil.plToOrderSkuItemPo(aggregate);
        orderInfoMapper.insert(orderBaseInfo);
        orderSkuInfoMapper.batchInsert(orderSkuInfoList);
        orderSkuItemInfoMapper.batchInsert(orderSkuItemInfoList);
        log.info("save order info success !!! orderInfo={},orderSku={},orderSkuItem={}", JSONObject.toJSONString(orderBaseInfo),
                JSONObject.toJSONString(orderSkuInfoList),JSONObject.toJSONString(orderSkuItemInfoList));
    }


    /**
     * 更新订单履约聚合
     * @param aggregate
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ShippingCallbackAggregate aggregate) {
        //更新主订单信息

        //更新sku发货信息

        //新增发货回传记录
    }

}
