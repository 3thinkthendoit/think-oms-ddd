![banner](https://github.com/user-attachments/assets/87e74021-5126-45a8-81dc-b566ab48b80d)

# think-oms

A **DDD four-layer** OMS (Order Management System) demo for learning Domain-Driven Design in practice.

> **Languages:** English (default) · [中文版 / Chinese](./README.zh-CN.md)

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
