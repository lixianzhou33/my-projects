# 我的项目作品集

> 作者：刘植本 | 学号：230054470246  
> 包含 Java 程序、Java Web 应用和前端网页等多个课程项目

---

## 项目目录

| 项目 | 技术栈 | 说明 |
|------|--------|------|
| [company-personnel-system](./company-personnel-system/) | Java + MySQL + JDBC | 公司人员管理系统 |
| [anime-website](./anime-website/) | HTML + CSS + JavaScript | 动漫主题前端网站 |
| [student-management-system](./student-management-system/) | Java Web (Servlet + JSP) + MySQL | 学生信息管理系统 |

---

## 1. 公司人员管理系统

### 技术栈
- **语言**：Java
- **数据库**：MySQL 5.7
- **连接方式**：JDBC

### 功能说明
- 员工信息管理（增删改查）
- 不同职位薪资计算（经理、兼职销售、兼职技术员）
- 用户登录认证
- 数据持久化存储到 MySQL 数据库

### 项目结构
```
company-personnel-system/
├── lzb/
│   ├── CompanySystem.java    # 主程序入口
│   ├── Person.java           # 人员抽象基类
│   ├── Manager.java          # 经理类
│   ├── User.java             # 用户实体类
│   ├── SalesManager.java     # 销售经理类
│   ├── PartTimeSalesman.java # 兼职销售类
│   ├── PartTimeTech.java     # 兼职技术员类
│   └── lib/
│       └── mysql-connector-java-5.1.39-bin.jar
└── 公司人员管理项目文档.docx
```

---

## 2. 动漫主题网站

### 技术栈
- **前端**：HTML5 + CSS3 + JavaScript
- **主题**：动漫《剑来》

### 页面功能
- 首页展示
- 剧情介绍
- 人物介绍
- 图片展示
- 视频播放
- 游客签到表单

### 项目结构
```
anime-website/
├── js/
│   ├── index.html       # 首页
│   ├── shouye.html      # 首页内容
│   ├── juqing.html      # 剧情页面
│   ├── renwu.html       # 人物页面
│   ├── tupian.html      # 图片页面
│   └── shipin.html      # 视频页面
├── css/
│   └── css.css          # 全局样式
└── img/                 # 图片资源 (21张)
```

---

## 3. 学生信息管理系统

### 技术栈
- **后端**：Java Servlet + JSP
- **前端**：Bootstrap + jQuery
- **数据库**：MySQL
- **服务器**：Tomcat

### 功能模块
- 管理员登录/退出
- 班级管理（增删改查）
- 学生信息管理（增删改查）
- 学生个人信息查看
- 验证码登录
- 分页查询

### 项目结构
```
student-management-system/
└── javaweb/
    ├── src/com/hello/
    │   ├── dao/          # 数据访问层 (AdminDao, StudentDao, ClazzDao)
    │   ├── entity/       # 实体类 (Admin, Student, Clazz)
    │   ├── service/      # 业务逻辑层
    │   ├── servlet/      # 控制器层 (Servlet)
    │   ├── filter/       # 过滤器 (登录过滤)
    │   └── utils/        # 工具类 (JDBC, 分页)
    └── web/
        ├── *.jsp         # 页面文件
        └── assets/       # 静态资源 (Bootstrap, CSS, JS, 图片)
```

---

## 使用说明

### 环境要求
- JDK 1.8+
- MySQL 5.7+
- Tomcat 8.5+ (学生管理系统)
- 浏览器 (动漫网站)

### 运行方式
1. **公司人员管理系统**：用 IDEA 打开项目，配置 MySQL 连接，运行 `CompanySystem.java`
2. **动漫网站**：直接用浏览器打开 `js/index.html`
3. **学生管理系统**：部署到 Tomcat，访问 `http://localhost:8080/javaweb/`
