![banner](https://github.com/user-attachments/assets/87e74021-5126-45a8-81dc-b566ab48b80d)

# think-oms

A **DDD four-layer** OMS (Order Management System) demo for learning Domain-Driven Design in practice.

> **Languages:** English (default) · [中文版](#chinese--中文版)

---

## Overview

`think-oms` unifies orders from multiple channels (self-operated mall, Pinduoduo, Douyin, Taobao/Tmall, Xiaomi, and similar platforms) into one OMS for order management and fulfillment tracking. Downstream systems can include invoicing, settlement, WMS, after-sales, CTC, and analytics.

**Core capabilities**

- Risk audit for inbound orders
- Warehouse dispatch
- Order splitting

![OMS landscape](https://github.com/user-attachments/assets/121efaf8-bbd4-4919-9403-76f6a66adbde)

### Order fulfillment flow

![Order fulfillment flow](https://github.com/user-attachments/assets/3f85c87f-eed6-4a18-8d46-0f58ce221fd3)

---

## Tech stack

| Item | Version / choice |
|------|------------------|
| Java | **17** |
| Spring Boot | **3.5.9** |
| Build | Maven multi-module |
| ORM | MyBatis-Plus **3.5.17** (`mybatis-plus-spring-boot3-starter`) |
| DB pool | Druid **1.2.28** (`druid-spring-boot-3-starter`) |
| MySQL driver | `mysql-connector-j` **9.5.0** |
| MQ | RocketMQ Spring Boot starter **2.3.6** |
| HTTP | OkHttp **4.12.0** |
| Misc | Lombok **1.18.42**, Guava **33.7.1-jre**, Fastjson **1.2.83** |

Injection uses `jakarta.annotation.Resource` (Jakarta EE / Spring Boot 3).

---

## Module layout

Dependency rule: **outer depends on inner**; the domain module never depends on application, infrastructure, or interface.

```text
think-oms-domain            (innermost — no project module deps)
        ↑
        ├── think-oms-application      → domain only
        └── think-oms-infrastructure   → domain + tech libs
                ↑
think-oms-interface  → application + infrastructure   (bootable)
```

| Module | Role |
|--------|------|
| `think-oms-interface` | Northbound adapters (HTTP / job / MQ / RPC / local events) + Spring Boot entry |
| `think-oms-application` | Use-case orchestration (`*AppService`, CQRS-style) |
| `think-oms-domain` | Aggregates, domain services, ports, PL objects |
| `think-oms-infrastructure` | ACL implementations of ports + `core` (MyBatis / HTTP / Redis / RocketMQ) |

### Package map (matches current code)

```text
think-oms/
├─ think-oms-application/
│  └─ com.think.oms.app.service/ ..................... OrderAppService (use cases)
├─ think-oms-domain/
│  └─ com.think.oms.domain/
│     ├─ model/
│     │  ├─ aggregate/
│     │  │  ├─ create/ ............................... OrderCreateAggregate
│     │  │  ├─ orderfulfill/ ......................... OrderFulfillAggregate
│     │  │  └─ shippingcallback/ ..................... ShippingCallbackAggregate
│     │  ├─ constant/ ................................ enums
│     │  ├─ dp/ ...................................... domain primitives (e.g. OrderId)
│     │  └─ valueobject/ ............................. value objects
│     ├─ port/
│     │  ├─ gateway/ ................................. southbound contracts
│     │  ├─ publisher/ ............................... domain event publisher
│     │  ├─ repository/ .............................. persistence ports
│     │  └─ pl/
│     │     ├─ osh/ .................................. northbound PL
│     │     │  ├─ command/ ........................... create / shipping commands
│     │     │  ├─ event/ ............................. domain events
│     │     │  └─ query/ ............................. queries
│     │     └─ acl/ .................................. southbound PL
│     │        ├─ request/ | response/ ............... gateway I/O
│     │        └─ *Info / *Result .................... ACL transfer models
│     └─ service/ .................................... domain services
├─ think-oms-infrastructure/
│  └─ com.think.oms.infrastructure/
│     ├─ acl/
│     │  ├─ api/{douyin,pdd,taobao}/ .................. channel protocol clients
│     │  ├─ gateway/ | publisher/ | repository/ ...... port implementations
│     │  └─ pl/ ...................................... aggregate ↔ PO helpers
│     └─ core/{http,mybatis,redis,rockermq}/ .......... tech adapters
└─ think-oms-interface/
   └─ com.think.oms/
      ├─ osh/ ........................................ remote / northbound gateway
      │  ├─ ThinkOmsApplication ...................... Spring Boot main
      │  ├─ controller/ | dto/ | job/ | listener/
      │  ├─ mq/ | rpc/
      └─ local/ ...................................... local facade → AppService
```

**PL naming note:** `osh` = Open Host Service (northbound), `acl` = Anti-Corruption Layer (southbound).

---

## Strategic design (diagrams)

Product vision:

![Product vision](https://github.com/user-attachments/assets/f5a42e39-297d-46f1-baa8-53c1ad0d3539)

| Use case | Diagram |
|----------|---------|
| Create order | ![create](https://github.com/user-attachments/assets/e063bd60-6632-4b11-ab05-9b5ee0cadc14) |
| Fulfill / ship | ![fulfill](https://github.com/user-attachments/assets/9ea54e6e-7dae-47ab-bd42-acfd52f239e0) |
| Query order | ![query](https://github.com/user-attachments/assets/563359ef-914e-4c73-af21-8066a3826a12) |
| Shipping callback | ![callback](https://github.com/user-attachments/assets/2d543ca6-12c4-4a67-bbe9-0a695379a8fa) |

Domain model / bounded contexts / context map:

![Domain model](https://github.com/user-attachments/assets/3bbe323a-f3cc-4b06-bee8-7adc78fd1df8)

![Bounded contexts](https://github.com/user-attachments/assets/1f88e689-9c49-425e-8b39-74a7a2abaa2d)

![Context map](https://github.com/user-attachments/assets/77dada29-0b86-43a4-ab7e-948045927f8b)

---

## Tactical design

Create-order model and inbound call sequence:

![Create-order model](https://github.com/user-attachments/assets/501036d6-2df1-47a9-a88c-b0058b98fc40)

![DDD call sequence](https://github.com/user-attachments/assets/c5909cf6-e9e8-4573-bd0e-8e1c783ea00a)

Layering (outer → inner):

![Layering practices](https://github.com/user-attachments/assets/71554155-8446-4160-aa9d-1ea216eeb578)

![Four-layer OMS](https://github.com/user-attachments/assets/7c2b2931-c485-4092-a63a-9433be4435cd)

### Typical create-order orchestration

From `OrderAppService` (current code):

```java
public void createOrder(OrderCreateCommand command) {
    OrderCreateAggregate aggregate = OrderCreateAggregate.create(command);
    orderCreateDomainService.isExist(aggregate);
    orderCreateDomainService.initBaseInfo(aggregate);
    orderCreateDomainService.audit(aggregate);
    aggregate.priceCalculate();
    aggregate.priorityProcessing();
    orderCreateRepository.save(aggregate);
    orderEventPublisher.publish(new OrderCreatedEvent(aggregate.getOrderId().getOrderNo()));
}
```

### Dispatch → fulfill (event-driven compensation)

```text
OrderCreatedListener  →  dispatchOrder (split / warehouse)
                      →  publish OrderFulfillEvent
OrderFulfilledListener →  fulfillOrder (push WMS via OrderFulfillGateway)
```

---

## Quick start

**Requirements:** JDK 17+, Maven 3.6+

```bash
mvn clean compile -DskipTests
```

Boot entry: `com.think.oms.osh.ThinkOmsApplication` in `think-oms-interface`.

Default HTTP port: `8000` (`think-oms-interface/src/main/resources/application.yml`).

Configure MySQL / Druid / MyBatis before running:

- Driver: `com.mysql.cj.jdbc.Driver`
- Mapper locations: `classpath*:mapper/*.xml`

---

## Design notes (short)

| Topic | Practice in this demo |
|-------|------------------------|
| Anemic vs rich model | Prefer rich aggregates with domain behavior + `@Getter`, not public setters for state |
| AppService | Orchestrates use cases; keeps steps loosely coupled |
| Domain service | Cross-aggregate / southbound collaboration (gateways) |
| Aggregate behavior | Operates on own state (`priceCalculate`, `dispatch`, `split`, …) |
| CQRS lean | Queries may bypass domain and call gateways directly |
| Consistency | Domain events + async listeners for compensation (e.g. WMS push) |

This repository is a **learning demo**, not a production-ready OMS.

---

<a id="chinese--中文版"></a>

# 中文版

基于 **DDD 四层架构** 的订单中台 DEMO，供领域驱动设计实践参考。

[↑ Back to English](#think-oms)

## 背景

设计一款 OMS，接入市面上主流平台订单（自营商城、拼多多、抖音、淘宝天猫、小米、小红书、快手等），统一订单管理与链路跟踪，并服务下游发票、结算、WMS、售后、CTC、大数据等系统。

**核心能力：** 订单风控审核、订单拆单、订单自动分仓。

![企业微信截图_17229378026391](https://github.com/user-attachments/assets/121efaf8-bbd4-4919-9403-76f6a66adbde)

#### 订单履约流程图

![订单履约流程图](https://github.com/user-attachments/assets/3f85c87f-eed6-4a18-8d46-0f58ce221fd3)

## 技术栈（与当前代码一致）

| 项 | 版本 |
|----|------|
| Java | **17** |
| Spring Boot | **3.5.9** |
| MyBatis-Plus | **3.5.17**（`mybatis-plus-spring-boot3-starter`） |
| Druid | **1.2.28**（`druid-spring-boot-3-starter`） |
| MySQL | `mysql-connector-j` **9.5.0** |
| RocketMQ Starter | **2.3.6** |
| OkHttp / Lombok / Guava / Fastjson | 4.12.0 / 1.18.42 / 33.7.1-jre / 1.2.83 |

依赖注入统一使用 `jakarta.annotation.Resource`。

## 战略设计实践

#### 产品愿景图

![企业微信截图_17231869995864](https://github.com/user-attachments/assets/f5a42e39-297d-46f1-baa8-53c1ad0d3539)

#### 用例图

##### 创建订单

![企业微信截图_17237872552658](https://github.com/user-attachments/assets/e063bd60-6632-4b11-ab05-9b5ee0cadc14)

##### 订单履约（订单发货）

![企业微信截图_17248153411295](https://github.com/user-attachments/assets/9ea54e6e-7dae-47ab-bd42-acfd52f239e0)

##### 订单查询

![diagram-15589063498785607716](https://github.com/user-attachments/assets/563359ef-914e-4c73-af21-8066a3826a12)

##### 发货回传

![企业微信截图_17231966512293](https://github.com/user-attachments/assets/2d543ca6-12c4-4a67-bbe9-0a695379a8fa)

#### 领域模型 / 领域划分 / 上下文关系

![企业微信截图_17247515366952](https://github.com/user-attachments/assets/3bbe323a-f3cc-4b06-bee8-7adc78fd1df8)

![企业微信截图_17237150702506](https://github.com/user-attachments/assets/1f88e689-9c49-425e-8b39-74a7a2abaa2d)

![企业微信截图_17237149727451](https://github.com/user-attachments/assets/77dada29-0b86-43a4-ab7e-948045927f8b)

## 战术设计实践

## 创建订单代码模型图

![diagram-14049767362457485694](https://github.com/user-attachments/assets/501036d6-2df1-47a9-a88c-b0058b98fc40)

### DDD 分层代码调用时序图（订单接入）

![企业微信截图_17285471469080](https://github.com/user-attachments/assets/c5909cf6-e9e8-4573-bd0e-8e1c783ea00a)

## 代码分层架构实践

#### 网上几种架构实践：核心是领域模型，外层依赖内层

![企业微信截图_20240724171510](https://github.com/user-attachments/assets/71554155-8446-4160-aa9d-1ea216eeb578)

#### 订单中台架构实战：四层架构

![企业微信截图_17231697931191](https://github.com/user-attachments/assets/7c2b2931-c485-4092-a63a-9433be4435cd)

### 代码结构说明（已按最新仓库校正）

```text
think-oms/
├─ think-oms-application/ .............................. 应用层
│  └─ com.think.oms.app.service/ ....................... AppService（对应用例）
├─ think-oms-domain/ ................................... 领域层
│  └─ com.think.oms.domain/
│     ├─ model/
│     │  ├─ aggregate/
│     │  │  ├─ create/ ................................. 创建订单聚合
│     │  │  ├─ orderfulfill/ ........................... 履约/分仓拆单聚合
│     │  │  └─ shippingcallback/ ....................... 发货回传聚合
│     │  ├─ constant/ .................................. 业务枚举
│     │  ├─ dp/ ........................................ DP（如 OrderId）
│     │  └─ valueobject/ ............................... 值对象
│     ├─ port/
│     │  ├─ gateway/ | publisher/ | repository/ ........ 南向端口
│     │  └─ pl/
│     │     ├─ osh/{command,event,query}/ .............. 北向 PL（Open Host Service）
│     │     └─ acl/{request,response,...}/ ............. 南向 PL（ACL）
│     └─ service/ ...................................... 领域服务
├─ think-oms-infrastructure/ ........................... 基础设施层
│  └─ com.think.oms.infrastructure/
│     ├─ acl/
│     │  ├─ api/{douyin,pdd,taobao}/ ................... 外部订单协议客户端
│     │  ├─ gateway/ | publisher/ | repository/ ........ Port 实现
│     │  └─ pl/ ........................................ 聚合 ↔ PO 转换
│     └─ core/{http,mybatis,redis,rockermq}/ ........... 技术组件
└─ think-oms-interface/ ................................ 接口层（北向）
   └─ com.think.oms/
      ├─ osh/ .......................................... 远程网关 + 启动类
      │  ├─ controller/ | dto/ | job/ | listener/
      │  ├─ mq/ | rpc/
      └─ local/ ........................................ 本地门面 → AppService
```

依赖方向：**外层依赖内层**；`domain` 不依赖 `application` / `infrastructure` / `interface`。

启动类：`com.think.oms.osh.ThinkOmsApplication`。

## 代码落地

### 贫血模型（属性和行为分离）

```java
@Data
public class OrderInfo {
    private String orderNo;
    private String externalOrderNo;
    private OrderSource orderSource;
    private OrderStatus orderStatus;
    public List<SkuItemInfo> skuInfos;
}

OrderInfo orderInfo = new OrderInfo();
if (this.orderStatus != OrderStatus.FULFILLED) {
    orderInfo.setOrderStatus(OrderStatus.HANG_UP); // 状态可被随意 set
}
```

### 充血模型（封装属性与领域行为）

```java
@Getter
public class OrderInfo {
    private String orderNo;
    private String externalOrderNo;
    private OrderSource orderSource;
    private OrderStatus orderStatus;
    public List<SkuItemInfo> skuInfos;

    public static OrderInfo create(OrderCreateCommand createCommand) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.externalOrderNo = createCommand.getExternalOrderNo();
        orderInfo.orderSource = createCommand.getOrderSource();
        return orderInfo;
    }

    /** 订单挂起（领域方法） */
    public void hangup() {
        if (this.orderStatus == OrderStatus.FULFILLED) {
            return;
        }
        this.orderStatus = OrderStatus.HANG_UP;
    }
}
```

### 流程编排（低耦合）

与当前 `OrderAppService#createOrder` 一致：

```java
OrderCreateAggregate aggregate = OrderCreateAggregate.create(command);
orderCreateDomainService.isExist(aggregate);
orderCreateDomainService.initBaseInfo(aggregate);
orderCreateDomainService.audit(aggregate);
aggregate.priceCalculate();
aggregate.priorityProcessing();
orderCreateRepository.save(aggregate);
orderEventPublisher.publish(new OrderCreatedEvent(aggregate.getOrderId().getOrderNo()));
```

### 领域服务（与外部协作，不适合放在聚合内）

```java
orderCreateDomainService.deductInventory(aggregate);

public void deductInventory(OrderCreateAggregate aggregate) {
    DeductInventoryRequest request = DeductInventoryRequest.builder().build();
    DeductInventoryResponse response = inventoryGateway.deduct(request);
    aggregate.deductInventory(response.getInventoryInfos());
}
```

### 聚合

- 限制外界对聚合内部对象的随意访问  
- 维护领域概念完整性  
- 保证聚合内数据强一致性  
- 用业务语言表达状态变更  
- 自给自足 / 互为协作  

履约聚合关键行为：`initBaseInfo` → `check` → `dispatch` → `split`（见 `OrderFulfillAggregate`）。

### 值对象 / DP

- 值对象：自我验证、自我组合、自我运算；构建后属性不可变  
- DP（如 `OrderId`）：值对象 + 领域构造规则  

### 业务方法 vs 领域方法 vs 领域行为

| 类型 | 含义 | 示例 |
|------|------|------|
| 业务方法 | 可编排、易变的用例步骤 | `OrderAppService#createOrder` |
| 领域方法 | 跨南向网关协作、含动词 | `initBaseInfo`、`deductInventory`、`audit` |
| 领域行为 | 聚合内部操作自身数据 | `priceCalculate`、`dispatch`、`split` |

### DDD 与性能、一致性

- 接入聚合做批量还是单笔，需结合吞吐与一致性权衡  
- 分仓拆单持久化后，通过领域事件解耦，并可做补偿：

```java
// 分仓拆单后发布事件
orderFulfillRepository.save(aggregate);
orderEventPublisher.publish(new OrderFulfillEvent(orderNo));

// 监听后推 WMS
@EventListener(OrderFulfillEvent.class)
@Async
public void onApplicationEvent(OrderFulfillEvent event) {
    orderLocalService.fulfillOrder(event.getOrderNo());
}
```

查询类可绕过领域层，直接调用南向网关（CQRS 瘦查询），见 `OrderAppService#query`。

---

本仓库为 **学习型 DEMO**，非生产级 OMS 实现。
