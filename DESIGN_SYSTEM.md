# 设计语言规范文档

## 1. 色彩方案

### 主色系
- **Primary**: #1E88E5 (蓝色) - 应用的主要品牌色
- **OnPrimary**: #FFFFFF - 主色上的文本颜色
- **PrimaryContainer**: #E3F2FD - 主色容器背景
- **OnPrimaryContainer**: #0D47A1 - 主色容器上的文本颜色

### 辅助色系
- **Secondary**: #03A9F4 (青色) - 用于次要操作和信息
- **OnSecondary**: #FFFFFF - 辅助色上的文本颜色
- **SecondaryContainer**: #E1F5FE - 辅助色容器背景
- **OnSecondaryContainer**: #01579B - 辅助色容器上的文本颜色

### 第三色系
- **Tertiary**: #9C27B0 (紫色) - 用于特殊功能和强调
- **OnTertiary**: #FFFFFF - 第三色上的文本颜色
- **TertiaryContainer**: #F3E5F5 - 第三色容器背景
- **OnTertiaryContainer**: #4A148C - 第三色容器上的文本颜色

### 状态色系
- **Error**: #E53935 (红色) - 错误状态
- **Success**: #43A047 (绿色) - 成功状态
- **Warning**: #FF9800 (橙色) - 警告状态
- **Info**: #1E88E5 (蓝色) - 信息状态

### 背景色系
- **Background**: #FAFAFA - 应用背景
- **Surface**: #FFFFFF - 卡片和组件背景
- **SurfaceVariant**: #F5F5F5 - 次要背景
- **OnSurface**: #212121 - 表面上的主要文本
- **OnSurfaceVariant**: #757575 - 表面上的次要文本

### 轮廓和分隔线
- **Outline**: #E0E0E0 - 轮廓线
- **OutlineVariant**: #EEEEEE - 次要轮廓线

## 2. 排版规范

### 字体家族
- 主要字体: FontFamily.Default (系统默认字体)

### 字号层级

| 类型 | 字号 | 字重 | 行高 | 字间距 | 用途 |
|------|------|------|------|--------|------|
| displayLarge | 57sp | W400 | 64sp | -0.25sp | 大标题和营销内容 |
| displayMedium | 45sp | W400 | 52sp | 0sp | 大标题和营销内容 |
| displaySmall | 36sp | W400 | 44sp | 0sp | 大标题和营销内容 |
| headlineLarge | 32sp | W400 | 40sp | 0sp | 页面和区块标题 |
| headlineMedium | 28sp | W400 | 36sp | 0sp | 页面和区块标题 |
| headlineSmall | 24sp | W400 | 32sp | 0sp | 页面和区块标题 |
| titleLarge | 22sp | W500 | 28sp | 0sp | 卡片标题和列表项标题 |
| titleMedium | 16sp | W500 | 24sp | 0.15sp | 卡片标题和列表项标题 |
| titleSmall | 14sp | W500 | 20sp | 0.1sp | 卡片标题和列表项标题 |
| bodyLarge | 16sp | W400 | 24sp | 0.5sp | 主要内容 |
| bodyMedium | 14sp | W400 | 20sp | 0.25sp | 主要内容 |
| bodySmall | 12sp | W400 | 16sp | 0.4sp | 辅助文本 |
| labelLarge | 14sp | W500 | 20sp | 0.1sp | 按钮、标签等 |
| labelMedium | 12sp | W500 | 16sp | 0.5sp | 按钮、标签等 |
| labelSmall | 11sp | W500 | 16sp | 0.5sp | 按钮、标签等 |

## 3. 组件样式

### 卡片
- **圆角**: 12dp (Medium)
- **阴影**: 低阴影 (2dp)
- **内边距**: 16dp (CardPadding)
- **背景**: Surface (#FFFFFF)

### 按钮
- **高度**: 48dp (ButtonHeight)
- **圆角**: 12dp (Medium)
- **内边距**: 16dp (Large) 水平, 12dp (Medium) 垂直
- **文字样式**: labelLarge

### 列表项
- **高度**: 自适应
- **内边距**: 16dp (Medium) 垂直, 16dp (Large) 水平
- **点击效果**: 缩放动画 (0.95f) + 背景色变化
- **分隔线**: 1dp, 颜色为 OutlineVariant

### 图标
- **尺寸**: 24dp (IconMedium) - 标准尺寸
- **颜色**: 主要使用 OnSurface (#212121) 或 Primary (#1E88E5)
- **背景**: 可选使用 PrimaryContainer 背景

## 4. 间距与布局规则

### 间距系统 (基于4dp网格)

| 名称 | 值 | 用途 |
|------|-----|------|
| ExtraSmall | 4dp | 微小间距，如图标与文本之间 |
| Small | 8dp | 小间距，如列表项内元素间距 |
| Medium | 12dp | 中等间距，如组件内边距 |
| Large | 16dp | 大间距，如页面边距、卡片间距 |
| ExtraLarge | 24dp | 特大间距，如区块间距 |
| Huge | 32dp | 巨大间距，如页面顶部间距 |
| ExtraHuge | 48dp | 超大间距，如特殊场景 |

### 页面布局
- **页面边距**: 16dp (ScreenPadding) 水平
- **顶部间距**: 24dp (ExtraLarge) 或 32dp (Huge)
- **区块间距**: 16dp (Large) 或 24dp (ExtraLarge)
- **列表项间距**: 8dp (ListItemSpacing)

### 响应式布局
- **移动端**: 单列布局
- **平板**: 双列布局（视内容而定）
- **桌面端**: 多列布局（视内容而定）

## 5. 交互反馈机制

### 点击效果
- **缩放动画**: 点击时缩放到 0.95f，释放后恢复
- **背景色变化**: 点击时背景色变为 PrimaryContainer (透明度 0.5f)
- **动画曲线**: spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)

### 加载动画
- **样式**: CircularProgressIndicator
- **尺寸**: 32dp 或 56dp
- **颜色**: Primary (#1E88E5)
- **动画**: 无限循环的缩放动画

### 错误提示
- **样式**: 卡片式 Snackbar
- **背景色**: ErrorContainer (#FFEBEE)
- **文本色**: OnErrorContainer (#B71C1C)
- **动画**: 淡入淡出 + 缩放动画

### 列表项动画
- **进入动画**: 淡入 + 缩放 (初始缩放 0.9f)
- **动画时长**: 300ms

## 6. 图标系统

### 图标风格
- 使用 Material Icons 图标库
- 统一使用填充式图标
- 保持图标风格一致性

### 图标尺寸

| 名称 | 值 | 用途 |
|------|-----|------|
| IconSmall | 16dp | 小型图标，如状态指示 |
| IconMedium | 24dp | 标准图标，如菜单项、按钮 |
| IconLarge | 32dp | 大型图标，如用户头像背景 |
| IconExtraLarge | 48dp | 特大型图标，如特殊场景 |
| IconHuge | 80dp | 巨大图标，如用户头像 |

### 图标背景
- **圆形背景**: 使用 CircleShape
- **背景色**: 可选使用 PrimaryContainer (透明度 0.3f)
- **阴影**: 低阴影 (2dp) 或中阴影 (4dp)

## 7. 卡片样式规范

### 标准卡片
- **圆角**: 12dp (Medium)
- **阴影**: 低阴影 (2dp)
- **内边距**: 16dp (CardPadding)
- **背景**: Surface (#FFFFFF)

### 渐变卡片
- **背景**: 垂直渐变，从 PrimaryContainer 到 PrimaryContainer (透明度 0.7f)
- **其他属性**: 与标准卡片一致

### 列表卡片
- **圆角**: 12dp (Medium)
- **阴影**: 无阴影或低阴影 (2dp)
- **内边距**: 16dp (Medium) 垂直, 16dp (Large) 水平
- **背景**: Surface (#FFFFFF)

## 8. 布局模式

### 首页布局
- **顶部**: 状态栏 + 标题栏 (可选)
- **Banner区域**: 高度约 180dp，圆角 12dp
- **热门卡片区域**: 网格布局，卡片间距 12dp
- **文章列表区域**: 垂直列表，列表项间距 8dp
- **底部**: 底部导航栏 (56dp 高度)

### "我的页面"布局
- **顶部**: 状态栏 + 标题栏 (可选)
- **用户信息区域**: 卡片式布局，包含头像、昵称、状态
- **菜单区域**: 卡片式布局，包含多个菜单项
- **底部**: 底部导航栏 (56dp 高度)

## 9. 设计原则

### 一致性
- 所有UI元素在不同页面间保持一致的视觉风格
- 遵循统一的色彩、排版、组件和间距规范

### 简洁性
- 保持界面简洁明了
- 避免不必要的装饰和复杂效果

### 可用性
- 确保界面易于理解和操作
- 提供清晰的视觉反馈

### 响应式
- 适应不同屏幕尺寸和设备类型
- 保持在各种设备上的良好用户体验

## 10. 应用场景

### 首页
- Banner轮播图
- 热门卡片（问答、专栏、路线）
- 文章列表（置顶文章和普通文章）
- 下拉刷新
- 上拉加载更多

### "我的页面"
- 用户信息卡片
- 功能菜单（收藏、积分、待办、设置）
- 登录状态显示

## 11. 实现指南

### 组件复用
- 尽可能复用现有的组件
- 创建通用的组件库

### 主题应用
- 使用 MaterialTheme 提供的主题属性
- 避免硬编码颜色、尺寸等数值

### 动画应用
- 统一使用 Compose 的动画 API
- 保持动画效果的一致性

### 性能优化
- 避免过度使用复杂动画
- 优化列表渲染性能
- 合理使用 Compose 的重组机制

## 12. 测试与验证

### 多设备测试
- 在不同尺寸的移动设备上测试
- 在平板和桌面设备上测试
- 确保在所有设备上的显示效果一致

### 用户体验测试
- 评估界面的可用性
- 收集用户反馈
- 根据反馈进行必要的调整

### 性能测试
- 测试应用的启动时间
- 测试列表滚动的流畅度
- 测试动画的性能表现

---

此设计语言规范文档旨在确保应用在视觉风格和交互体验上保持一致性，为开发团队提供明确的设计指导。