package com.think.oms.domain.model.aggregate.create;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.think.oms.domain.model.constant.*;
import com.think.oms.domain.model.dp.OrderId;
import com.think.oms.domain.model.valueobject.StoreInfo;
import com.think.oms.domain.model.valueobject.UserInfo;
import com.think.oms.domain.port.pl.acl.SkuFullInfo;
import com.think.oms.domain.port.pl.osh.command.OrderCreateCommand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 订单聚合
 */
@Getter
@Slf4j
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
     * 订单备注
     */
    private String desc;

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
     * Sku下单信息
     */
    private List<OrderSku> skuInfos;

    /**
     * 下单用户信息
     */
    private UserInfo buyer;

    /**
     * 支付信息
     */
    private OrderPay orderPay;

    /**
     * 收货地址
     */
    public ShippingAddress shippingAddress;

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
        aggregate.orderTitle = command.getOrderTitle();
        aggregate.shippingAddress = new ShippingAddress(command.getRecipient(),command.getMobile(),command.getAddress());
        aggregate.attachInfos = command.getAttachInfos();
        aggregate.buyer = new UserInfo(command.getUserId(),command.getUserName(),command.getUserType());
        aggregate.invoiceInfo = new OrderInvoice(command.getInvoiceName(),command.getInvoiceDetails());
        aggregate.orderType = command.getOrderType();
        aggregate.storeInfo = new StoreInfo(command.getStoreCode());
        List<OrderSku> skuInfos = Lists.newArrayList();
        //支付信息
        aggregate.orderPay = new OrderPay(command.getCurrency(),command.getPayType(), command.getOrderPrice(),
                command.getDiscountPrice(),command.getFeeAmountMap());
        //sku 转 domain sku
        command.getOrderSkuInfos().forEach(orderSkuInfo -> {
            skuInfos.add(new OrderSku(orderSkuInfo));
        });
        aggregate.skuInfos = skuInfos;
        aggregate.orderStatus = command.getOrderStatus();

    }

    /**
     * 订单检查(领域方法-高内聚)
     */
    public void check(){
         //订单状态 未支付的不允许接入
        Assert.isTrue(OrderStatus.PAYED.getCode() != orderStatus.getCode(),
                String.format("orderNo={},orderStatus={},未支付不允许接入!!!"));
        //领域限制条件在此编写
        Assert.isNull(this.invoiceInfo,"发票信息不正确!!!");
        Assert.isNull(this.buyer.getUserId(),"用户信息不正确!!!");
        Assert.isNull(this.shippingAddress.getAddressCode(),"下单地址信息不正确!!!");
    }

    /**
     * sku金额拆分计算(领域行为)
     */
    public void  priceCalculate(){
        //优惠金额，运费 等附加费用 根据 sku下单金额权重比例进行金额拆分
        this.skuItems = Lists.newArrayList();
        this.skuInfos.forEach(skuInfo -> {
            //拆组合商品 计算子sku需要均摊的金额，附加费用
            Map<FeeType,Long> feeAmountInfos = Maps.newHashMap();
            //feeAmountInfos.put(FeeType.TRAN_FEE,)
            //feeAmountInfos.put(FeeType.DISCOUNT_FEE,)
            OrderSkuItem skuItem = new OrderSkuItem(skuInfo.getSkuInfo().getSkuCode(),
                    skuInfo.getSkuBuyAmount(),skuInfo.getSkuPayPrice(),feeAmountInfos);
            this.skuItems.add(skuItem);
        });
    }

    /**
     * 优先级处理
     */
    public void priorityProcessing(){
        if(CollectionUtils.isEmpty(this.skuItems)){
            return;
        }
        Map<String,OrderSku> skuMap = Maps.newHashMap();
        //子商品也需要转换,这里省略
        this.skuInfos.forEach(skuInfo->{skuMap.put(skuInfo.getSkuInfo().getSkuCode(),skuInfo);});
        this.skuItems.forEach(skuItem->{
            OrderSku orderSku = skuMap.get(skuItem.getSkuCode());
            Assert.notNull(orderSku,String.format("skuCode=%s查询不到sku信息",skuItem.getSkuCode()));
             int priority = 1;
             //生鲜优先发货
             if(SkuCategory.FRUSH_FOOD == orderSku.getSkuCategory()){
                 priority = 3;
             }
             //虚拟订单无需发货
             if(SkuType.VIRTUAL == orderSku.getSkuType()){
                 priority = 0;
             }
             skuItem.priorityProcessing(priority);
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
     * 挂起订单
     * @param desc
     */
    public void hangup(String desc){
        this.orderStatus = OrderStatus.HANG_UP;
        this.desc = desc;
    }


}
