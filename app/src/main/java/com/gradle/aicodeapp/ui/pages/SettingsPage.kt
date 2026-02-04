package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.gradle.aicodeapp.ui.components.AppTopAppBar
import com.gradle.aicodeapp.ui.components.CenteredLoadingView
import com.gradle.aicodeapp.ui.components.FullScreenLoadingView
import com.gradle.aicodeapp.ui.components.LoadingSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.data.preferences.SettingsDataStore
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import com.gradle.aicodeapp.ui.theme.Primary
import com.gradle.aicodeapp.ui.theme.PrimaryContainer
import com.gradle.aicodeapp.ui.theme.Secondary
import com.gradle.aicodeapp.ui.theme.SecondaryContainer
import com.gradle.aicodeapp.ui.theme.Tertiary
import com.gradle.aicodeapp.ui.theme.TertiaryContainer
import com.gradle.aicodeapp.ui.theme.LanguageManager
import com.gradle.aicodeapp.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsPage(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 语言切换状态
    var isLanguageChanging by remember { mutableStateOf(false) }
    var showLanguageSuccess by remember { mutableStateOf(false) }

    // 监听语言变化，显示成功提示
    LaunchedEffect(uiState.language) {
        if (showLanguageSuccess) {
            val message = when (uiState.language) {
                SettingsDataStore.LANGUAGE_ZH -> "语言已切换为简体中文"
                SettingsDataStore.LANGUAGE_EN -> "Language switched to English"
                SettingsDataStore.LANGUAGE_JA -> "言語が日本語に変更されました"
                else -> "语言已切换"
            }
            snackbarHostState.showSnackbar(message)
            showLanguageSuccess = false
            isLanguageChanging = false
        }
    }

    Scaffold(
        topBar = {
            SettingsTopAppBar(onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(paddingValues)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(Spacing.Medium))

                // 设置分组标题
                SectionTitle(title = stringResource(R.string.appearance_settings))

                Spacer(modifier = Modifier.height(Spacing.Small))

                DarkModeSection(
                    currentMode = uiState.darkMode,
                    onModeChange = { mode ->
                        viewModel.setDarkMode(mode)
                    }
                )

                Spacer(modifier = Modifier.height(Spacing.Large))

                SectionTitle(title = stringResource(R.string.language_settings))

                Spacer(modifier = Modifier.height(Spacing.Small))

                LanguageSection(
                    currentLanguage = uiState.language,
                    isChanging = isLanguageChanging,
                    onLanguageChange = { language ->
                        if (language != uiState.language) {
                            isLanguageChanging = true
                            showLanguageSuccess = true
                            viewModel.setLanguage(language)
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.height(Spacing.Large))

                LogoutButton(onLogout = onLogout)

                Spacer(modifier = Modifier.height(Spacing.ExtraLarge))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(
    onBackClick: () -> Unit,
) {
    AppTopAppBar(
        title = stringResource(R.string.settings),
        onNavigationClick = onBackClick
    )
}

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(horizontal = ResponsiveLayout.calculateHorizontalPadding())
    )
}

@Composable
private fun DarkModeSection(
    currentMode: String,
    onModeChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "arrow_rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ResponsiveLayout.calculateHorizontalPadding())
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
                spotColor = Primary.copy(alpha = 0.1f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 头部行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        expanded = !expanded
                    }
                    .padding(Spacing.Medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryContainer
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.dark_mode),
                            modifier = Modifier.size(24.dp),
                            tint = Primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Spacing.Medium))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.dark_mode),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = when (currentMode) {
                            SettingsDataStore.DARK_MODE_LIGHT -> stringResource(R.string.light_mode)
                            SettingsDataStore.DARK_MODE_DARK -> stringResource(R.string.dark_mode)
                            SettingsDataStore.DARK_MODE_SYSTEM -> stringResource(R.string.follow_system)
                            else -> stringResource(R.string.follow_system)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(Spacing.Small))

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.expand),
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                            .rotate(rotation),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )

                    DarkModeOption(
                        icon = Icons.Default.Star,
                        title = stringResource(R.string.light_mode),
                        selected = currentMode == SettingsDataStore.DARK_MODE_LIGHT,
                        onClick = {
                            onModeChange(SettingsDataStore.DARK_MODE_LIGHT)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )

                    DarkModeOption(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.dark_mode),
                        selected = currentMode == SettingsDataStore.DARK_MODE_DARK,
                        onClick = {
                            onModeChange(SettingsDataStore.DARK_MODE_DARK)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )

                    DarkModeOption(
                        icon = Icons.Default.Settings,
                        title = stringResource(R.string.follow_system),
                        selected = currentMode == SettingsDataStore.DARK_MODE_SYSTEM,
                        onClick = {
                            onModeChange(SettingsDataStore.DARK_MODE_SYSTEM)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DarkModeOption(
    icon: ImageVector,
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.05f else if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            PrimaryContainer.copy(alpha = 0.3f)
        } else if (isPressed) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        } else {
            Color.Transparent
        },
        animationSpec = tween(durationMillis = 150),
        label = "background"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .background(backgroundColor)
            .padding(Spacing.Medium)
            .scale(scale),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(10.dp),
            color = if (selected) PrimaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
                    tint = if (selected) Primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(Spacing.Medium))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            ),
            color = if (selected) Primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        if (selected) {
            Surface(
                shape = CircleShape,
                color = PrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "已选择",
                    modifier = Modifier
                        .size(28.dp)
                        .padding(4.dp),
                    tint = Primary
                )
            }
        }
    }
}

@Composable
private fun LanguageSection(
    currentLanguage: String,
    isChanging: Boolean,
    onLanguageChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "arrow_rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ResponsiveLayout.calculateHorizontalPadding())
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
                spotColor = Secondary.copy(alpha = 0.1f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        expanded = !expanded
                    }
                    .padding(Spacing.Medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = SecondaryContainer
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.language),
                            modifier = Modifier.size(24.dp),
                            tint = Secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Spacing.Medium))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.language),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = when (currentLanguage) {
                            SettingsDataStore.LANGUAGE_ZH -> stringResource(R.string.simplified_chinese)
                            SettingsDataStore.LANGUAGE_EN -> stringResource(R.string.english)
                            SettingsDataStore.LANGUAGE_JA -> stringResource(R.string.japanese)
                            else -> stringResource(R.string.simplified_chinese)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 显示加载状态
                if (isChanging) {
                    CenteredLoadingView(
                        size = LoadingSize.SMALL
                    )
                    Spacer(modifier = Modifier.width(Spacing.Small))
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.expand),
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                            .rotate(rotation),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )

                    LanguageOption(
                        title = stringResource(R.string.simplified_chinese),
                        subtitle = "简体中文",
                        selected = currentLanguage == SettingsDataStore.LANGUAGE_ZH,
                        isChanging = isChanging && currentLanguage == SettingsDataStore.LANGUAGE_ZH,
                        onClick = {
                            onLanguageChange(SettingsDataStore.LANGUAGE_ZH)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )

                    LanguageOption(
                        title = stringResource(R.string.english),
                        subtitle = "English",
                        selected = currentLanguage == SettingsDataStore.LANGUAGE_EN,
                        isChanging = isChanging && currentLanguage == SettingsDataStore.LANGUAGE_EN,
                        onClick = {
                            onLanguageChange(SettingsDataStore.LANGUAGE_EN)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )

                    LanguageOption(
                        title = stringResource(R.string.japanese),
                        subtitle = "日本語",
                        selected = currentLanguage == SettingsDataStore.LANGUAGE_JA,
                        isChanging = isChanging && currentLanguage == SettingsDataStore.LANGUAGE_JA,
                        onClick = {
                            onLanguageChange(SettingsDataStore.LANGUAGE_JA)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOption(
    title: String,
    subtitle: String,
    selected: Boolean,
    isChanging: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            SecondaryContainer.copy(alpha = 0.3f)
        } else if (isPressed) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        } else {
            Color.Transparent
        },
        animationSpec = tween(durationMillis = 150),
        label = "background"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .background(backgroundColor)
            .padding(Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                ),
                color = if (selected) Secondary else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 显示加载指示器
        if (isChanging) {
            CenteredLoadingView(
                size = LoadingSize.SMALL
            )
            Spacer(modifier = Modifier.width(Spacing.Small))
        }

        if (selected && !isChanging) {
            Surface(
                shape = CircleShape,
                color = SecondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "已选择",
                    modifier = Modifier
                        .size(28.dp)
                        .padding(4.dp),
                    tint = Secondary
                )
            }
        }
    }
}

@Composable
private fun LogoutButton(
    onLogout: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Button(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ResponsiveLayout.calculateHorizontalPadding())
            .height(52.dp)
            .scale(scale),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = stringResource(R.string.logout),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(Spacing.Small))
        Text(
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}
