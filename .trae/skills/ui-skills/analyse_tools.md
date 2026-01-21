## 🛠️ Compose专属工具链
1. **布局检查器**：
   - Android Studio Layout Inspector（支持Compose）
   - 启用重组计数：`adb shell setprop debug.compose.metrics true`

2. **性能分析**：
   ```kotlin
   // 使用compositionLocalOf跟踪重组
   val LocalRecomposeLogger = compositionLocalOf { RecomposeLogger() }
   
   // 在开发中使用快照状态观察
   Snapshot.registerGlobalWriteObserver { changedState ->
       Log.d("Recompose", "State changed: $changedState")
   }