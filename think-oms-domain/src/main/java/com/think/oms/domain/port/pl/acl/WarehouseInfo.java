package com.think.oms.domain.port.pl.acl;

import lombok.Data;
import java.util.Map;

@Data
public class WarehouseInfo {

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 与收货地址距离
     */
    private Double distance;

    /**
     * 仓库发货处理水平
     */
    private Integer shippingLevel;

    /**
     * sku的库存余量
     */
    private Map<String,Long> inventoryMap;
}
