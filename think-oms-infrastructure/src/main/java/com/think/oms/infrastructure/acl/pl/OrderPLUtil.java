package com.think.oms.infrastructure.acl.pl;

import com.google.common.collect.Lists;
import com.think.oms.domain.model.aggregate.create.OrderCreateAggregate;
import com.think.oms.domain.model.aggregate.orderfulfill.OrderFulfillAggregate;
import com.think.oms.domain.port.pl.acl.SkuFullInfo;
import com.think.oms.domain.port.pl.acl.SkuItemInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderBaseInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderSkuInfo;
import com.think.oms.infrastructure.core.mybatis.po.OrderSkuItemInfo;
import java.util.Date;
import java.util.List;

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

    public static OrderFulfillAggregate plToOrderDo(OrderBaseInfo orderBaseInfo, List<OrderSkuItemInfo> orderSkuItemInfos){
        List<SkuItemInfo> skuItemInfos = Lists.newArrayList();
        //OrderSkuItemInfo po 转 SkuItemInfo
        orderSkuItemInfos.forEach(item->{
             SkuItemInfo skuItemInfo = new SkuItemInfo();
             //赋值 自行补充其他属性
            skuItemInfo.setSkuFullInfo(new SkuFullInfo(item.getSkuCode()));
            skuItemInfos.add(skuItemInfo);
        });
        return  new OrderFulfillAggregate(orderBaseInfo.getOrderNo(),orderBaseInfo.getStoreCode(),skuItemInfos);
    }
}
