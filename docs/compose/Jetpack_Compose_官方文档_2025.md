# Jetpack Compose 官方文档（2025年最新版）

> 本文档通过 Context7 获取，包含 Jetpack Compose 的最新 API 参考、组件说明、使用示例及最佳实践。

---

## 目录

1. [核心功能概述](#一核心功能概述)
2. [布局系统](#二布局系统)
3. [状态管理](#三状态管理)
4. [动画效果](#四动画效果)
5. [主题定制](#五主题定制)
6. [最佳实践](#六最佳实践)

---

## 一、核心功能概述

### 1.1 什么是 Jetpack Compose？

Jetpack Compose 是 Google 官方推出的**声明式 UI 框架**，用于构建原生 Android 应用程序界面。它彻底改变了传统的 View 系统开发方式：

- **声明式编程**：通过描述 UI 应该是什么样子，而非如何构建它
- **Kotlin 优先**：完全使用 Kotlin 编写，无需 XML
- **实时预览**：支持 Android Studio 的实时预览功能
- **响应式 UI**：数据驱动 UI 自动更新

### 1.2 核心概念

```kotlin
// UI = f(State) - UI 是状态的函数
@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}
```

### 1.3 版本要求

- **最低 Android 版本**：API 21 (Android 5.0)
- **推荐版本**：API 26+ (Android 8.0)
- **Kotlin 版本**：1.9.0+
- **Compose BOM**：2024.02.00+

---

## 二、布局系统

### 2.1 基础布局组件

#### Column - 垂直排列
```kotlin
@Composable
fun ColumnExample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("第一行")
        Text("第二行")
        Text("第三行")
    }
}
```

**常用属性：**
- `verticalArrangement`: 垂直排列方式（Top、Center、Bottom、SpaceBetween、SpaceAround、SpaceEvenly）
- `horizontalAlignment`: 水平对齐方式（Start、CenterHorizontally、End）

#### Row - 水平排列
```kotlin
@Composable
fun RowExample() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("左侧")
        Button(onClick = { }) { Text("按钮") }
        Text("右侧")
    }
}
```

**常用属性：**
- `horizontalArrangement`: 水平排列方式
- `verticalAlignment`: 垂直对齐方式（Top、CenterVertically、Bottom）

#### Box - 堆叠布局
```kotlin
@Composable
fun BoxExample() {
    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // 背景层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
        )
        // 内容层
        Text("居中文字", color = Color.White)
        // 右上角图标
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "关闭",
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}
```

### 2.2 ConstraintLayout - 约束布局

```kotlin
@Composable
fun ConstraintLayoutExample() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (button, text) = createRefs()
        
        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }
        ) {
            Text("按钮")
        }
        
        Text(
            "文本",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(button.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }
        )
    }
}
```

**优势：**
- 扁平化布局结构，避免嵌套地狱
- 性能优于多层嵌套的 Column/Row
- 适合复杂界面布局

### 2.3 滚动布局

```kotlin
@Composable
fun ScrollableColumn() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(100) { index ->
            Text("Item $index", modifier = Modifier.fillMaxWidth())
        }
    }
}
```

**LazyColumn vs Column：**
- `Column` + `verticalScroll()`：适合少量内容
- `LazyColumn`：适合大量列表数据，支持懒加载

---

## 三、状态管理

### 3.1 基础状态 API

#### mutableStateOf - 可观察状态
```kotlin
@Composable
fun Counter() {
    // 创建可观察状态
    var count by remember { mutableStateOf(0) }
    
    Column {
        Text("计数: $count")
        Button(onClick = { count++ }) {
            Text("增加")
        }
    }
}
```

#### remember - 记忆状态
```kotlin
@Composable
fun RememberExample() {
    // 普通 remember - 重组时保持值
    var text by remember { mutableStateOf("") }
    
    // rememberSaveable - 配置更改时保持值（如旋转屏幕）
    var savedText by rememberSaveable { mutableStateOf("") }
}
```

**对比：**
| API | 重组时 | 配置更改时 | 使用场景 |
|-----|--------|-----------|---------|
| `remember` | ✅ 保持 | ❌ 丢失 | 临时 UI 状态 |
| `rememberSaveable` | ✅ 保持 | ✅ 保持 | 需要持久化的状态 |

### 3.2 ViewModel 状态管理

```kotlin
// ViewModel
class MyViewModel : ViewModel() {
    // 私有可变状态
    private val _uiState = MutableStateFlow(UiState())
    
    // 公开只读状态
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    // 更新状态
    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }
}

// 数据类
data class UiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val items: List<String> = emptyList()
)

// Composable 中使用
@Composable
fun MyScreen(viewModel: MyViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        TextField(
            value = uiState.name,
            onValueChange = viewModel::updateName
        )
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }
}
```

### 3.3 状态提升（State Hoisting）

```kotlin
// 有状态版本（内部管理状态）
@Composable
fun StatefulCounter() {
    var count by remember { mutableStateOf(0) }
    StatelessCounter(count = count, onIncrement = { count++ })
}

// 无状态版本（状态由外部传入）
@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit) {
    Button(onClick = onIncrement) {
        Text("点击了 $count 次")
    }
}
```

**最佳实践：**
- 将状态提升到最低公共祖先
- 优先使用无状态组件，便于测试和复用
- 业务逻辑放在 ViewModel 中

---

## 四、动画效果

### 4.1 可见性动画

#### AnimatedVisibility
```kotlin
@Composable
fun VisibilityAnimation() {
    var visible by remember { mutableStateOf(true) }
    
    Column {
        Button(onClick = { visible = !visible }) {
            Text("切换可见性")
        }
        
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
            )
        }
    }
}
```

**进入动画：**
- `fadeIn()` - 淡入
- `slideIn()` - 滑入
- `expandIn()` - 展开
- `scaleIn()` - 缩放进入

**退出动画：**
- `fadeOut()` - 淡出
- `slideOut()` - 滑出
- `shrinkOut()` - 收缩
- `scaleOut()` - 缩放退出

### 4.2 内容尺寸动画

```kotlin
@Composable
fun ContentSizeAnimation() {
    var expanded by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .animateContentSize()
            .background(Color.LightGray)
            .clickable { expanded = !expanded }
    ) {
        Text(
            text = if (expanded) "这是一段很长的文本内容..." else "短文本",
            modifier = Modifier.padding(16.dp)
        )
    }
}
```

### 4.3 内容切换动画

#### Crossfade - 淡入淡出切换
```kotlin
@Composable
fun CrossfadeExample() {
    var currentPage by remember { mutableStateOf("A") }
    
    Crossfade(targetState = currentPage) { page ->
        when (page) {
            "A" -> PageA()
            "B" -> PageB()
            "C" -> PageC()
        }
    }
}
```

#### AnimatedContent - 高级内容切换
```kotlin
@Composable
fun AnimatedContentExample() {
    var count by remember { mutableStateOf(0) }
    
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { height -> height } + fadeIn() togetherWith
                slideOutVertically { height -> -height } + fadeOut()
            } else {
                slideInVertically { height -> -height } + fadeIn() togetherWith
                slideOutVertically { height -> height } + fadeOut()
            }
        }
    ) { targetCount ->
        Text("计数: $targetCount", fontSize = 24.sp)
    }
}
```

### 4.4 属性动画

```kotlin
@Composable
fun PropertyAnimation() {
    var enabled by remember { mutableStateOf(true) }
    
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300)
    )
    
    val color by animateColorAsState(
        targetValue = if (enabled) Color.Green else Color.Gray
    )
    
    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer { this.alpha = alpha }
            .background(color)
            .clickable { enabled = !enabled }
    )
}
```

---

## 五、主题定制

### 5.1 MaterialTheme 3

```kotlin
@Composable
fun MyApp() {
    MyApplicationTheme {
        // 应用内容
        MainScreen()
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
```

### 5.2 颜色方案

```kotlin
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F)
)
```

### 5.3 字体排版

```kotlin
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

### 5.4 形状

```kotlin
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)
```

### 5.5 使用主题

```kotlin
@Composable
fun ThemedComponent() {
    // 使用主题颜色
    Text(
        text = "主色调文本",
        color = MaterialTheme.colorScheme.primary
    )
    
    // 使用主题字体
    Text(
        text = "标题文本",
        style = MaterialTheme.typography.headlineLarge
    )
    
    // 使用主题形状
    Card(
        shape = MaterialTheme.shapes.medium
    ) {
        // 卡片内容
    }
}
```

### 5.6 Material Theme Builder

Google 提供了在线工具 [Material Theme Builder](https://m3.material.io/theme-builder) 来快速生成主题：

1. 选择基础颜色
2. 自动生成完整的 ColorScheme
3. 导出为 Compose 代码
4. 复制到项目中使用

---

## 六、最佳实践

### 6.1 性能优化

1. **避免不必要的重组**
```kotlin
// ❌ 不好：整个列表都会重组
@Composable
fun BadList(items: List<Item>) {
    Column {
        items.forEach { item ->
            ListItem(item)
        }
    }
}

// ✅ 好：只有变化的项目会重组
@Composable
fun GoodList(items: List<Item>) {
    LazyColumn {
        items(items, key = { it.id }) { item ->
            ListItem(item)
        }
    }
}
```

2. **使用 remember 缓存计算结果**
```kotlin
@Composable
fun ExpensiveCalculation(data: List<Int>) {
    // 缓存计算结果，避免每次重组都重新计算
    val sortedData = remember(data) {
        data.sortedDescending()
    }
}
```

3. **使用 derivedStateOf 优化频繁变化的状态**
```kotlin
@Composable
fun ScrollHeader(scrollState: ScrollState) {
    // 只有当条件从 true 变为 false 或相反时才触发重组
    val isVisible by remember {
        derivedStateOf { scrollState.value > 100 }
    }
}
```

### 6.2 架构建议

1. **UI 层结构**
```
Screen (页面)
├── ViewModel (状态管理)
├── State (UI 状态)
└── Components (可复用组件)
    ├── Atoms (原子组件)
    ├── Molecules (分子组件)
    └── Organisms (有机体组件)
```

2. **状态管理层次**
- **Screen Level**: 使用 ViewModel 管理业务状态
- **Component Level**: 使用 remember 管理 UI 状态
- **Theme Level**: 使用 CompositionLocal 管理主题状态

### 6.3 常见错误避免

1. **不要在 Composable 中执行副作用**
```kotlin
// ❌ 错误
@Composable
fun BadExample() {
    val result = fetchData() // 网络请求！
}

// ✅ 正确
@Composable
fun GoodExample(viewModel: MyViewModel) {
    val result by viewModel.data.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
}
```

2. **正确使用 Modifier 顺序**
```kotlin
// ❌ 不好
Box(modifier = Modifier.background(Color.Red).padding(16.dp))

// ✅ 好：先 padding 再 background
Box(modifier = Modifier.padding(16.dp).background(Color.Red))
```

---

## 参考资源

- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [Compose API 参考](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary)
- [Material Design 3](https://m3.material.io/)
- [Compose 示例代码](https://github.com/android/compose-samples)

---

*本文档最后更新：2025年1月*
