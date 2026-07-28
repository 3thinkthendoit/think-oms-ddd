![image](https://github.com/user-attachments/assets/87e74021-5126-45a8-81dc-b566ab48b80d)

# think-oms-ddd
An Order Middle-Platform based on the DDD four-layer architecture. This project is a DEMO intended as a reference for Domain-Driven Design (DDD) practice.

# Background
The goal is to design an OMS (Order Management System) capable of supporting order integration from most platforms on the market—such as self-operated malls, Pinduoduo, Douyin, Taobao/Tmall, Xiaomi, Xiaohongshu, Kuaishou, etc.—to achieve unified order management and order link tracking. It serves downstream systems such as Invoicing, Settlement, WMS, After-sales, CTC, and Big Data.
Core functions: Order risk control audit, order splitting, and automatic order warehouse allocation.

![企业微信截图_17229378026391](https://github.com/user-attachments/assets/121efaf8-bbd4-4919-9403-76f6a66adbde)

#### Order Fulfillment Flowchart

![订单履约流程图](https://github.com/user-attachments/assets/3f85c87f-eed6-4a18-8d46-0f58ce221fd3)

# Strategic Design Practice

#### Product Vision Map

![企业微信截图_17231869995864](https://github.com/user-attachments/assets/f5a42e39-297d-46f1-baa8-53c1ad0d3539)

#### Use Case Diagrams:

##### Create Order

![企业微信截图_17237872552658](https://github.com/user-attachments/assets/e063bd60-6632-4b11-ab05-9b5ee0cadc14)

##### Order Fulfillment (Order Shipping)

![企业微信截图_17248153411295](https://github.com/user-attachments/assets/9ea54e6e-7dae-47ab-bd42-acfd52f239e0)

##### Order Query
![diagram-15589063498785607716](https://github.com/user-attachments/assets/563359ef-914e-4c73-af21-8066a3826a12)

##### Shipping Callback

![企业微信截图_17231966512293](https://github.com/user-attachments/assets/2d543ca6-12c4-4a67-bbe9-0a695379a8fa)

#### Domain Model

![企业微信截图_17247515366952](https://github.com/user-attachments/assets/3bbe323a-f3cc-4b06-bee8-7adc78fd1df8)

#### Domain Partitioning

![企业微信截图_17237150702506](https://github.com/user-attachments/assets/1f88e689-9c49-425e-8b39-74a7a2abaa2d)

#### Context Relationships

![企业微信截图_17237149727451](https://github.com/user-attachments/assets/77dada29-0b86-43a4-ab7e-948045927f8b)

# Tactical Design Practice

## Order Creation Code Model Diagram
![diagram-14049767362457485694](https://github.com/user-attachments/assets/501036d6-2df1-47a9-a88c-b0058b98fc40)

### DDD Layered Code Call Sequence Diagram (Order Integration)

![企业微信截图_17285471469080](https://github.com/user-attachments/assets/c5909cf6-e9e8-4573-bd0e-8e1c783ea00a)

## Code Layering Architecture Practice

#### Common Architecture Practices: The core is the domain model, and outer layers depend on inner layers.
![企业微信截图_20240724171510](https://github.com/user-attachments/assets/71554155-8446-4160-aa9d-1ea216eeb578)

#### Order Middle-Platform Practical Architecture: Four-Layer Architecture

![企业微信截图_17231697931191](https://github.com/user-attachments/assets/7c2b2931-c485-4092-a63a-9433be4435cd)

### Code Structure Explanation
<pre>
think-oms/
├─ think-oms-application/ ......................................... Application Layer
│  └─ src/ ........................................................ 
│     ├─ main/ .................................................... 
│     │  ├─ java/ ................................................. 
│     │  │  └─ com/ ............................................... 
│     │  │     └─ think/ .......................................... 
│     │  │        └─ oms/ ......................................... 
│     │  │           └─ app/ ...................................... 
│     │  │              └─ service/ ............................... AppService (corresponds to use cases)
│     │  └─ resources/ ............................................ 
│     └─ test/ .................................................... 
│        └─ java/ ................................................. 
├─ think-oms-domain/ .............................................. Domain Layer
│  └─ src/ ........................................................ 
│     ├─ main/ .................................................... 
│     │  ├─ java/ ................................................. 
│     │  │  └─ com/ ............................................... 
│     │  │     └─ think/ .......................................... 
│     │  │        └─ oms/ ......................................... 
│     │  │           └─ domain/ ................................... 
│     │  │              ├─ model/ ................................. 
│     │  │              │  ├─ aggregate/ .......................... Aggregates/Aggregate Roots
│     │  │              │  │  ├─ ass/ ............................. 
│     │  │              │  │  ├─ create/ .......................... 
│     │  │              │  │  └─ fulfill/ ......................... 
│     │  │              │  ├─ constant/ ........................... Business Enums
│     │  │              │  ├─ dp/ ................................. DP Models
│     │  │              │  └─ valueobject/ ........................ Value Objects
│     │  │              ├─ pl/ .................................... PL Transformation Layer
│     │  │              │  ├─ command/ ............................ Commands for creating Aggregate Roots
│     │  │              │  ├─ event/ .............................. Domain Events
│     │  │              │  ├─ query/ .............................. Queries
│     │  │              │  ├─ request/ ............................ Gateway Requests
│     │  │              │  └─ response/ ........................... Gateway Responses
│     │  │              ├─ port/ .................................. Domain Layer Ports (Southbound Gateway)
│     │  │              │  ├─ gateway/ ............................ Gateways
│     │  │              │  ├─ publisher/ .......................... Domain Event Publishers
│     │  │              │  └─ repository/ ......................... Repositories
│     │  │              └─ service/ ............................... Domain Services
│     │  └─ resources/ ............................................ 
│     └─ test/ .................................................... 
│        └─ java/ ................................................. 
├─ think-oms-infrastructure/ ...................................... Infrastructure Layer
│  └─ src/ ........................................................ 
│     ├─ main/ .................................................... 
│     │  ├─ java/ ................................................. 
│     │  │  └─ com/ ............................................... 
│     │  │     └─ think/ .......................................... 
│     │  │        └─ oms/ ......................................... 
│     │  │           └─ infrastructure/ ........................... 
│     │  │              ├─ acl/ ................................... Southbound Gateway (Port implementations)
│     │  │              │  ├─ api/ ................................ External Order Protocol Implementations
│     │  │              │  │  ├─ douyin/ .......................... 
│     │  │              │  │  ├─ kuaishou/ ........................ 
│     │  │              │  │  ├─ mijia/ ........................... 
│     │  │              │  │  ├─ pdd/ ............................. 
│     │  │              │  │  └─ taobao/ .......................... 
│     │  │              │  ├─ gateway/ ............................ Implementation of port-gateway
│     │  │              │  ├─ pl/ ................................. PL Utilities
│     │  │              │  ├─ publisher/ .......................... Implementation of port-publisher
│     │  │              │  └─ repository/ ......................... Implementation of port-repository
│     │  │              ├─ common/ ................................ Common
│     │  │              │  └─ util/ ............................... Utility Classes
│     │  │              └─ core/ .................................. Technical Implementations
│     │  │                 ├─ http/ ............................... HTTP implementation
│     │  │                 ├─ mybatis/ ............................ ORM implementation
│     │  │                 ├─ redis/ .............................. Cache implementation
│     │  │                 └─ rockermq/ ........................... MQ implementation
│     └─ test/ .................................................... 
│        └─ java/ ................................................. 
└─ think-oms-interface/ ........................................... Interface Layer (Northbound Gateway)
   ├─ pom.xml ..................................................... 
   └─ src/ ........................................................ 
      ├─ main/ .................................................... 
      │  ├─ java/ ................................................. 
      │  │  └─ com/ ............................................... 
      │  │     └─ think/ .......................................... 
      │  │        └─ oms/ ......................................... 
      │  │           └─ ohs/ ...................................... Remote Gateway 
      │  │           |   ├─ controller/ ............................ HTTP Interfaces
      │  │           |   ├─ dto/ ................................... DTOs
      │  │           |   ├─ job/ ................................... Scheduled Tasks
      │  │           |   ├- listener/ .............................. Local Event Listeners
      │  │           |   ├- mq/ .................................... Remote Event Listeners
      │  │           |   └- rpc/ ................................... RPC Interfaces
      |  |           └- local/ ...................................... Local Gateway 
      |  |           |   └- service/ ............................ Local Services
      │  └- resources/ ............................................ 
      └- test/ .................................................... 
         └- java/ ................................................. 
</pre>

## Implementation

### Anemic Model (Separation of properties and behavior)
```Java
// All class properties are exposed for modification
@Data
public class OrderInfo {

    private String orderNo;

    private String externalOrderNo;

    private OrderSource orderSource;

    private OrderStatus orderStatus;

    public List<SkuItemInfo> skuInfos;

}

OrderInfo orderInfo = new OrderInfo();
// Business logic: Order needs to be hung up, but not if it's already shipped
if(this.orderStatus != OrderStatus.FULFILLED){
    orderInfo.setOrderStatus(OrderStatus.HANG_UP);// Properties can be changed via set methods at any time
}
```

### Rich Model (Encapsulation of properties and domain behavior)
```Java
// Only expose properties needed by the order domain and other domains
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
        // other property assignments
        return orderInfo;
    }
    /**
     * Order Hang-up (Domain Method)
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
// Business logic: Order needs to be hung up, but not if it's already shipped
orderInfo.hangup();
```

### Process Orchestration (Low Coupling)
```Java
   // Create order aggregate
   OrderCreateAggregate aggregate = OrderCreateAggregate.create(command);
  // Check for duplicate creation
   orderCreateDomainService.isExist(aggregate);
  // Initialize necessary business information for the aggregate
   orderCreateDomainService.initBaseInfo(aggregate);
  // Order risk control audit
   orderCreateDomainService.audit(aggregate);
  // Amount splitting
   aggregate.priceCalculate();
  // SKU shipping priority processing
   aggregate.priorityProcessing();
  // Save new order
   orderCreateRepository.save(aggregate);
  // Publish order creation event to notify fulfillment (split order, warehouse allocation)
   orderEventPublisher.publish(new OrderCreatedEvent(aggregate.getOrderId().getOrderNo()));
```

### Domain Service (Domain behavior collaborating with external systems, unsuitable for Aggregates)
```Java
orderCreateDomainService.deductInventory(aggregate);

public void deductInventory(OrderCreateAggregate aggregate){
    DeductInventoryRequest request = DeductInventoryRequest.builder()
                .build();
    // Call Inventory domain via Southbound Gateway to deduct inventory
    DeductInventoryResponse response = inventoryGateway.deduct(request);
    // Domain service calls the aggregate's domain method to complete post-deduction logic
    aggregate.deductInventory(response.getInventoryInfos());
}
```

### Aggregate

* Restricts external access to objects within the aggregate.
* Maintains domain conceptual integrity.
* Ensures strong consistency of data within the aggregate.
* State Change: Expresses domain logic from a business perspective.
* Self-sufficient: Performs business logic by operating on its own properties.
* Collaborative: Collaborates with other objects.
```Java
@Getter
public class OrderFulfillAggregate {

    /**
     * Order Number
     */
    private String orderNo;

    /**
     * Store Code
     */
    private String storeCode;

    /**
     * Order order SKU item map
     */
    private List<FulfillSkuItem> fulfillSkuItems;


    /**
     * SKU warehouse inventory information
     */
    private List<FulfillWarehouse> fulfillWarehouses;

    /**
     * SKU-Warehouse mapping relationship
     */
    private Map<String,String> skuMappingWarehouseMap;

    /**
     * Store-Warehouse mapping relationship
     */
    private Map<String,String> storeMappingWarehouseMap;


    /**
     * Order splitting results
     */
    private Map<String,List<OrderSplitResult>> splitOrders;


    /**
     * Factory method or constructor can be used
     * @param orderNo
     * @param storeCode
     * @param skuItemInfos
     */
    public OrderFulfillAggregate(String orderNo,String storeCode,List<SkuItemInfo> skuItemInfos){
        // Initialization validation omitted here
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
        // check
        this.skuMappingWarehouseMap = skuMappingWarehouseMap;
        this.storeMappingWarehouseMap = storeMappingWarehouseMap;
        warehouseInfos.forEach(warehouseInfo -> {
            this.fulfillWarehouses.add(new FulfillWarehouse(warehouseInfo.getWarehouseCode(),warehouseInfo.getAreaCode(),
                    warehouseInfo.getInventoryMap()));
        });

    }

    /**
     * Business check
     */
    public void check(){

    }

    /**
     * Warehouse allocation (Dispatch)
     */
    public void dispatch(){
        this.fulfillSkuItems.forEach(this::doDispatch);
    }

    private void doDispatch(FulfillSkuItem skuItem) {
        // 1. Ship from store-specified warehouse
        String warehouseCode = storeMappingWarehouseMap.get(this.storeCode);
        if (!Objects.isNull(warehouseCode)) {
            skuItem.dispatch(warehouseCode,skuItem.getShippingAmount());
            return;
        }
        // 2. Ship from SKU-specified warehouse
        warehouseCode = skuMappingWarehouseMap.get(skuItem.getSkuCode());
        if (!Objects.isNull(warehouseCode)) {
            skuItem.dispatch(warehouseCode,skuItem.getShippingAmount());
            return;
        }
        // 3. Weight calculation based on geographical proximity + inventory balance + warehouse capacity
        this.dispatchByOptimalWarehouse(skuItem);
    }


    /**
     * Optimal Warehouse Selection
     * @param skuItem
     */
    private void dispatchByOptimalWarehouse(FulfillSkuItem skuItem){
        Map<String,Integer> dispatchMap = Maps.newHashMap();
        this.fulfillWarehouses.forEach(warehouse->{
               // dispatchMap logic
        });
        dispatchMap.forEach((warehouseCode,amount)->{
            skuItem.dispatch(warehouseCode,amount);
        });

    }

    /**
     * Generate parent order number
     * @return
     */
    private String makeParentOrderNo(){
        return "10"+System.currentTimeMillis();
    }

    /**
     * Split order based on warehouse allocation results
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

### Value Object
* Self-validating.
* Self-composing.
* Self-calculating.
* Properties cannot be changed during the process, except for the build method.

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

### DP Object
Essentially a value object with added domain behavior.
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

### Distinction between Business Methods and Domain Methods

**Business Method:** Logic that may change over a certain period of time (low coupling, can be orchestrated at any time). Orchestration steps can be separated.
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

**Domain Method:** Logic that remains unchanged over a certain period (high cohesion, domain logic solidified over time, scenario-independent, reusable). Orchestration steps cannot be separated.
```Java
orderCreateDomainService.initBaseInfo(aggregate);

    /**
     * Domain Method (High cohesion, low coupling)
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

### Distinction between Domain Methods and Domain Behaviors

**Domain Behavior:** Operations on its own data within an Aggregate/Entity.
```Java
aggregate.priceCalculate();

/**
* SKU amount splitting calculation (Domain Behavior)
*/
    public void  priceCalculate(){
        // Split discount amounts, shipping fees, etc., based on the weight of the SKU order amount
        this.skuItems = Lists.newArrayList();
        this.skuInfos.forEach(skuInfo -> {
            // Split combined products, calculate amount and additional fees to be allocated to sub-SKUs
            Map<FeeType,Long> feeAmountInfos = Maps.newHashMap();
            // feeAmountInfos.put(FeeType.TRAN_FEE,)
            // feeAmountInfos.put(FeeType.DISCOUNT_FEE,)
            OrderSkuItem skuItem = new OrderSkuItem(skuInfo.getSkuInfo().getSkuId(),skuInfo.getSkuInfo().getSkuCode(),
                    skuInfo.getSkuBuyAmount(),skuInfo.getSkuPayPrice(),feeAmountInfos);
            this.skuItems.add(skuItem);
        });
    }
```

**Domain Method:** Collaborates with Southbound Gateways for data; uses design patterns to meet extension requirements; data is provided by repositories/abstract gateways; names must contain verbs.
```Java
 orderCreateDomainService.deductInventory(aggregate);
 /**
     * Deduct Inventory (Domain Method)
     * Multi-domain collaboration
     * @param aggregate
     */
    public void deductInventory(OrderCreateAggregate aggregate){
        DeductInventoryRequest request = DeductInventoryRequest.builder()
                .build();
        DeductInventoryResponse response = inventoryGateway.deduct(request);
        aggregate.deductInventory(response.getInventoryInfos());
    }
```

### Trade-offs and Balance between DDD and Performance

Should order integration aggregates be designed for batch integration or single integration? How to choose?

### How DDD Handles Data Consistency (Business Compensation)

After order warehouse allocation and splitting are persisted:

Decoupling through domain events allows for consistent data compensation.
```Java
    // Publish domain event after order warehouse allocation and splitting
     OrderFulfillAggregate aggregate = orderFulfillRepository.ofByOrderNo(orderNo);
     orderFulfillDomainService.initBaseInfo(aggregate);
     aggregate.check();
     aggregate.dispatch();
     aggregate.split();
     orderFulfillRepository.save(aggregate);
     orderEventPublisher.publish(new OrderFulfillEvent(orderNo));

    // Listen to domain event
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
     * Push to WMS fulfillment (Business compensation can be implemented here)
     * Business logic is thin, so it can bypass domain layer logic and operate directly on the Southbound Gateway
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
