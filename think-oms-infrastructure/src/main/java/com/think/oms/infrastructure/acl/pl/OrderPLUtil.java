package com.think.oms.infrastructure.acl.pl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.think.oms.domain.model.aggregate.create.OrderCreateAggregate;
import com.think.oms.domain.model.constant.FeeType;
import com.think.oms.domain.model.aggregate.create.OrderSkuItem;
import com.think.oms.domain.pl.command.OrderCreateCommand;
import com.think.oms.infrastructure.core.mybatis.po.OrderBaseInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderSkuInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderSkuItemInfo;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 南向网关PL转换
 */
public class OrderPLUtil {

    /**
     * 聚合根转PO
     * @param aggregate
     * @return
     */
    public static OrderBaseInfo plToOrderPo(OrderCreateAggregate aggregate){
        OrderBaseInfo order =  OrderBaseInfo.builder()
                .createTime(new Date())
                .updateTime(new Date())
                .externalOrderNo(aggregate.getOrderId().getExternalOrderNo())
                .orderNo(aggregate.getOrderId().getOrderNo())
                .orderPrice(aggregate.getOrderPay().getPayAmount())
                .orderTitle(aggregate.getOrderTitle())
                .build();
        return order;
    }

    public static List<OrderSkuInfo> plToOrderSkuPo(OrderCreateAggregate aggregate){
        List<OrderSkuInfo> list = Lists.newArrayList();
        aggregate.getSkuInfos().forEach(skuInfo -> {
            OrderSkuInfo orderSku = OrderSkuInfo.builder()
                    .build();
            list.add(orderSku);
        });
        return list;
    }

    public static List<OrderSkuItemInfo> plToOrderSkuItemPo(OrderCreateAggregate aggregate){
        List<OrderSkuItemInfo> list = Lists.newArrayList();
        aggregate.getSkuInfos().forEach(skuInfo -> {
            OrderSkuItemInfo orderSkuItem = OrderSkuItemInfo.builder()
                    .build();
            list.add(orderSkuItem);
        });
        return list;
    }

    public static OrderCreateAggregate plToOrderDo(OrderBaseInfo orderBaseInfo, List<OrderSkuInfo> orderSkuInfos,
                                                   List<OrderSkuItemInfo> skuItemInfos){
        OrderCreateCommand command = OrderCreateCommand.builder()
                .orderPrice(orderBaseInfo.getOrderPrice())
                .orderTitle(orderBaseInfo.getOrderTitle())
                //后续属性 自行还原
                .build();
        List<OrderSkuItem> skuItems = Lists.newArrayList();
        //skuItem po 转 domain
        skuItemInfos.forEach(item->{
            Map<FeeType,Long> feeAmountInfos = Maps.newHashMap();
            skuItems.add(new OrderSkuItem(item.getSkuCode(),item.getSkuAmount(),item.getPayPrice(),feeAmountInfos));
        });
        return OrderCreateAggregate.create(command,skuItems);
    }
}
