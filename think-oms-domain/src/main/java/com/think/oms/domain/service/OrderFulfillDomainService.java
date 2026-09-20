package com.think.oms.domain.service;

import com.think.oms.domain.model.aggregate.orderfulfill.OrderFulfillAggregate;
import com.think.oms.domain.port.pl.acl.WarehouseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderFulfillDomainService {

    public void initBaseInfo(OrderFulfillAggregate aggregate){
        List<WarehouseInfo> warehouseInfos = getSkuWarehouseInfo(aggregate);
        Map<String,String> skuWarehouseMappingMap = getSkuWarehouseMapping(aggregate);
        Map<String,String> storeWarehouseMappingMap = getSkuStoreMapping(aggregate);
        aggregate.initBaseInfo(warehouseInfos,skuWarehouseMappingMap,storeWarehouseMappingMap);
    }

    private List<WarehouseInfo> getSkuWarehouseInfo(OrderFulfillAggregate aggregate){
        // call acl
        return null;
    }

    private Map<String,String> getSkuWarehouseMapping(OrderFulfillAggregate aggregate){
        // call acl
        return null;
    }

    private Map<String,String> getSkuStoreMapping(OrderFulfillAggregate aggregate){
        // call acl
        return null;
    }
}
