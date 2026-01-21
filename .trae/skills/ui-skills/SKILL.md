---
name: UI-SKILLS
description: 专注于 Jetpack Compose 项目 的现代化 UI 优化以及在创建页面时遵从此规则。
---

## 🔍 Compose健康度检查清单
请检查以下关键项：

1. **Material Design版本**
   - [ ] 是否使用 `material3` 依赖而非 `material`？
   - [ ] `implementation("androidx.compose.material3:material3")`

2. **主题配置评估**
   - [ ] 是否使用 `dynamicColor = true`（Android 12+）？
   - [ ] 暗色主题是否完整实现？
   - [ ] 形状系统是否一致（`Shapes`）？

3. **性能红线标记**
   - [ ] 是否在 `@Composable` 中执行耗时操作？
   - [ ] `remember` 使用是否恰当？
   - [ ] 是否存在不必要的重组？