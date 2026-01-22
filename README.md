# Ai-WanAndroid

一个基于Jetpack Compose和现代Android架构开发的WanAndroid客户端应用，采用MVVM架构模式，提供完整的Android技术知识平台功能。

## 项目简介

Ai-WanAndroid是一个通过AI编程工具TRAE生成的现代化Android应用，基于WanAndroid开放API构建。项目采用最新的Android开发技术栈，包括Jetpack Compose、Hilt依赖注入、Kotlin协程等，实现了完整的用户认证、文章浏览、收藏管理、搜索等功能。

## 技术栈

### 核心框架
- **Kotlin**: 2.0.0
- **Android Gradle Plugin**: 8.7.2
- **Jetpack Compose**: 2024.04.01
- **Hilt**: 2.49 (依赖注入框架)
- **Kotlin Coroutines**: 1.7.3 (协程)

### 网络请求
- **Retrofit**: 2.9.0 (RESTful API客户端)
- **OkHttp**: 4.12.0 (HTTP客户端)
- **Gson**: 2.10.1 (JSON解析)

### UI组件
- **Jetpack Compose**: 声明式UI框架
- **Material Design 3**: Material3设计规范
- **Coil**: 2.7.0 (图片加载库)
- **Accompanist SwipeRefresh**: 0.32.0 (下拉刷新)

### 导航与状态管理
- **Navigation Compose**: 2.7.7 (页面导航)
- **Hilt Navigation Compose**: 1.2.0 (导航依赖注入)

### 数据存储
- **DataStore**: 1.0.0 (轻量级数据存储)

### 测试框架
- **JUnit**: 4.13.2 (单元测试)
- **Espresso**: 3.5.1 (UI测试)

## 项目架构

### 整体架构设计

项目采用**MVVM (Model-View-ViewModel)**架构模式，结合**Clean Architecture**思想，实现清晰的分层架构：

```
┌─────────────────────────────────────────┐
│           UI Layer (Compose)          │
│  - Pages (HomePage, MinePage, etc.)  │
│  - Components (ArticleItem, etc.)     │
│  - ViewModels                       │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Repository Layer              │
│  - NetworkRepository                │
│  - DataCacheManager                 │
│  - UserManager                     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Network Layer                │
│  - ApiService                     │
│  - GlobalErrorInterceptor           │
│  - ErrorHandler                    │
└─────────────────────────────────────┘
```

### 模块划分

#### 1. UI层 (ui)
- **pages**: 页面组件
  - HomePage: 首页（Banner轮播 + 文章列表）
  - SquarePage: 广场页面
  - ProjectPage: 项目页面
  - NavigationPage: 导航页面
  - MinePage: 我的页面
  - LoginPage: 登录页面
  - RegisterPage: 注册页面
  - SearchPage: 搜索页面
  - CollectPage: 收藏页面
  - CollectAddPage: 添加收藏页面
  - CollectEditPage: 编辑收藏页面
  - ArticleDetailPage: 文章详情页面

- **components**: 可复用UI组件
  - ArticleItem: 文章列表项
  - BannerCarousel: Banner轮播图
  - BottomNavigationBar: 底部导航栏
  - CollectIcon: 收藏图标
  - CollectItem: 收藏列表项
  - ErrorDialog: 错误对话框
  - FlowLayout: 流式布局
  - GlobalErrorHandler: 全局错误处理器
  - LoginRequiredDialog: 登录提示对话框
  - ProjectItem: 项目列表项
  - SearchBox: 搜索框
  - Skeleton: 骨架屏组件

- **viewmodel**: 视图模型
  - HomeViewModel: 首页业务逻辑
  - SquareViewModel: 广场业务逻辑
  - ProjectViewModel: 项目业务逻辑
  - NavigationViewModel: 导航业务逻辑
  - MineViewModel: 我的页面业务逻辑
  - LoginViewModel: 登录业务逻辑
  - RegisterViewModel: 注册业务逻辑
  - SearchViewModel: 搜索业务逻辑
  - CollectViewModel: 收藏业务逻辑

- **state**: UI状态管理
  - HomeUiState, SquareUiState, ProjectUiState等

- **theme**: 主题配置
  - Color: 颜色定义
  - Type: 字体排版
  - Shape: 形状定义
  - Spacing: 间距定义
  - Theme: 主题配置

#### 2. 网络层 (network)
- **api**: API接口定义
  - ApiService: 完整的WanAndroid API接口

- **model**: 数据模型
  - ApiResponse: API响应基类
  - Article: 文章模型
  - Banner: Banner模型
  - LoginResponse: 登录响应
  - RegisterResponse: 注册响应
  - ProjectCategory: 项目分类
  - NavigationGroup: 导航分组
  - Friend: 常用网站/热词

- **repository**: 数据仓库
  - NetworkRepository: 网络请求统一管理

- **di**: 依赖注入模块
  - NetworkModule: 网络层依赖注入配置

- **interceptor**: 拦截器
  - GlobalErrorInterceptor: 全局错误拦截器

- **exception**: 异常处理
  - ApiException: API异常基类
  - NotLoginException: 未登录异常
  - NetworkException: 网络异常
  - TimeoutException: 超时异常
  - ServerException: 服务器异常
  - ParseException: 解析异常
  - ErrorCode: 错误码定义
  - ErrorHandler: 错误处理器
  - ErrorLogger: 错误日志记录

#### 3. 数据层 (data)
- **UserManager**: 用户信息管理
- **CollectStateManager**: 收藏状态管理

#### 4. 缓存层 (cache)
- **DataCacheManager**: 数据缓存管理器
- **CacheModule**: 缓存依赖注入配置
- **CacheConfig**: 缓存配置
- **CacheKeys**: 缓存键定义

#### 5. 导航层 (navigation)
- **AppNavigation**: 应用导航配置
- **MainTabNavigation**: 主标签页导航
- **NavigationRoutes**: 导航路由定义
- **NavigationArguments**: 导航参数定义

#### 6. 工具层 (utils)
- **AppContext**: 应用上下文
- **LogUtils**: 日志工具
- **ToastUtils**: Toast工具

### 数据流

```
用户交互 → UI组件 → ViewModel → Repository → Network → API
    ↓           ↓          ↓          ↓
  State更新   UI刷新    缓存更新   数据存储
```

## 主要功能模块

### 1. 用户认证模块
- **登录功能**: 用户名密码登录
- **注册功能**: 新用户注册
- **退出登录**: 清除用户信息
- **Cookie管理**: 自动管理登录Cookie
- **登录状态检测**: 自动判断登录状态

### 2. 首页模块
- **Banner轮播**: 顶部轮播图展示
- **置顶文章**: 置顶文章列表
- **文章列表**: 分页加载文章
- **下拉刷新**: 刷新最新文章
- **上拉加载**: 自动加载更多

### 3. 广场模块
- **广场文章**: 用户分享的文章列表
- **搜索入口**: 快速跳转搜索页面
- **分页加载**: 支持分页浏览

### 4. 项目模块
- **项目分类**: 按分类浏览项目
- **项目列表**: 分类的项目文章
- **分类切换**: 快速切换不同分类
- **分页加载**: 支持分页浏览

### 5. 导航模块
- **导航数据**: 展示网站导航
- **分组展示**: 按分组组织导航链接
- **随机颜色**: 为每个导航项生成随机颜色

### 6. 我的模块
- **用户信息**: 显示登录用户信息
- **收藏管理**: 进入收藏列表
- **退出登录**: 清除用户数据

### 7. 收藏模块
- **收藏列表**: 查看已收藏文章
- **收藏文章**: 收藏站内/站外文章
- **取消收藏**: 取消已收藏文章
- **编辑收藏**: 修改收藏信息
- **添加收藏**: 添加站外文章到收藏

### 8. 搜索模块
- **文章搜索**: 按关键词搜索文章
- **搜索热词**: 显示热门搜索词
- **搜索历史**: 记录搜索历史
- **分页加载**: 分页显示搜索结果

### 9. 文章详情模块
- **文章浏览**: WebView加载文章内容
- **收藏操作**: 快速收藏/取消收藏
- **返回导航**: 返回上一页

## 核心特性

### 1. 网络请求异常统一处理
- 统一错误码定义（SUCCESS、ERROR_NOT_LOGIN、ERROR_NETWORK等）
- 自定义异常类（ApiException、NotLoginException、NetworkException等）
- 全局错误拦截器自动拦截所有API响应
- 错误日志记录系统
- 标准化错误展示对话框

### 2. 全局错误处理机制
- 使用SharedFlow发送全局错误事件
- 根据错误类型显示不同的错误提示
- 未登录错误自动跳转登录页面
- 友好的错误提示信息

### 3. 数据缓存策略
- 使用DataCacheManager实现数据缓存
- 支持内存缓存和持久化缓存
- 可配置的缓存过期时间
- 减少网络请求，提升用户体验

### 4. 下拉刷新与分页加载
- 使用Accompanist SwipeRefresh实现下拉刷新
- 自动检测滚动到底部触发加载更多
- 骨架屏显示加载状态
- 平滑的加载动画

### 5. Cookie自动管理
- 自动保存服务器返回的Cookie
- 自动在请求中添加Cookie头
- 支持登录状态持久化

### 6. 骨架屏加载状态
- 统一的骨架屏组件
- 不同页面使用不同的骨架屏布局
- 提升加载体验

### 7. Material Design 3主题
- 完整的色彩体系（Primary、Secondary、Success、Warning、Error等）
- 统一的形状定义（圆角、边框等）
- 标准化的间距和排版
- 符合WCAG 2.1可访问性标准

### 8. 响应式UI设计
- 支持不同屏幕尺寸
- 自适应布局
- 流式布局组件（FlowLayout）
- 文本省略号截断

### 9. 依赖注入
- 使用Hilt实现依赖注入
- 模块化的依赖配置
- 单例管理网络组件
- 简化测试和代码维护

## 开发环境配置

### 系统要求
- **操作系统**: Windows / macOS / Linux
- **JDK版本**: JDK 11或更高版本
- **Android Studio**: Arctic Fox (2020.3.1) 或更高版本
- **Gradle**: 8.7.2

### SDK版本
- **compileSdk**: 34
- **minSdk**: 24 (Android 7.0)
- **targetSdk**: 34 (Android 14)

### 构建配置
- **Java版本**: 11
- **Kotlin版本**: 2.0.0
- **JVM参数**: -Xmx2048m -Dfile.encoding=UTF-8
- **R类命名空间**: android.nonTransitiveRClass=true

### 依赖管理
- **Gradle版本目录**: 使用libs.versions.toml统一管理依赖版本
- **Maven仓库**: 使用阿里云Maven镜像加速依赖下载
  - https://maven.aliyun.com/repository/google/
  - https://maven.aliyun.com/repository/central/

### 网络配置
- **网络权限**: INTERNET、ACCESS_NETWORK_STATE
- **网络安全配置**: network_security_config.xml
- **API基础URL**: https://www.wanandroid.com/
- **请求超时**: 30秒

### 构建工具
- **Gradle Wrapper**: 已包含
- **Kotlin Compose Compiler**: 2.0.0
- **Hilt Compiler**: 2.49
- **KAPT**: Kotlin注解处理器

## 项目结构

```
Ai-WanAndroid/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gradle/aicodeapp/
│   │   │   │   ├── cache/              # 缓存层
│   │   │   │   │   ├── di/
│   │   │   │   │   └── DataCacheManager.kt
│   │   │   │   ├── data/              # 数据层
│   │   │   │   │   ├── CollectStateManager.kt
│   │   │   │   │   └── UserManager.kt
│   │   │   │   ├── navigation/        # 导航层
│   │   │   │   │   ├── AppNavigation.kt
│   │   │   │   │   ├── MainTabNavigation.kt
│   │   │   │   │   ├── NavigationRoutes.kt
│   │   │   │   │   └── NavigationArguments.kt
│   │   │   │   ├── network/           # 网络层
│   │   │   │   │   ├── api/
│   │   │   │   │   ├── di/
│   │   │   │   │   ├── exception/
│   │   │   │   │   ├── interceptor/
│   │   │   │   │   ├── model/
│   │   │   │   │   └── repository/
│   │   │   │   ├── ui/                # UI层
│   │   │   │   │   ├── components/
│   │   │   │   │   ├── pages/
│   │   │   │   │   ├── state/
│   │   │   │   │   ├── theme/
│   │   │   │   │   └── viewmodel/
│   │   │   │   ├── utils/             # 工具类
│   │   │   │   │   ├── AppContext.kt
│   │   │   │   │   ├── LogUtils.kt
│   │   │   │   │   └── ToastUtils.kt
│   │   │   │   ├── AppApplication.kt
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/                  # 资源文件
│   │   │   │   ├── drawable/
│   │   │   │   ├── mipmap-*/
│   │   │   │   ├── values/
│   │   │   │   └── xml/
│   │   │   └── AndroidManifest.xml
│   │   └── test/                    # 测试代码
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml            # 版本目录
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── install.bat
└── settings.gradle.kts
```

## 快速开始

### 克隆项目
```bash
git clone https://github.com/liupengchao92/Ai-WanAndroid.git
cd Ai-WanAndroid
```

### 安装依赖
```bash
./gradlew build
```

### 运行项目
1. 使用Android Studio打开项目
2. 等待Gradle同步完成
3. 连接Android设备或启动模拟器
4. 点击Run按钮运行应用

## API文档

本项目使用[WanAndroid开放API](https://www.wanandroid.com/blog/show/2)。

主要API接口：
- 用户登录/注册
- 首页文章列表
- Banner轮播图
- 广场文章
- 项目分类/列表
- 导航数据
- 文章搜索
- 收藏管理

## 贡献指南

欢迎提交Issue和Pull Request！

## 许可证

本项目采用MIT许可证。

## 联系方式

- 作者: 鹏超 刘
- 邮箱: 876367352@qq.com
- GitHub: https://github.com/liupengchao92

## 更新日志

### 最新更新
- 实现网络请求异常及服务器返回错误的统一处理机制
- 修复多个关键bug并优化用户体验
- 首页轮播图圆角样式优化
- 收藏页面图标适配与应用色彩体系重构
- 我的页面图标全面优化

## 致谢

感谢WanAndroid提供的开放API服务。
