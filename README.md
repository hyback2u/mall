# 分布式商城系统(先写到这里吧... 去干其他的了...QAQ)
> 项目分为三篇，分为分布式基础篇，分布式高级篇和高可用集群架构篇

# 1、分布式基础篇
## 1.1 内容概述
分布式基础篇内容：

## 1.2 开发技术栈
- 后端：SpringBoot、MyBatis、MyBatisPlus、SpringCloud、Docker
- 前端：Vue、ElementUI

采用逆向工程的方式快速的开发一个后台管理系统，是基础的全栈开发。

## 1.3 项目效果
分布式基础篇：商城的后台管理系统，做一个整套的CRUD逻辑
- 前后端分离的方式，使用 Vue 开发。
- 商品管理系统的编写，掌握基础的 CRUD 开发。
- 包括：远程调用微服务。

![image](https://user-images.githubusercontent.com/42307653/172985271-85d46458-29ac-4735-8eb4-65620bc40f31.png)


# 2、分布式高级篇
## 2.1 内容概述
分布式高级篇内容：将通过实现一整套的商城业务逻辑，如商品服务、购物车、订单、结算、库存、秒杀 . . .

通过实现这么一套业务，来打通我们整个在微服务开发期间的整个技术栈以及一些重点、难点。
包括会使用 SpringBoot、SpringCloud 作为基础的配套，以及搭配 SpringCloud Alibaba；比如它的 Nacos 注册中心，配置中心、Sentinel 引入流量保护、以及 Seata 分布式事务等。

不仅有分布式里的高级技术以及微服务的中间所有的配套方案；还会讲解各种微服务开发期间的重难点，比如一些网关、远程调用、链路追踪、缓存、Session同步方案以及全文检索、异步编排、线程池、压力测试、调优、Redis、分布式锁等等；通过分布式高级篇，拥有一个独立的项目架构能力。

## 2.2 开发技术栈
- ElasticSearch + Kibana + logStash
- 性能压测与系统调优
- 缓存、分布式缓存技术
- SpringCloud Alibaba Sentinel 流控组件


## 2.3 项目效果
分布式高级篇：微服务开发中的所有技术问题及方案 和 周边治理设施；
- Sentinel 流量控制
- Nacos 注册中心
- Zipkin 链路追踪 查找线上问题

![image](https://user-images.githubusercontent.com/42307653/172985837-572108c5-a3bc-4335-b441-d845c611b98f.png)


