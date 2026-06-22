# Git 版本管理指南

> 本文档记录了项目的 Git 仓库初始化、首次推送，以及日常代码提交推送的完整流程。

---

## 一、首次创建仓库并推送

### 1.1 前置条件

- 已安装 Git（`git --version` 验证）
- 已配置 Git 用户信息（只需配置一次）
- 已在 GitHub 上创建好空仓库
- 已配置 SSH Key 或 Token 认证

```bash
# 配置用户信息（只需执行一次）
git config --global user.name "你的用户名"
git config --global user.email "你的邮箱"
```

### 1.2 初始化本地仓库

```bash
# 进入项目根目录
cd D:\CodeWorkPlace\IdeaCode\online-shopping-springboot

# 初始化 Git 仓库
git init
```

### 1.3 创建 .gitignore 文件

在项目根目录创建 `.gitignore`，排除不需要提交的文件：

```gitignore
# Maven 构建产物
target/
*.jar
*.war
*.class

# IDE 配置文件
.idea/
*.iml
.vscode/
.settings/
.project
.classpath
*.swp

# 操作系统文件
.DS_Store
Thumbs.db

# 前端依赖和构建产物
mall-front/node_modules/
mall-front/dist/

# 日志文件
*.log

# Redis 数据文件
dump.rdb
```

### 1.4 添加文件并提交

```bash
# 添加所有文件到暂存区
git add .

# 查看暂存状态（确认文件列表）
git status

# 首次提交
git commit -m "feat: 初始化简化版微服务商城项目

- 5个模块: mall-common, mall-gateway, mall-user, mall-product, mall-order
- 简化架构: 去掉Nacos/JWT/Resilience4J, 用Session+Cookie认证
- 保留Feign简化版(无熔断降级), 地址写死在配置文件
- Gateway纯路由转发, 无鉴权过滤器
- Redis做缓存+Session存储
- 前端Vue3适配Session模式
- SQL初始化脚本+API测试报告"
```

### 1.5 关联远程仓库并推送

```bash
# 添加远程仓库地址（SSH 方式）
git remote add origin git@github.com:LeRising/online-shopping-springboot.git

# 将本地分支重命名为 main
git branch -M main

# 推送到远程仓库
git push -u origin main
```

> **说明**：`-u origin main` 表示将本地 `main` 分支与远程 `origin/main` 关联，后续直接执行 `git push` 即可。

---

## 二、日常代码修改后的提交推送流程

每次修改代码后，按以下步骤提交并推送。

### 2.1 查看修改内容

```bash
# 查看哪些文件被修改了
git status

# 查看具体修改了什么内容（可选）
git diff
```

### 2.2 添加修改到暂存区

```bash
# 方式一：添加所有修改的文件
git add .

# 方式二：只添加指定文件
git add 文件路径

# 示例：
git add mall-product/src/main/java/com/mall/product/service/impl/ProductServiceImpl.java
```

### 2.3 提交

```bash
git commit -m "类型: 简要描述修改内容"
```

**提交信息规范**：

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat: 添加商品搜索功能` |
| `fix` | 修复 Bug | `fix: 修复 Redis 序列化 LocalDateTime 失败` |
| `refactor` | 重构代码 | `refactor: 简化 Feign 客户端，去掉熔断降级` |
| `docs` | 文档更新 | `docs: 添加 API 测试报告` |
| `style` | 代码格式调整 | `style: 统一代码缩进风格` |
| `test` | 测试相关 | `test: 添加用户注册接口测试` |
| `chore` | 构建/配置变更 | `chore: 更新 pom.xml 依赖版本` |

### 2.4 推送到远程仓库

```bash
# 推送到远程 main 分支
git push origin main
```

---

## 三、常用 Git 操作速查

### 3.1 查看历史提交记录

```bash
# 查看简洁版提交历史
git log --oneline

# 查看详细提交历史
git log

# 查看某个文件的修改历史
git log --follow -p 文件路径
```

### 3.2 撤销操作

```bash
# 撤销工作区的修改（未 add 之前）
git checkout -- 文件路径

# 撤销暂存区的修改（已 add，未 commit）
git reset HEAD 文件路径

# 撤销最近一次提交（保留修改）
git reset --soft HEAD~1

# 撤销最近一次提交（丢弃修改，慎用！）
git reset --hard HEAD~1
```

### 3.3 分支操作

```bash
# 查看所有分支
git branch -a

# 创建并切换到新分支
git checkout -b feature/新功能名

# 切换回 main 分支
git checkout main

# 合并分支到 main
git merge feature/新功能名

# 删除本地分支
git branch -d feature/新功能名

# 删除远程分支
git push origin --delete feature/新功能名
```

### 3.4 拉取远程更新

```bash
# 拉取远程最新代码并合并
git pull origin main

# 仅获取远程更新（不合并）
git fetch origin
```

---

## 四、常见问题处理

### 4.1 推送时提示 "Authentication failed"

原因：Token 过期或 SSH Key 未配置。

**SSH 方式解决**：
```bash
# 检查是否有 SSH Key
ls ~/.ssh/id_rsa.pub

# 如果没有，生成一个
ssh-keygen -t rsa -b 4028 -C "你的邮箱"

# 复制公钥内容，添加到 GitHub -> Settings -> SSH Keys
cat ~/.ssh/id_rsa.pub

# 测试连接
ssh -T git@github.com
```

### 4.2 推送时提示 "rejected (non-fast-forward)"

原因：远程仓库有本地没有的提交。

```bash
# 先拉取远程更新，再推送
git pull origin main --rebase
git push origin main
```

### 4.3 提交信息写错了

```bash
# 修改最近一次提交信息
git commit --amend -m "正确的提交信息"

# 强制推送（仅限未推送到远程的情况）
git push --force-with-lease origin main
```

### 4.4 不小心提交了不需要的文件

```bash
# 从暂存区移除（保留文件）
git reset HEAD 文件路径

# 从 Git 追踪中移除（保留本地文件）
git rm --cached 文件路径

# 将文件名添加到 .gitignore 后重新提交
```

---

## 五、本项目的分支策略（建议）

```
main          ← 生产环境代码，始终保持可运行状态
  └── dev     ← 开发分支，日常开发在此分支进行
       └── feature/xxx   ← 功能分支，开发完成后合并到 dev
```

```bash
# 创建 dev 分支
git checkout -b dev
git push -u origin dev

# 开发新功能时从 dev 创建功能分支
git checkout dev
git checkout -b feature/新功能名

# 功能完成后合并到 dev
git checkout dev
git merge feature/新功能名
git push origin dev

# dev 稳定后合并到 main
git checkout main
git merge dev
git push origin main
```
