# 网上商城购物系统

基于 Spring Boot + Spring Cloud 的微服务架构网上商城系统，采用 Nacos 服务注册发现、Gateway 网关路由、OpenFeign 服务间调用等技术，实现了完整的电商购物流程。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2.5 | 基础框架 |
| Spring Cloud | 2023.0.1 | 微服务治理 |
| Spring Cloud Alibaba | 2023.0.1.0 | Nacos 服务注册发现 |
| Spring Cloud Gateway | - | API 网关 |
| OpenFeign | - | 服务间 HTTP 调用 |
| MyBatis-Plus | 3.5.6 | ORM 框架 |
| MySQL | 8.x | 数据库 |
| Vue 3 | 3.x | 前端框架 |
| Element Plus | 2.x | UI 组件库 |
| Vite | 5.x | 前端构建工具 |

## 项目结构

```
online-shopping-springboot/
├── mall-common/          # 公共模块（实体基类、拦截器、异常处理）
├── mall-gateway/         # API 网关（端口 8080）
├── mall-user/            # 用户服务（端口 8081）
├── mall-product/         # 商品服务（端口 8082）
├── mall-order/           # 订单服务（端口 8083）
├── mall-front/           # 前端项目（Vue 3）
├── sql/                  # 数据库初始化脚本
└── images/               # 商品图片资源
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.9+
- MySQL 8.x
- Node.js 18+
- Nacos 2.3.2（可通过 Docker 部署）

### 1. 启动 Nacos

```bash
docker run -d --name nacos \
  -p 8848:8848 \
  -p 9848:9848 \
  -p 9849:9849 \
  -e MODE=standalone \
  nacos/nacos-server:v2.3.2
```

访问 http://localhost:8848/nacos 验证 Nacos 是否启动成功。

### 2. 初始化数据库

```bash
mysql -uroot -p < sql/init.sql
```

或导入备份：

```bash
mysql -uroot -p < D:/mall.sql
```

### 3. 启动后端服务

按以下顺序启动各服务：

```bash
# 1. 启动用户服务
cd mall-user && mvn spring-boot:run

# 2. 启动商品服务
cd mall-product && mvn spring-boot:run

# 3. 启动订单服务
cd mall-order && mvn spring-boot:run

# 4. 启动网关服务
cd mall-gateway && mvn spring-boot:run
```

### 4. 启动前端

```bash
cd mall-front
npm install
npm run dev
```

访问 http://localhost:5173 打开前端页面。

## 功能特性

### 用户端

- 用户注册/登录/登出
- 商品浏览（列表、详情、搜索、分类）
- 购物车管理（添加、修改、删除）
- 订单管理（创建、支付、确认收货、退货）
- 个人中心（信息管理、收货地址管理）

### 管理端

- 仪表盘（订单数、商品数、用户数统计）
- 商品管理（增删改查）
- 分类管理（增删改查）
- 公告管理（增删改查）
- 订单管理（查看列表、发货）

## 系统架构

```
                    ┌─────────────────┐
                    │   Vue 3 前端    │
                    │   localhost:5173 │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │     Gateway     │
                    │  localhost:8080  │
                    │  路由转发+鉴权   │
                    └────────┬────────┘
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
  ┌────────▼────────┐ ┌─────▼─────┐ ┌─────────▼────────┐
  │    mall-user    │ │mall-product│ │    mall-order    │
  │  localhost:8081 │ │localhost:8082│ │  localhost:8083  │
  │  用户/地址管理   │ │ 商品/分类  │ │  购物车/订单     │
  └────────┬────────┘ └─────┬─────┘ └─────────┬────────┘
           │                 │                 │
           │                 │    Feign 调用    │
           └─────────────────┼─────────────────┘
                             │
                    ┌────────▼────────┐
                    │  Nacos 注册中心  │
                    │  localhost:8848  │
                    └─────────────────┘
```

## 服务端口

| 服务 | 端口 | 说明 |
|------|------|------|
| mall-gateway | 8080 | API 网关，统一入口 |
| mall-user | 8081 | 用户服务 + 静态资源 |
| mall-product | 8082 | 商品服务 |
| mall-order | 8083 | 订单服务 |
| Nacos | 8848 | 服务注册发现 |
| 前端 | 5173 | Vue 开发服务器 |

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 普通用户 | zhangsan | user123 |

## 核心功能流程

### 购物流程

```
浏览商品 → 加入购物车 → 选择商品结算 → 创建订单 → 模拟支付
```

### 订单状态流转

```
创建订单 → 待付款(0) → 已付款(1) → 已发货(2) → 已完成(3)
                ↓                       ↓
            已取消(4)                已退货(5)
```

## 项目特点

1. **微服务架构**：服务独立部署，职责清晰
2. **服务注册发现**：使用 Nacos 实现服务自动注册与发现
3. **网关统一入口**：Gateway 负责路由转发和 Token 鉴权
4. **声明式服务调用**：使用 OpenFeign 简化服务间 HTTP 调用
5. **本地内存缓存**：使用 ConcurrentHashMap 缓存 Token 和热点数据
6. **前后端分离**：Vue 3 + Element Plus 独立前端项目

## License

MIT
