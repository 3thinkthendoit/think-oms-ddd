# think-oms-ddd
基于DDD四层架构的订单中台，此文只是一篇DEMO 供领域驱动设计实践参考

# 背景
设计一款OMS可以支持接入市面上大多数平台的订单：如自营商城，拼多多，抖音，淘宝天猫、小米、小红书、快手等平台做统一订单管理，订单链路跟踪等 服务下游发票,结算，WMS，售后,CTC,大数据等系统
核心功能：订单风控审核，订单拆单、订单自动分仓

![企业微信截图_17229378026391](https://github.com/user-attachments/assets/121efaf8-bbd4-4919-9403-76f6a66adbde)


#### 订单履约流程图

![订单履约流程图](https://github.com/user-attachments/assets/3f85c87f-eed6-4a18-8d46-0f58ce221fd3)





# 战略设计实践

#### 产品愿景图

![企业微信截图_17231869995864](https://github.com/user-attachments/assets/f5a42e39-297d-46f1-baa8-53c1ad0d3539)




#### 用例图：

##### 创建订单

![企业微信截图_17237872552658](https://github.com/user-attachments/assets/e063bd60-6632-4b11-ab05-9b5ee0cadc14)


##### 订单履约(订单发货)

![企业微信截图_17248153411295](https://github.com/user-attachments/assets/9ea54e6e-7dae-47ab-bd42-acfd52f239e0)



##### 订单查询
![diagram-15589063498785607716](https://github.com/user-attachments/assets/563359ef-914e-4c73-af21-8066a3826a12)


##### 发货回传


![企业微信截图_17231966512293](https://github.com/user-attachments/assets/2d543ca6-12c4-4a67-bbe9-0a695379a8fa)


#### 领域模型

![企业微信截图_17247515366952](https://github.com/user-attachments/assets/3bbe323a-f3cc-4b06-bee8-7adc78fd1df8)


#### 领域划分

![企业微信截图_17237150702506](https://github.com/user-attachments/assets/1f88e689-9c49-425e-8b39-74a7a2abaa2d)


#### 上下文关系

![企业微信截图_17237149727451](https://github.com/user-attachments/assets/77dada29-0b86-43a4-ab7e-948045927f8b)




# 战术设计实践

## 创建订单代码模型图
![diagram-14049767362457485694](https://github.com/user-attachments/assets/501036d6-2df1-47a9-a88c-b0058b98fc40)

### DDD分层代码调用时序图(订单接入)

![企业微信截图_17285471469080](https://github.com/user-attachments/assets/c5909cf6-e9e8-4573-bd0e-8e1c783ea00a)






## 代码分层架构实践

#### 网上几种架构实践 核心是领域模型 外层依赖内层
![企业微信截图_20240724171510](https://github.com/user-attachments/assets/71554155-8446-4160-aa9d-1ea216eeb578)


#### 订单中台架构实战 四层架构

![企业微信截图_17231697931191](https://github.com/user-attachments/assets/7c2b2931-c485-4092-a63a-9433be4435cd)





### 代码结构说明
<pre>
think-oms/
├─ think-oms-application/ ......................................... 应用层
│  └─ src/ ........................................................ 
│     ├─ main/ .................................................... 
│     │  ├─ java/ ................................................. 
│     │  │  └─ com/ ............................................... 
│     │  │     └─ think/ .......................................... 
│     │  │        └─ oms/ ......................................... 
│     │  │           └─ app/ ...................................... 
│     │  │              └─ service/ ............................... AppService (对应user case)
│     │  └─ resources/ ............................................ 
│     └─ test/ .................................................... 
│        └─ java/ ................................................. 
├─ think-oms-domain/ .............................................. 领域层
│  └─ src/ ........................................................ 
│     ├─ main/ .................................................... 
│     │  ├─ java/ ................................................. 
│     │  │  └─ com/ ............................................... 
│     │  │     └─ think/ .......................................... 
│     │  │        └─ oms/ ......................................... 
│     │  │           └─ domain/ ................................... 
│     │  │              ├─ model/ ................................. 
│     │  │              │  ├─ aggregate/ .......................... 聚合/聚合根
│     │  │              │  │  ├─ ass/ ............................. 
│     │  │              │  │  ├─ create/ .......................... 
│     │  │              │  │  └─ fulfill/ ......................... 
│     │  │              │  ├─ constant/ ........................... 业务枚举
│     │  │              │  ├─ dp/ ................................. DP模型
│     │  │              │  └─ valueobject/ ........................ 值对象
│     │  │              ├─ pl/ .................................... PL转换层
│     │  │              │  ├─ command/ ............................ 创建聚合根command
│     │  │              │  ├─ event/ .............................. 领域事件
│     │  │              │  ├─ query/ .............................. query
│     │  │              │  ├─ request/ ............................ 网关request
│     │  │              │  └─ response/ ........................... 网关response
│     │  │              ├─ port/ .................................. 领域层port(南向网关)
│     │  │              │  ├─ gateway/ ............................ 网关
│     │  │              │  ├─ publisher/ .......................... 领域事件发布
│     │  │              │  └─ repository/ ......................... 资源库
│     │  │              └─ service/ ............................... 领域Service
│     │  └─ resources/ ............................................ 
│     └─ test/ .................................................... 
│        └─ java/ ................................................. 
├─ think-oms-infrastructure/ ...................................... 基础设施层
│  └─ src/ ........................................................ 
│     ├─ main/ .................................................... 
│     │  ├─ java/ ................................................. 
│     │  │  └─ com/ ............................................... 
│     │  │     └─ think/ .......................................... 
│     │  │        └─ oms/ ......................................... 
│     │  │           └─ infrastructure/ ........................... 
│     │  │              ├─ acl/ ................................... 南向网关(port具体实现)
│     │  │              │  ├─ api/ ................................ 外部订单协议实现
│     │  │              │  │  ├─ douyin/ .......................... 
│     │  │              │  │  ├─ kuaishou/ ........................ 
│     │  │              │  │  ├─ mijia/ ........................... 
│     │  │              │  │  ├─ pdd/ ............................. 
│     │  │              │  │  └─ taobao/ .......................... 
│     │  │              │  ├─ gateway/ ............................ 对应port-gateway具体实现
│     │  │              │  ├─ pl/ ................................. PL工具
│     │  │              │  ├─ publisher/ .......................... 对应port-publisher具体实现
│     │  │              │  └─ repository/ ......................... 对应port-repository具体实现
│     │  │              ├─ common/ ................................ 通用
│     │  │              │  └─ util/ ............................... 工具类
│     │  │              └─ core/ .................................. 技术具体实现
│     │  │                 ├─ http/ ............................... http实现
│     │  │                 ├─ mybatis/ ............................ orm实现
│     │  │                 ├─ redis/ .............................. cache实现
│     │  │                 └─ rockermq/ ........................... mqs实现
│     └─ test/ .................................................... 
│        └─ java/ ................................................. 
└─ think-oms-interface/ ........................................... 接口层(北向网关)
   ├─ pom.xml ..................................................... 
   └─ src/ ........................................................ 
      ├─ main/ .................................................... 
      │  ├─ java/ ................................................. 
      │  │  └─ com/ ............................................... 
      │  │     └─ think/ .......................................... 
      │  │        └─ oms/ ......................................... 
      │  │           └─ ohs/ ...................................... 远程网关 
      │  │           |   ├─ controller/ ............................ http接口
      │  │           |   ├─ dto/ ................................... DTO
      │  │           |   ├─ job/ ................................... 定时任务
      │  │           |   ├─ listener/ .............................. 本地事件监听
      │  │           |   ├─ mq/ .................................... 远程事件监听
      │  │           |   └─ rpc/ ................................... RPC接口
      |  |           └─ local/ ...................................... 本地网关 
      |  |           |   └─ service/ ............................ 本地service
      │  └─ resources/ ............................................ 
      └─ test/ .................................................... 
         └─ java/ ................................................. 
</pre>


##  代码落地
### 贫血模型(属性和行为分离)
```Java
//所有类属性对外暴露和修改
@Data
public class OrderInfo {

    private String orderNo;

    private String externalOrderNo;

    private OrderSource orderSource;

    private OrderStatus orderStatus;

    public List<SkuItemInfo> skuInfos;

}

OrderInfo orderInfo = new OrderInfo();
//发生业务 需要挂起订单 ,已发货的无需挂起
if(this.orderStatus != OrderStatus.FULFILLED){
    orderInfo.setOrderStatus(OrderStatus.HANG_UP);//类的属性随时可以通过set方法变更
}

```
### 充血模型(封装属性和领域行为)
```Java
//只暴露订单域和其他域需要协助的类属性
@Getter
public class OrderInfo {

    private String orderNo;

    private String externalOrderNo;

    private OrderSource orderSource;

    private OrderStatus orderStatus;

    public List<SkuItemInfo> skuInfos;

    public static OrderInfo create(OrderCreateCommand createCommand){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.externalOrderNo = createCommand.getExternalOrderNo();
        orderInfo.orderSource = createCommand.getOrderSource();
        //其他属性赋值
        return orderInfo;
    }
    /**
     * 订单挂起(领域方法)
     */
    public void hangup(){
        if(this.orderStatus == OrderStatus.FULFILLED){
            return;
        }
        this.orderStatus = OrderStatus.HANG_UP;
    }
}
OrderCreateCommand command = getOrderCreateCommand();
OrderInfo orderInfo = OrderInfo.create(command);
//发生业务 需要挂起订单 已发货的无需挂起
orderInfo.hangup();

```

### 流程编排(低耦合)
```Java
   //创建订单聚合
   OrderCreateAggregate aggregate = OrderCreateAggregate.create(command);
  //是否重复创建
   orderCreateDomainService.isExist(aggregate);
  //初始化聚合业务的必要信息
   orderCreateDomainService.initBaseInfo(aggregate);
  //订单风控审核
   orderCreateDomainService.audit(aggregate);
  //金额拆分
   aggregate.priceCalculate();
  //sku发货优先级处理
   aggregate.priorityProcessing();
  //新增订单
   orderCreateRepository.save(aggregate);
  //发布订单创建事件，通知履约集合 拆单、分厂
   orderEventPublisher.publish(new OrderCreatedEvent(aggregate.getOrderId().getOrderNo()));
```
### 领域服务(与外部协作领域行为，不适合放在聚合)
```Java
orderCreateDomainService.deductInventory(aggregate);

public void deductInventory(OrderCreateAggregate aggregate){
    DeductInventoryRequest request = DeductInventoryRequest.builder()
                .build();
    //通过南向网关调用 库存域进行库存扣减
    DeductInventoryResponse response = inventoryGateway.deduct(request);
    //domain service 调用聚合的领域方法 完成库存扣减后逻辑
    aggregate.deductInventory(response.getInventoryInfos());
}
    
```
### 聚合

* 限制外界对聚合的对象访问
* 维护领域概念性完整
* 保证聚合内数据强一致性
* 变更状态：业务角度去表达领域逻辑
* 自给自足：操作自身属性进行业务逻辑
* 互为协作：与别的对象进行协作
```Java
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
    
 
```
### 值对象
* 自我验证
* 自我组合
* 自我运算
* 属性在整个过程中,除构建方法外不允许改变


```Java
@Getter
public class StoreInfo {

    private String storeCode;
    private String storeName;

    public void init(String storeCode,String storeName){
        Assert.isNull(storeCode,"storeCode is null!!!");
        Assert.isNull(storeName,"storeName is null!!!");
        this.storeCode = storeCode;
        this.storeName = storeName;
    }

    public StoreInfo(String storeCode){
        Assert.isNull(storeCode,"storeCode is null!!!");
        this.storeCode = storeCode;
    }
```
### DP对象

本身也是值对象，附加了领域行为
```Java
@Getter
public class OrderId {
    private String orderNo;
    private  String externalOrderNo;
    private OrderSource orderSource;

    public OrderId(String externalOrderNo,OrderSource orderSource){
        this.externalOrderNo = externalOrderNo;
        this.orderSource = orderSource;
        this.orderNo = System.currentTimeMillis()+"";
    }
    
    public OrderId(String orderNo){
        this.orderNo = orderNo;
    }
}
```
### 业务方法和领域方法为区分

业务方法:未来一段时间可能发生变化的逻辑(低耦合，可以随时编排)，编排骤可以分开
```Java
 public void createOrder(OrderCreateCommand command){
        OrderCreateAggregate aggregate = OrderCreateAggregate.create(command);
        orderCreateDomainService.isExist(aggregate);
        orderCreateDomainService.initBaseInfo(aggregate);
        aggregate.check();
        aggregate.priceCalculate();
        orderCreateDomainService.deductInventory(aggregate);
        orderRepository.save(aggregate);
        orderEventPublisher.publish(new OrderCreatedEvent(aggregate.getOrderId().getOrderNo()));
    }
```

领域方法：未来一段时间逻辑不变(高内聚，长时间沉淀后的领域逻辑，场景不相关的，可复用的)，编排骤无法分开
```Java
orderCreateDomainService.initBaseInfo(aggregate);

    /**
     * 领域方法(高内聚低耦合)
     * @param aggregate
     */
    public void initBaseInfo(OrderCreateAggregate aggregate){
        this.initStoreInfo(aggregate);
        this.initSkuInfo(aggregate);
        this.initInvoiceInfo(aggregate);
        this.initShippingAddress(aggregate);
        this.initBuyer(aggregate);
    }
```

### 领域方法和领域行为区分
领域行为：聚合/实体内操作自身数据
```Java
aggregate.priceCalculate();

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
            OrderSkuItem skuItem = new OrderSkuItem(skuInfo.getSkuInfo().getSkuId(),skuInfo.getSkuInfo().getSkuCode(),
                    skuInfo.getSkuBuyAmount(),skuInfo.getSkuPayPrice(),feeAmountInfos);
            this.skuItems.add(skuItem);
        });
    }
```

领域方法:与南向网关合作数据,设计模式满足扩展需求,资源库/抽象网关提供数据,名称中需要包含动词
```Java
 orderCreateDomainService.deductInventory(aggregate);
 /**
     * 扣减库存 (领域方法)
     * 多领域协作
     * @param aggregate
     */
    public void deductInventory(OrderCreateAggregate aggregate){
        DeductInventoryRequest request = DeductInventoryRequest.builder()
                .build();
        DeductInventoryResponse response = inventoryGateway.deduct(request);
        aggregate.deductInventory(response.getInventoryInfos());
    }
```

### DDD和性能之间取舍和平衡

接入订单聚合是设计成批量接入还是单次接入？如何取舍

### DDD如何处理 数据一致性（业务补偿）

订单分仓拆单 持久化后，

通过领域事件解耦,可以做一致性数据补偿
```Java
    //订单分仓拆单后 发布领域事件
     OrderFulfillAggregate aggregate = orderFulfillRepository.ofByOrderNo(orderNo);
     orderFulfillDomainService.initBaseInfo(aggregate);
     aggregate.check();
     aggregate.dispatch();
     aggregate.split();
     orderFulfillRepository.save(aggregate);
     orderEventPublisher.publish(new OrderFulfillEvent(orderNo));

    //监听领域事件
    @EventListener(value = OrderFulfillEvent.class)
    @Async
    public void onApplicationEvent(OrderFulfillEvent event) {
        log.info("OrderFulfilledEvent :orderNo=[{}]",event.getOrderNo());
        try {
            orderAppService.fulfillOrder(event.getOrderNo());
        }catch (Exception ex){
            log.error(ex.getMessage(),ex);
        }
    }

    /**
     * 推WMS履约(可做业务补偿)
     * 业务逻辑比较薄，可以直接绕过领域层逻辑直接操作南向网关
     * @param orderNo
     */
    public void fulfillOrder(String orderNo){
        List<FulfillOrderInfo> list = orderFulfillRepository.queryFulfillOrderInfos(orderNo);
        list.forEach(order->{
            OrderFulfillRequest request = OrderFulfillRequest.builder()
                    .warehouseCode(order.getWarehouseCode())
                    .omsOrderNo(order.getOmsOrderNo())
                    .build();
            OrderFulfillResponse response =  orderFulfillGateway.fulfill(request);
            if(response.isFulfill()){
                orderFulfillRepository.updateOrderFulfill(order.getOmsOrderNo());
            }
        });
    }
```
