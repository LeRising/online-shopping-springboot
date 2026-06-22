# 网上商城购物系统（简化微服务版）

基于 Spring Boot + Spring Cloud 的微服务架构网上商城系统。从原版项目简化而来，去除了 Nacos、JWT、Redis、Resilience4J 等复杂组件，采用 Token + 内存缓存方案，保留核心业务功能和 Feign 服务间调用。

---

## 项目简介

本项目是一个功能完整的网上商城后端系统，包含用户管理、商品浏览、购物车、订单管理、管理员后台等电商核心功能。前端基于 Vue 3 + Element Plus，后端采用微服务架构拆分为用户服务、商品服务、订单服务三个独立服务，通过 API 网关统一入口。

### 核心功能

**用户端**
- 用户注册、登录、登出（Token 认证，内存存储）
- 商品浏览：列表、详情、热门推荐、新品推荐
- 商品搜索：按分类筛选、关键字搜索、分页查询
- 购物车管理：添加、修改数量、选中/取消、删除
- 订单管理：创建订单、模拟支付、确认收货、申请退货
- 个人中心：查看/修改个人信息、管理收货地址

**管理员端**
- 仪表盘：统计商品数、订单数、用户数（跨服务 Feign 调用）
- 商品管理：增删改查，上下架状态
- 分类管理：单级分类结构
- 轮播图管理：增删改查
- 订单管理：查看所有订单、发货操作

---

## 技术栈

| 技术领域 | 技术选型 | 版本 |
|---------|---------|------|
| 后端框架 | Spring Boot | 3.2.5 |
| 微服务框架 | Spring Cloud | 2023.0.1 |
| API 网关 | Spring Cloud Gateway | — |
| 服务间调用 | OpenFeign | — |
| ORM 框架 | MyBatis-Plus | 3.5.6 |
| 数据库 | MySQL | 8.x |
| 用户认证 | Token + 内存存储 | — |
| API 文档 | Knife4j (OpenAPI 3) | 4.4.0 |
| 工具库 | Hutool | 5.8.26 |
| 前端框架 | Vue 3 + Element Plus | 3.4.x |
| 前端构建 | Vite | 5.2.x |
| 构建工具 | Maven | 3.9.x |
| 开发语言 | Java | 17 |

### 与原版项目的区别

| 维度 | 原版项目 | 本项目（简化版） |
|------|---------|----------------|
| 服务注册中心 | Nacos | ❌ 去掉，地址写死在配置文件 |
| 用户认证 | JWT Token + Redis 黑名单 | ✅ Token + 内存 Map 存储 |
| 网关鉴权 | JWT AuthGlobalFilter | ✅ Token AuthGlobalFilter（调用 mall-user 验证） |
| 熔断降级 | Resilience4J | ❌ 去掉 |
| Feign 调用 | Feign + Fallback + 超时重试 | ✅ 简化版，url 写死，无熔断 |
| 缓存 | Redis | ✅ 本地内存缓存（静态变量） |
| Session 存储 | Spring Session + Redis | ❌ 去掉，改用 Token |

---

## 项目结构

```
online-shopping-springboot/
├── pom.xml                              # 父 POM（统一版本管理）
├── sql/
│   └── init.sql                         # 数据库初始化脚本
├── images/                              # 商品图片、轮播图
│
├── mall-common/                         # 公共模块
│   └── src/main/java/com/mall/common/
│       ├── config/
│       │   ├── MybatisPlusConfig.java   # MyBatis-Plus 分页 + 自动填充
│       │   ├── WebMvcConfig.java        # 拦截器注册、白名单、CORS
│       │   └── LoginInterceptor.java    # 登录拦截器（验证 X-User-* Header）
│       ├── entity/
│       │   └── BaseEntity.java          # 实体基类（id, createTime, updateTime）
│       ├── exception/
│       │   ├── BusinessException.java   # 业务异常
│       │   └── GlobalExceptionHandler.java # 全局异常处理
│       ├── result/
│       │   ├── R.java                   # 统一响应封装
│       │   └── PageResult.java          # 分页结果封装
│       └── util/
│           └── UserContext.java         # 用户上下文（基于 ThreadLocal）
│
├── mall-gateway/                        # API 网关（:8080）
│   └── src/main/java/com/mall/gateway/
│       ├── GatewayApplication.java
│       └── filter/
│           └── AuthGlobalFilter.java    # 全局鉴权过滤器（调用 mall-user 验证 Token）
│
├── mall-user/                           # 用户服务（:8081）
│   └── src/main/java/com/mall/user/
│       ├── UserApplication.java
│       ├── config/
│       │   └── SecurityConfig.java      # Spring Security 配置（BCrypt）
│       ├── controller/
│       │   └── UserController.java      # 用户接口（注册/登录/登出/信息/地址/验证）
│       ├── service/
│       │   ├── UserService.java
│       │   └── UserAddressService.java
│       ├── mapper/
│       │   ├── UserMapper.java
│       │   └── UserAddressMapper.java
│       ├── entity/
│       │   ├── User.java                # 用户实体
│       │   └── UserAddress.java         # 收货地址实体
│       ├── dto/
│       │   ├── LoginDTO.java
│       │   ├── RegisterDTO.java
│       │   ├── UserDTO.java
│       │   └── AddressDTO.java
│       └── feign/
│           └── UserFeignService.java    # 对外暴露 Feign 接口
│
├── mall-product/                        # 商品服务（:8082）
│   └── src/main/java/com/mall/product/
│       ├── ProductApplication.java
│       ├── controller/
│       │   ├── ProductController.java       # 商品接口
│       │   ├── CategoryController.java      # 分类接口
│       │   ├── BannerController.java        # 轮播图接口
│       │   ├── AdminProductController.java  # 管理员-商品 CRUD
│       │   ├── AdminCategoryController.java # 管理员-分类 CRUD
│       │   └── AdminBannerController.java   # 管理员-轮播图 CRUD
│       ├── service/
│       │   ├── ProductService.java
│       │   ├── CategoryService.java
│       │   └── BannerService.java
│       ├── mapper/
│       │   ├── ProductMapper.java       # 含原子库存扣减 SQL
│       │   ├── CategoryMapper.java
│       │   └── BannerMapper.java
│       ├── entity/
│       │   ├── Product.java
│       │   ├── Category.java
│       │   └── Banner.java
│       ├── dto/
│       │   └── ProductDTO.java
│       └── feign/
│           └── ProductFeignService.java # 对外暴露 Feign 接口
│
├── mall-order/                          # 订单服务（:8083）
│   └── src/main/java/com/mall/order/
│       ├── OrderApplication.java
│       ├── controller/
│       │   ├── CartController.java           # 购物车接口
│       │   ├── OrderController.java          # 订单接口
│       │   ├── AdminOrderController.java     # 管理员-订单管理
│       │   └── AdminDashboardController.java # 管理员-仪表盘统计
│       ├── service/
│       │   ├── CartService.java
│       │   └── OrderService.java
│       ├── mapper/
│       │   ├── CartMapper.java
│       │   ├── OrderInfoMapper.java
│       │   └── OrderItemMapper.java
│       ├── entity/
│       │   ├── Cart.java
│       │   ├── OrderInfo.java
│       │   └── OrderItem.java
│       ├── dto/
│       │   ├── CreateOrderDTO.java
│       │   ├── OrderDTO.java
│       │   ├── OrderItemDTO.java
│       │   └── CartDTO.java
│       └── feign/
│           ├── ProductFeignClient.java   # 调用商品服务
│           └── UserFeignClient.java      # 调用用户服务
│
└── mall-front/                          # 前端项目（Vue 3）
    ├── src/
    │   ├── api/                         # API 接口封装
    │   ├── components/                  # 公共组件
    │   ├── router/                      # 路由配置
    │   ├── store/                       # Pinia 状态管理
    │   ├── utils/                       # 工具函数（Axios 封装）
    │   └── views/                       # 页面组件
    ├── package.json
    └── vite.config.js
```

---

## 系统架构

```
                        浏览器 / Vue 前端
                        (http://localhost:5173)
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Gateway (:8080)    │
                    │  路由转发 + Token鉴权 │
                    │  + CORS + 静态资源   │
                    └──┬──────┬──────┬────┘
                       │      │      │
            ┌──────────┘      │      └──────────┐
            ▼                 ▼                  ▼
    ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
    │ mall-user    │  │ mall-product │  │ mall-order   │
    │   (:8081)    │  │   (:8082)    │  │   (:8083)    │
    │  用户服务     │  │  商品服务     │  │  订单服务     │
    │ + 静态资源   │  │              │  │              │
    └──────┬───────┘  └──────┬───────┘  └──────┬───────┘
           │                 │                  │
           │                 │◄───Feign─────────┤
           │◄────────────────│───Feign──────────┤
           │                 │                  │
           └─────────────────┼──────────────────┘
                             │
                         ┌───┴───┐
                         │ MySQL │
                         └───────┘
```

### 认证流程

```
1. 用户登录 → mall-user 生成 Token（UUID），存入内存 ConcurrentHashMap
2. 返回 Token 给前端，前端存入 localStorage
3. 后续请求 → 前端在 Authorization Header 中携带 Bearer Token
4. 请求到达 Gateway → AuthGlobalFilter 拦截
5. 调用 mall-user /api/user/validate 验证 Token → 获取用户信息
6. 设置 X-User-Id / X-User-Username / X-User-Role Header → 转发到下游服务
7. 下游服务 LoginInterceptor 从 Header 读取用户信息 → 存入 UserContext（ThreadLocal）
```

### 服务间调用关系

```
mall-order ──Feign──→ mall-product
    │                     │
    │ getProduct()        │ 返回商品信息
    │ deductStock()       │ 扣减库存
    │ restoreStock()      │ 恢复库存
    │ countProducts()     │ 统计商品数
    │
    └──Feign──→ mall-user
                     │
                     │ getAddress()  → 返回收货地址
                     │ countUsers()  → 统计用户数
```

---

## 数据库设计

系统使用单一 MySQL 数据库 `mall`，共 8 张表：

```
user (用户表) ──1:N── user_address (收货地址表)
    │
    └──1:N── cart (购物车表) ──N:1── product (商品表) ──N:1── category (分类表)
    │
    └──1:N── order_info (订单表) ──1:N── order_item (订单明细表)

banner (轮播图表)  独立表
```

| 表名 | 说明 | 核心字段 |
|------|------|---------|
| `user` | 用户表 | username, password(BCrypt), nickname, role(0普通/1管理员) |
| `user_address` | 收货地址表 | user_id, name, phone, province, city, district, detail, is_default |
| `product` | 商品表 | name, price, stock, category_id, status(0下架/1上架), sales |
| `category` | 分类表 | name, parent_id(0=顶级), sort |
| `cart` | 购物车表 | user_id, product_id, quantity, checked |
| `order_info` | 订单表 | order_no(雪花算法), user_id, total_amount, status(0~5) |
| `order_item` | 订单明细表 | order_id, product_id, product_name(快照), price(快照) |
| `banner` | 轮播图表 | image, url, sort |

**订单状态流转**：
```
0(待付款) ──支付──→ 1(已付款) ──发货──→ 2(已发货) ──确认──→ 3(已完成)
    │                    │                    │
    └──取消──→ 4(已取消)  └──退货──→ 5(已退货)  └──退货──→ 5(已退货)
```

---

## 快速开始

### 环境要求

- JDK 17
- Maven 3.9+
- MySQL 8.x
- Node.js 18+（前端）

### 1. 初始化数据库

```bash
mysql -u root -p --default-character-set=utf8mb4 < sql/init.sql
```

### 2. 修改配置

各服务的 `application.yml` 中修改 MySQL 连接信息（默认 `localhost`，密码 `root`）。

### 3. 启动后端服务

```bash
# 按顺序启动
cd mall-user     && mvn spring-boot:run    # :8081
cd mall-product  && mvn spring-boot:run    # :8082
cd mall-order    && mvn spring-boot:run    # :8083
cd mall-gateway  && mvn spring-boot:run    # :8080
```

### 4. 启动前端

```bash
cd mall-front
npm install
npm run dev    # :5173
```

### 5. 访问

- 前端页面：`http://localhost:5173`
- API 网关：`http://localhost:8080`
- API 文档：
  - 用户服务：`http://localhost:8081/doc.html`
  - 商品服务：`http://localhost:8082/doc.html`
  - 订单服务：`http://localhost:8083/doc.html`

### 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 普通用户 | zhangsan | user123 |

---

## API 接口总览

### 用户服务 `/api/user`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/register` | 用户注册 | 否 |
| POST | `/login` | 用户登录 | 否 |
| POST | `/logout` | 用户登出 | 是 |
| GET | `/validate` | 验证 Token（供 Gateway 调用） | 否 |
| GET | `/info` | 获取用户信息 | 是 |
| PUT | `/info` | 更新用户信息 | 是 |
| GET | `/address/list` | 收货地址列表 | 是 |
| POST | `/address` | 新增收货地址 | 是 |
| PUT | `/address` | 修改收货地址 | 是 |
| DELETE | `/address/{id}` | 删除收货地址 | 是 |

### 商品服务 `/api/product`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/list` | 商品列表（分页+搜索） | 否 |
| GET | `/{id}` | 商品详情 | 否 |
| GET | `/hot` | 热门商品 | 否 |
| GET | `/new` | 新品推荐 | 否 |
| GET | `/category/list` | 分类列表 | 否 |

### 轮播图 `/api/banner`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/list` | 轮播图列表 | 否 |

### 购物车 `/api/cart`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/list` | 购物车列表 | 是 |
| POST | `/add` | 添加购物车 | 是 |
| PUT | `/update` | 修改数量 | 是 |
| DELETE | `/{id}` | 删除购物车项 | 是 |
| PUT | `/check/{id}` | 更新选中状态 | 是 |

### 订单 `/api/order`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/create` | 创建订单 | 是 |
| GET | `/list` | 我的订单列表 | 是 |
| GET | `/{id}` | 订单详情 | 是 |
| PUT | `/cancel/{id}` | 取消订单 | 是 |
| POST | `/pay/{id}` | 模拟支付 | 是 |
| PUT | `/confirm/{id}` | 确认收货 | 是 |
| PUT | `/return/{id}` | 申请退货 | 是 |

### 管理员接口 `/api/admin`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/dashboard/stats` | 仪表盘统计 |
| GET/POST/PUT/DELETE | `/product/**` | 商品 CRUD |
| GET/POST/PUT/DELETE | `/category/**` | 分类 CRUD |
| GET/POST/PUT/DELETE | `/banner/**` | 轮播图 CRUD |
| GET | `/order/list` | 所有订单列表 |
| PUT | `/order/ship/{id}` | 订单发货 |

---

## 许可证

本项目仅供学习交流使用。
