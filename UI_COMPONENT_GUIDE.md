# UI组件库使用指南

## 1. 概述

本指南基于Material 3设计系统，定义了Ai-WanAndroid应用的完整UI组件库和设计规范。旨在确保应用的视觉一致性、交互流畅性和性能优异，提升用户体验和品牌形象。

## 2. 设计系统规范

### 2.1 色彩系统

#### 2.1.1 色彩定义

| 色彩类别 | 颜色值 | 应用场景 |
|---------|-------|--------|
| Primary | #1E88E5 | 主要操作按钮、关键链接、重要信息强调 |
| PrimaryLight | #64B5F6 | 主色浅色变体 |
| PrimaryDark | #0D47A1 | 主色深色变体 |
| Secondary | #03A9F4 | 次要操作、标签、分类信息 |
| Tertiary | #9C27B0 | 特殊功能、创新元素、品牌亮点 |
| Error | #E53935 | 错误状态、警告信息 |
| Success | #43A047 | 成功状态、确认信息 |
| Warning | #FF9800 | 警告状态、提示信息 |
| Info | #1E88E5 | 信息状态、说明文本 |
| Background | #FAFAFA | 页面背景、内容区域 |
| Surface | #FFFFFF | 卡片背景、组件表面 |
使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档使用 context7 获取 Android Jetpack Compose 的最新文档
#### 2.1.2 色彩应用规则

1. **主色（Primary）**：用于主要操作按钮、关键链接和重要信息强调
2. **辅助色（Secondary）**：用于次要操作、标签和分类信息
3. **第三色（Tertiary）**：用于特殊功能、创新元素和品牌亮点
4. **状态色**：严格按照语义使用（错误-红色、成功-绿色、警告-橙色、信息-蓝色）
5. **文本色**：根据背景色自动调整对比度，确保可读性
6. **背景色**：使用不同层级的背景色创建视觉深度
7. **轮廓色**：用于边框、分隔线等元素，区分不同区域

#### 2.1.3 色彩评估指标

1. **色彩对比度**：确保文本与背景对比度符合WCAG 2.1 AA标准
2. **色彩一致性**：同一类型元素在不同页面使用相同色彩
3. **色彩和谐度**：通过色彩理论确保整体视觉和谐
4. **色彩可访问性**：考虑色盲用户的体验，使用多种视觉提示

### 2.2 排版系统

#### 2.2.1 字体定义

| 字体类型 | 字号 | 字重 | 行高 | 应用场景 |
|---------|-----|------|------|--------|
| displayLarge | 57sp | W400 | 64sp | 大标题、营销内容 |
| displayMedium | 45sp | W400 | 52sp | 大标题、品牌展示 |
| displaySmall | 36sp | W400 | 44sp | 大标题、页面头部 |
| headlineLarge | 32sp | W400 | 40sp | 页面标题、主要区块标题 |
| headlineMedium | 28sp | W400 | 36sp | 页面标题、区块标题 |
| headlineSmall | 24sp | W400 | 32sp | 区块标题、卡片标题 |
| titleLarge | 22sp | W500 | 28sp | 卡片标题、列表项标题 |
| titleMedium | 16sp | W500 | 24sp | 列表项标题、按钮文本 |
| titleSmall | 14sp | W500 | 20sp | 小卡片标题、标签 |
| bodyLarge | 16sp | W400 | 24sp | 正文文本、详细说明 |
| bodyMedium | 14sp | W400 | 20sp | 辅助文本、说明信息 |
| bodySmall | 12sp | W400 | 16sp | 小文本、提示信息 |
| labelLarge | 14sp | W500 | 20sp | 按钮文本、标签 |
| labelMedium | 12sp | W500 | 16sp | 小按钮文本、徽章 |
| labelSmall | 11sp | W500 | 16sp | 微文本、状态标签 |

#### 2.2.2 排版应用规则

1. **大标题**：使用displayLarge/Medium/Small，用于营销页面和品牌展示
2. **页面标题**：使用headlineLarge/Medium/Small，用于页面顶部标题
3. **区块标题**：使用titleLarge/Medium/Small，用于卡片、列表等区块标题
4. **正文文本**：使用bodyLarge/Medium/Small，用于主要内容和说明文字
5. **辅助文本**：使用labelLarge/Medium/Small，用于按钮、标签和提示信息
6. **强调文本**：对于需要强调的文本，可使用对应字号的Medium或Bold字重
7. **文本对齐**：主要内容使用左对齐，标题可根据设计需要使用居中对齐

#### 2.2.3 排版评估指标

1. **字体层级**：确保页面有清晰的视觉层次结构，字号大小有明显区分
2. **文本可读性**：行高设置为字号的1.5倍左右，确保舒适的阅读体验
3. **排版一致性**：相同类型内容使用相同文本样式，保持视觉统一
4. **响应式适配**：在不同屏幕尺寸下调整字号大小，确保在小屏幕上的可读性
5. **文本密度**：根据内容类型调整文本密度，重要内容使用更大的行高和字间距

### 2.3 形状系统

#### 2.3.1 形状定义

| 形状类型 | 圆角半径 | 应用场景 |
|---------|---------|--------|
| extraSmall | 4dp | 小按钮、小标签、徽章 |
| small | 8dp | 按钮、卡片、输入框 |
| medium | 12dp | 卡片、对话框、面板 |
| large | 16dp | 大卡片、模态框、底部表单 |
| extraLarge | 24dp | 超大卡片、特殊容器 |
| full | 50% | 圆形按钮、头像、图标 |

#### 2.3.2 形状应用规则

1. **小形状（Small）**：用于小按钮、标签、徽章等小型元素
2. **中形状（Medium）**：用于卡片、输入框、对话框等中型元素
3. **大形状（Large）**：用于大卡片、模态框等大型元素
4. **自定义形状**：用于特殊场景，如只需要顶部圆角的元素
5. **圆形（Full）**：用于圆形按钮、头像等圆形元素

#### 2.3.3 形状评估指标

1. **形状一致性**：同一类型元素使用相同的圆角半径
2. **形状层次**：根据元素大小和重要性使用不同大小的圆角
3. **形状和谐**：确保圆角大小与元素尺寸成比例
4. **响应式适配**：在不同屏幕尺寸下保持形状的一致性

### 2.4 间距系统

#### 2.4.1 间距定义

| 间距类型 | 大小 | 应用场景 |
|---------|-----|--------|
| ExtraSmall | 4dp | 小元素间距、文本间距 |
| Small | 8dp | 元素间距、内边距 |
| Medium | 16dp | 区块间距、组件间距 |
| Large | 24dp | 页面区域间距、大组件间距 |
| ExtraLarge | 32dp | 页面边距、大区域间距 |
| Huge | 48dp | 超大间距、特殊布局 |

#### 2.4.2 间距应用规则

1. **统一间距**：使用预定义的间距值，确保整体布局的一致性
2. **层次分明**：通过不同大小的间距创建视觉层次
3. **响应式调整**：在不同屏幕尺寸下调整间距大小
4. **留白合理**：适当的留白可以提升内容的可读性和视觉美感

## 3. 组件使用指南

### 3.1 按钮组件

#### 3.1.1 AppPrimaryButton

**功能**：主要操作按钮，用于重要的用户操作

**参数**：
- `text`：按钮文本
- `onClick`：点击回调
- `modifier`：修饰符
- `enabled`：是否启用
- `icon`：可选图标
- `iconPosition`：图标位置（LEFT/RIGHT）
- `size`：按钮尺寸（SMALL/MEDIUM/LARGE）

**使用示例**：
```kotlin
AppPrimaryButton(
    text = "登录",
    onClick = { /* 登录逻辑 */ },
    modifier = Modifier.fillMaxWidth(),
    size = ButtonSize.MEDIUM
)
```

#### 3.1.2 AppSecondaryButton

**功能**：次要操作按钮，用于辅助性的用户操作

**参数**：与AppPrimaryButton相同

**使用示例**：
```kotlin
AppSecondaryButton(
    text = "取消",
    onClick = { /* 取消逻辑 */ },
    modifier = Modifier.fillMaxWidth(),
    size = ButtonSize.MEDIUM
)
```

#### 3.1.3 AppTextButton

**功能**：文本按钮，用于链接性的用户操作

**参数**：与AppPrimaryButton相似，但没有size参数

**使用示例**：
```kotlin
AppTextButton(
    text = "忘记密码？",
    onClick = { /* 忘记密码逻辑 */ }
)
```

### 3.2 卡片组件

#### 3.2.1 ArticleItem

**功能**：文章列表项，显示文章标题、作者、日期等信息

**参数**：
- `article`：文章数据
- `isTop`：是否置顶
- `isSquare`：是否来自广场
- `onClick`：点击回调
- `onCollectClick`：收藏回调

**使用示例**：
```kotlin
ArticleItem(
    article = article,
    isTop = true,
    onClick = { onArticleClick(article.link, article.title) },
    onCollectClick = { shouldCollect ->
        if (shouldCollect) {
            collectViewModel.collectArticle(article.id)
        } else {
            collectViewModel.uncollectArticle(article.id)
        }
        viewModel.updateArticleCollectStatus(article.id, shouldCollect)
    }
)
```

### 3.3 导航组件

#### 3.3.1 AppTopAppBar

**功能**：顶部应用栏，显示页面标题和导航按钮

**参数**：
- `title`：标题文本
- `modifier`：修饰符
- `navigationIcon`：导航图标
- `actions`：右侧操作图标
- `colors`：颜色配置
- `onNavigationClick`：导航点击回调

**使用示例**：
```kotlin
AppTopAppBar(
    title = "文章详情",
    onNavigationClick = { navController.popBackStack() }
)
```

### 3.4 响应式布局

#### 3.4.1 ResponsiveLayout

**功能**：响应式布局辅助函数，根据屏幕尺寸自动调整布局

**主要方法**：
- `getScreenSize()`：获取当前屏幕尺寸
- `calculateHorizontalPadding()`：计算水平外边距
- `responsiveContentPadding()`：获取响应式内容内边距
- `responsiveHorizontalPadding()`：获取响应式水平外边距修饰符
- `responsiveTextStyle()`：获取响应式文本样式
- `calculateGridColumns()`：计算网格列数

**使用示例**：
```kotlin
LazyColumn(
    modifier = Modifier.fillMaxSize(),
    state = listState,
    contentPadding = ResponsiveLayout.responsiveContentPadding()
) {
    // 列表内容
}
```

## 4. 最佳实践

### 4.1 组件开发

1. **单一职责**：每个组件只负责一个功能，保持组件的简洁性和可维护性
2. **可组合性**：设计可组合的组件，便于在不同场景下重用
3. **状态管理**：使用ViewModel和StateFlow管理复杂状态，避免在Composable中使用可变变量
4. **性能优化**：使用remember、derivedStateOf等优化组件重组
5. **错误处理**：提供清晰的错误状态和用户反馈

### 4.2 布局设计

1. **层次分明**：通过色彩、排版、间距等元素创建清晰的视觉层次
2. **响应式适配**：确保应用在不同屏幕尺寸下都能正常显示
3. **留白合理**：适当的留白可以提升内容的可读性和视觉美感
4. **对齐一致**：保持元素的对齐方式一致，提升整体布局的协调性
5. **导航清晰**：提供明确的导航路径，帮助用户快速找到所需内容

### 4.3 交互设计

1. **反馈及时**：用户操作后提供即时、明确的视觉反馈
2. **动画流畅**：使用适当的动画效果，提升用户体验
3. **操作简单**：减少用户操作步骤，简化复杂任务
4. **容错设计**：提供合理的默认值和错误处理机制
5. **可访问性**：确保应用对所有用户都可访问，包括残障用户

### 4.4 性能优化

1. **组件重组**：使用@Stable注解和remember优化组件重组
2. **状态管理**：使用StateFlow和ViewModel管理应用状态
3. **资源加载**：优化图片大小和质量，使用缓存机制
4. **动画性能**：避免在动画中进行复杂计算
5. **列表性能**：使用LazyColumn和LazyRow实现虚拟滚动

## 5. 评估指标

### 5.1 视觉一致性

1. **色彩一致性**：同一类型元素在不同页面使用相同色彩
2. **排版一致性**：相同类型内容使用相同文本样式
3. **形状一致性**：同一类型元素使用相同的圆角半径
4. **间距一致性**：使用统一的间距标准

### 5.2 交互体验

1. **响应时间**：用户操作响应时间不超过100ms
2. **动画流畅度**：动画和滚动操作保持60fps
3. **操作直观性**：用户能够直观理解如何操作应用
4. **反馈清晰度**：用户操作后有明确的视觉反馈

### 5.3 性能表现

1. **启动时间**：应用启动时间不超过3秒
2. **内存使用**：内存占用在合理范围内，无内存泄漏
3. **电池消耗**：正常使用下电池消耗不超过行业平均水平
4. **网络请求**：网络请求优化，减少不必要的请求

### 5.4 可访问性

1. **色彩对比度**：文本与背景对比度符合WCAG 2.1 AA标准
2. **屏幕阅读器**：支持屏幕阅读器，提供适当的内容描述
3. **触摸目标**：触摸目标大小至少为48dp
4. **键盘导航**：支持键盘导航，确保所有功能都可以通过键盘访问

## 6. 版本更新

### 6.1 版本历史

| 版本 | 日期 | 主要变更 |
|------|------|--------|
| 1.0 | 2026-01-27 | 初始版本，定义完整的设计系统和组件库 |

### 6.2 更新指南

1. **向后兼容**：确保新版本与旧版本兼容，避免破坏现有功能
2. **迁移指南**：提供详细的迁移指南，帮助开发者快速适应新版本
3. **废弃通知**：提前通知即将废弃的组件和API
4. **测试覆盖**：确保新版本经过充分测试，减少回归问题

## 7. 结语

本指南旨在为Ai-WanAndroid应用的UI开发提供统一的标准和最佳实践。通过遵循这些规范，我们可以创建一个视觉美观、交互流畅、性能优异的应用界面，提升用户体验和品牌形象。

随着应用的不断发展，我们将持续更新和完善本指南，以适应新的设计趋势和技术要求。

---

**文档维护者**：UI/UX团队
**最后更新**：2026-01-27
