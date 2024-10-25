# ZRPC

### 介绍

high performance rpc framework based on netty & spring

### 整体架构
![img.png](img/framework.png)

### pipeline 模型
![img.png](img/pipeline.png)

### 注册中心
采用层级结构，规范如下
![img.png](img/registry.png)

### 自定义协议

![img.png](img/protocol.png)
这里没有按照上面设计的方式去定义，最后为了方便，舍弃了一些用不到的字段。
![img.png](img/protocol_2.jpg)

### 消费者调用流程

![img.png](img/consumer.png)