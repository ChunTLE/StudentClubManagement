# 学生社团管理系统

## 项目简介
这是一个基于Spring Boot + MyBatis Plus的学生社团管理系统，提供用户登录注册、社团管理、活动管理等功能。

## 技术栈
- **后端框架**: Spring Boot 3.4.1
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis Plus 3.5.12
- **构建工具**: Maven
- **Java版本**: JDK 17

## 功能特性
- ✅ 用户登录注册
- ✅ 用户管理
- 🔄 社团管理（待实现）
- 🔄 活动管理（待实现）
- 🔄 财务管理（待实现）
- 🔄 邮件通知（待实现）

## 快速开始

### 1. 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置
1. 创建MySQL数据库
2. 执行 `scmsystem.sql` 脚本初始化数据库
3. 修改 `src/main/resources/application.yml` 中的数据库连接信息

### 3. 运行项目
```bash
# 克隆项目
git clone [项目地址]

# 进入项目目录
cd StudentClubManagement

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 4. 访问应用
- 应用地址: http://localhost:8080

## 项目结构
```
StudentClubManagement/
├── src/main/java/cn/pcs/studentclubmanagement/
│   ├── controller/          # 控制器层
│   │   ├── AuthController.java    # 认证控制器
│   │   └── UserController.java    # 用户控制器
│   ├── dto/                # 数据传输对象
│   │   ├── LoginRequest.java      # 登录请求
│   │   ├── LoginResponse.java     # 登录响应
│   │   └── RegisterRequest.java   # 注册请求
│   ├── entity/             # 实体类
│   │   ├── User.java             # 用户实体
│   │   └── Result.java           # 统一响应结果
│   ├── mapper/             # 数据访问层
│   │   └── UserMapper.java       # 用户Mapper
│   ├── service/            # 服务层
│   │   ├── UserService.java      # 用户服务接口
│   │   └── impl/
│   │       └── UserServiceImpl.java  # 用户服务实现
│   └── StudentClubManagementApplication.java  # 启动类
├── src/main/resources/
│   ├── application.yml     # 配置文件
├── scmsystem.sql          # 数据库初始化脚本
└── README.md              # 项目说明
```

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 用户管理接口
- `GET /api/users` - 获取所有用户
- `GET /api/users/search?username=xxx` - 根据用户名查询用户

## 测试账号
系统预置了以下测试账号：
- 管理员: wangwu / 123456
- 社团领导: lisi / 123456
- 普通成员: zhangsan / 123456

## 开发计划
- [ ] 完善社团管理功能
- [ ] 实现活动管理
- [ ] 添加财务管理
- [ ] 集成邮件通知
- [ ] 添加权限控制
- [ ] 优化前端界面

## 贡献指南
1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建 Pull Request

## 许可证
MIT License 