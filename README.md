# think-oms
基于DDD四层架构的订单中台，此文只是一篇DEMO 供领域驱动设计实践参考

# 背景
设计一款可以同时支持接入自有平台，拼多多，抖音，淘宝天猫、小米、小红书、快手等平台的订单，做统一管理用于下游结算，履约，售后，安装等服务

# 战略设计实践

用例图：

创建订单

![diagram-5860164288651623821](https://github.com/user-attachments/assets/9fd98c3a-cae9-446c-ace1-6c6b7e0b92aa)

订单履约(订单发货)

![diagram-14395309108625627799](https://github.com/user-attachments/assets/4d478c25-ce90-42b0-a708-241f5e091dbe)

创建订单时序图
![diagram-10159225477760837334](https://github.com/user-attachments/assets/08e6a62d-dcd2-46e1-9b25-153fc053d672)


# 代码分层架构实践
![image](https://github.com/user-attachments/assets/51593795-74c6-4c3d-8dd0-1edf68975115)


# 代码结构说明
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
│     │  │              └─ service/ ............................... AppService (对应use case)
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
      │  │           └─ osh/ ...................................... 
      │  │              ├─ controller/ ............................ http接口
      │  │              ├─ dto/ ................................... DTO
      │  │              ├─ job/ ................................... 定时任务
      │  │              ├─ listener/ .............................. 本地事件监听
      │  │              ├─ mq/ .................................... 远程事件监听
      │  │              └─ rpc/ ................................... RPC接口
      │  └─ resources/ ............................................ 
      └─ test/ .................................................... 
         └─ java/ ................................................. 
</pre>
