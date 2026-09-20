package com.think.oms.domain.model.aggregate.orderfulfill;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.think.oms.domain.port.pl.acl.SkuItemInfo;
import com.think.oms.domain.port.pl.acl.WarehouseInfo;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 订单履约聚合
 */
@Getter
public class OrderFulfillAggregate {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 店铺编码
     */
    private String storeCode;

    /**
     * 订单下单skuItemMap
     */
    private List<FulfillSkuItem> fulfillSkuItems;


    /**
     * sku 仓库库存信息
     */
    private List<FulfillWarehouse> fulfillWarehouses;

    /**
     * SKU-仓库映射关系
     */
    private Map<String,String> skuMappingWarehouseMap;

    /**
     *店铺-仓库映射关系
     */
    private Map<String,String> storeMappingWarehouseMap;


    /**
     * 拆单拆单结果
     */
    private Map<String,List<OrderSplitResult>> splitOrders;


    /**
     * 工厂创建或者构造函数都可以
     * @param orderNo
     * @param storeCode
     * @param skuItemInfos
     */
    public OrderFulfillAggregate(String orderNo,String storeCode,List<SkuItemInfo> skuItemInfos){
        //初始化校验 这里省略
        this.fulfillSkuItems = Lists.newArrayList();
        skuItemInfos.forEach(skuItemInfo -> {
            fulfillSkuItems.add(new FulfillSkuItem(skuItemInfo));
        });
        this.orderNo = orderNo;
        this.storeCode = storeCode;
        this.splitOrders = Maps.newHashMap();
        this.fulfillWarehouses = Lists.newArrayList();
        this.skuMappingWarehouseMap = Maps.newHashMap();
        this.storeMappingWarehouseMap = Maps.newHashMap();
    }

    public void initBaseInfo(List<WarehouseInfo> warehouseInfos, Map<String,String> skuMappingWarehouseMap,
                     Map<String,String> storeMappingWarehouseMap){
        //check
        this.skuMappingWarehouseMap = skuMappingWarehouseMap;
        this.storeMappingWarehouseMap = storeMappingWarehouseMap;
        warehouseInfos.forEach(warehouseInfo -> {
            this.fulfillWarehouses.add(new FulfillWarehouse(warehouseInfo.getWarehouseCode(),warehouseInfo.getAreaCode(),
                    warehouseInfo.getInventoryMap()));
        });

    }

    /**
     * 业务检查
     */
    public void check(){

    }

    /**
     * 分仓
     */
    public void dispatch(){
        this.fulfillSkuItems.forEach(this::doDispatch);
    }

    private void doDispatch(FulfillSkuItem skuItem) {
        //1 店铺指定仓库发货
        String warehouseCode = storeMappingWarehouseMap.get(this.storeCode);
        if (!Objects.isNull(warehouseCode)) {
            skuItem.dispatch(warehouseCode,skuItem.getShippingAmount());
            return;
        }
        //2 sku指定了仓库发货
        warehouseCode = skuMappingWarehouseMap.get(skuItem.getSkuCode());
        if (!Objects.isNull(warehouseCode)) {
            skuItem.dispatch(warehouseCode,skuItem.getShippingAmount());
            return;
        }
        //3 地理位置就近原则+库存余量+仓库处理能力 权重计算
        this.dispatchByOptimalWarehouse(skuItem);
    }


    /**
     * 最优仓库选择
     * @param skuItem
     */
    private void dispatchByOptimalWarehouse(FulfillSkuItem skuItem){
        Map<String,Integer> dispatchMap = Maps.newHashMap();
        this.fulfillWarehouses.forEach(warehouse->{
               //dispatchMap
        });
        dispatchMap.forEach((warehouseCode,amount)->{
            skuItem.dispatch(warehouseCode,amount);
        });

    }

    /**
     * 获取父订单号
     * @return
     */
    private String makeParentOrderNo(){
        return "10"+System.currentTimeMillis();
    }

    /**
     * 根据分仓结果拆单
     */
    public void split(){
        this.fulfillSkuItems.forEach(skuItem -> {
            skuItem.getDispatchInfo().forEach((warehouseCode,skuAmount)->{
                List<OrderSplitResult> splitResults = this.splitOrders.get(warehouseCode);
                String parentOrderNo = this.makeParentOrderNo();
                if(CollectionUtils.isEmpty(splitResults)){
                    splitResults = Lists.newArrayList();
                    this.splitOrders.put(warehouseCode,splitResults);
                }else {
                    orderNo = splitResults.get(0).getParentOrderNo();
                }
                splitResults.add(new OrderSplitResult(this.orderNo,parentOrderNo,skuItem.getSkuCode(),
                        skuAmount,warehouseCode));
            });
        });
    }

}
