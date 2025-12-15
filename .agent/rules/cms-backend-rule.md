---
trigger: always_on
---

# 项目基本规则 (Project Basic Rules)

## 1. 技术栈 (Technology Stack)
- **开发语言**: Java 17
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0+
- **ORM 框架**: MyBatis Plus 3.5.5
- **数据库迁移**: Flyway 9.22.3
- **安全框架**: Spring Security + JWT
- **API 文档**: Knife4j (OpenAPI 3)
- **实时通信**: Spring WebSocket (STOMP)
- **工具库**: Lombok, Hutool (可选), Druid

## 2. 项目结构 (Project Structure)
标准的 Spring Boot 分层架构：
```
com.mediguard.centralstation
├── common          // 全局配置、常量、异常处理、工具类
├── config          // Spring 配置类 (Security, WebSocket, Swagger 等)
├── controller      // REST 控制器层
├── entity          // 数据库实体类 (MyBatis Plus)
├── mapper          // MyBatis Mapper 接口
├── service         // 业务逻辑接口
│   └── impl        // 业务逻辑实现
├── model           // 数据传输对象、视图对象、枚举
│   ├── dto         // Data Transfer Objects (输入参数)
│   └── vo          // View Objects (输出响应)
└── security        // 认证与授权逻辑
```

## 3. 编码规范 (Coding Standards)

### 3.1 命名约定
- **类名**: PascalCase (大驼峰，例如 `PatientController`)
- **方法/变量**: camelCase (小驼峰，例如 `getPatientById`)
- **常量**: UPPER_SNAKE_CASE (全大写下划线，例如 `MAX_RETRY_COUNT`)
- **数据库表**: snake_case (下划线分隔，例如 `patient_info`)

### 3.2 实体类与 Lombok
- 使用 `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` 注解实体类。
- 使用 `@TableName` 指定数据库表名。
- 使用 `@TableId` 指定主键。
- 使用 `@Builder` 用于构建复杂对象。

### 3.3 控制器与 API
- **路径**: `/api/v1/{resource}` (或遵循 `api.md` 中的 `/api/{resource}`)
- **响应格式**: 始终使用标准的 `Result<T>` 或 `ApiResponse<T>` 对象包装响应。
  ```json
  {
    "code": 200,
    "message": "success",
    "data": { ... }
  }
  ```
- 使用 `@RestController` 和 `@RequestMapping`。
- 使用 `@Operation` (Swagger/Knife4j) 注解进行接口文档说明。

### 3.4 业务层 (Service)
- 必须定义接口 `IService` 和实现类 `ServiceImpl`。
- 写操作必须使用 `@Transactional` 注解。
- 业务逻辑应位于 Service 层，严禁写在 Controller 层。

### 3.5 异常处理
- 使用全局异常处理器 (`@RestControllerAdvice`)。
- 抛出自定义业务异常 (如 `BusinessException`)，避免直接抛出通用的 `RuntimeException`。

## 4. 数据库与迁移
- **Flyway**: 所有数据库 Schema 变更必须在 `src/main/resources/db/migration` 中进行版本控制。
- **命名规范**: `V{version}__{description}.sql` (例如 `V1.0.1__create_patient_table.sql`)。
- **MyBatis Plus**: 使用 `BaseMapper<T>` 进行基础 CRUD 操作。仅在复杂查询时编写自定义 XML Mapper。

## 5. 安全 (Security)
- **JWT**: 无状态认证。Token 通过 HTTP Header `Authorization: Bearer <token>` 传递。
- **密码**: 存储前必须使用 BCrypt 加密。
- **CORS**: 配置允许的跨域来源 (开发环境可设为 `*` 或指定前端地址)。

## 6. Git 工作流
- **主分支 (Main/Master)**: 生产环境代码。
- **开发分支 (Develop)**: 集成测试分支。
- **特性分支 (Feature)**: `feature/{feature-name}`。
- **提交信息**: 遵循 Conventional Commits (例如 `feat: add patient login`, `fix: resolve websocket timeout`)。
