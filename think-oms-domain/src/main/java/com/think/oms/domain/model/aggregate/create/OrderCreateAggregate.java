package com.think.oms.domain.model.aggregate.create;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.think.oms.domain.model.constant.FeeType;
import com.think.oms.domain.model.constant.OrderStatus;
import com.think.oms.domain.model.constant.OrderType;
import com.think.oms.domain.model.dp.OrderId;
import com.think.oms.domain.model.valueobject.StoreInfo;
import com.think.oms.domain.model.valueobject.UserInfo;
import com.think.oms.domain.pl.SkuFullInfo;
import com.think.oms.domain.pl.command.OrderCreateCommand;
import lombok.Getter;
import org.springframework.util.Assert;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 订单聚合
 */
@Getter
public class OrderCreateAggregate {

    /**
     * 订单ID
     */
    private OrderId orderId;

    /**
     * 订单状态
     */
    private OrderStatus orderStatus;

    /**
     * 订单类型
     */
    private OrderType orderType;

    /**
     * 店铺信息
     */
    private StoreInfo storeInfo;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 订单金额 单位分
     */
    private Long orderPrice;

    /**
     * Sku下单信息
     */
    private List<OrderSku> skuInfos;

    /**
     * 下单用户信息
     */
    private UserInfo buyer;


    /**
     * 收货地址
     */
    private String address;


    /**
     * 发票信息
     */
    private OrderInvoice invoiceInfo;

    /**
     * sku item级别下单信息
     */
    private List<OrderSkuItem> skuItems;


    /**
     * 附加信息
     */
    private Map<String,Object> attachInfos;


    /**
     * 库存扣减
     */
    private boolean deductInventory;

    public static OrderCreateAggregate create(OrderCreateCommand command){
        command.validate();
        OrderCreateAggregate aggregate = new OrderCreateAggregate();
        baseCreate(aggregate,command);
        return  aggregate;
    }


    public static OrderCreateAggregate create(OrderCreateCommand command, List<OrderSkuItem> skuItems){
        command.validate();
        OrderCreateAggregate aggregate = new OrderCreateAggregate();
        baseCreate(aggregate,command);
        aggregate.skuItems = skuItems;
        return  aggregate;
    }

    private static void baseCreate(OrderCreateAggregate aggregate, OrderCreateCommand command){
        aggregate.orderId = new OrderId(command.getExternalOrderNo(),command.getOrderSource());
        aggregate.orderPrice = command.getOrderPrice();
        aggregate.orderTitle = command.getOrderTitle();
        aggregate.address = command.getAddress();
        aggregate.attachInfos = command.getAttachInfos();
        aggregate.buyer = new UserInfo(command.getUserId(),command.getUserName(),command.getUserType());
        aggregate.invoiceInfo = new OrderInvoice(command.getInvoiceName(),command.getInvoiceDetails());
        aggregate.orderType = command.getOrderType();
        aggregate.storeInfo = new StoreInfo(command.getStoreCode());
        List<OrderSku> skuInfos = Lists.newArrayList();
        //sku 转 domain sku
        command.getOrderSkuInfos().forEach(orderSkuInfo -> {
            skuInfos.add(new OrderSku(orderSkuInfo));
        });
        aggregate.skuInfos = skuInfos;
        aggregate.orderStatus = command.getOrderStatus();
        aggregate.deductInventory = false;
    }

    /**
     * 订单检查(领域方法-高内聚)
     */
    public void check(){
         //订单状态 未支付的不允许接入
        Assert.isTrue(OrderStatus.PAYED.getCode() != orderStatus.getCode(),
                String.format("orderNo={},orderStatus={},未支付不允许接入!!!"));
        //领域限制条件在此编写
    }

    /**
     * sku金额拆分计算(领域方法)
     */
    public void  priceCalculate(){
        //优惠金额，运费 等附加费用 根据 sku下单金额权重比例进行金额拆分
        this.skuItems = Lists.newArrayList();
        this.skuInfos.forEach(skuInfo -> {
            //拆组合商品 计算子sku需要均摊的金额，附加费用
            Map<FeeType,Long> feeAmountInfos = Maps.newHashMap();
            //feeAmountInfos.put(FeeType.TRAN_FEE,)
            //feeAmountInfos.put(FeeType.DISCOUNT_FEE,)
            OrderSkuItem skuItem = new OrderSkuItem(skuInfo.getSkuInfo().getSkuId(),skuInfo.getSkuInfo().getSkuCode(),
                    skuInfo.getSkuBuyAmount(),skuInfo.getSkuPayPrice(),feeAmountInfos);
            this.skuItems.add(skuItem);
        });
    }


    /**
     * 完善sku下单信息
     * 比如外部sku转内部sku,sku 是否组合
     * @param skuInfoMap
     */
    public void modifyOrderSku(Map<String, SkuFullInfo> skuInfoMap){
        this.getSkuInfos().forEach(orderSku -> {
            //完善信息 调用OrderSku 领域方法
            SkuFullInfo skuFullInfo = skuInfoMap.get(orderSku.getSkuInfo().getExternalSkuId());
            if(Objects.isNull(skuFullInfo)){
                Assert.isTrue(false,String.format("根据外部skuIds=[%s]查询不到商品信息!!!",orderSku.getSkuInfo().getExternalSkuId()));
                return;
            }
            orderSku.modifySku(skuFullInfo);
        });
    }

    /**
     * 库存扣减成功
     */
    public void deductInventorySuccess(){
        this.deductInventory = true;
    }
}