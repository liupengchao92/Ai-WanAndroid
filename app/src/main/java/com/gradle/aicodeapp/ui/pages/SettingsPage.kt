package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.data.preferences.SettingsDataStore
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.SettingsViewModel
import java.util.Locale

@Composable
fun SettingsPage(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.language) {
        updateLocale(context, uiState.language)
    }

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(Spacing.Medium))

            DarkModeSection(
                currentMode = uiState.darkMode,
                onModeChange = { mode ->
                    viewModel.setDarkMode(mode)
                }
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            LanguageSection(
                currentLanguage = uiState.language,
                onLanguageChange = { language ->
                    viewModel.setLanguage(language)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            LogoutButton(
                onLogout = onLogout
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun DarkModeSection(
    currentMode: String,
    onModeChange: (String) -> Unit
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
            .padding(horizontal = Spacing.ScreenPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp) // 使用标准的medium圆角
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Small)
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
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.dark_mode),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(Spacing.Medium))

                Text(
                    text = stringResource(R.string.dark_mode),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = when (currentMode) {
                        SettingsDataStore.DARK_MODE_LIGHT -> stringResource(R.string.light_mode)
                        SettingsDataStore.DARK_MODE_DARK -> stringResource(R.string.dark_mode)
                        SettingsDataStore.DARK_MODE_SYSTEM -> stringResource(R.string.follow_system)
                        else -> stringResource(R.string.follow_system)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(Spacing.Small))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.expand),
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                    Divider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    DarkModeOption(
                        icon = Icons.Default.Star,
                        title = stringResource(R.string.light_mode),
                        selected = currentMode == SettingsDataStore.DARK_MODE_LIGHT,
                        onClick = {
                            onModeChange(SettingsDataStore.DARK_MODE_LIGHT)
                        }
                    )

                    Divider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    DarkModeOption(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.dark_mode),
                        selected = currentMode == SettingsDataStore.DARK_MODE_DARK,
                        onClick = {
                            onModeChange(SettingsDataStore.DARK_MODE_DARK)
                        }
                    )

                    Divider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant
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
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(Spacing.IconLarge),
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Spacer(modifier = Modifier.width(Spacing.Medium))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f)
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "已选择",
                modifier = Modifier.size(Spacing.IconMedium),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun LanguageSection(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
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
            .padding(horizontal = Spacing.ScreenPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp) // 使用标准的medium圆角
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Small)
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
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.language),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(Spacing.Medium))

                Text(
                    text = stringResource(R.string.language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = when (currentLanguage) {
                        SettingsDataStore.LANGUAGE_ZH -> stringResource(R.string.simplified_chinese)
                        SettingsDataStore.LANGUAGE_EN -> stringResource(R.string.english)
                        SettingsDataStore.LANGUAGE_JA -> stringResource(R.string.japanese)
                        else -> stringResource(R.string.simplified_chinese)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(Spacing.Small))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.expand),
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                    Divider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    LanguageOption(
                        title = stringResource(R.string.simplified_chinese),
                        selected = currentLanguage == SettingsDataStore.LANGUAGE_ZH,
                        onClick = {
                            onLanguageChange(SettingsDataStore.LANGUAGE_ZH)
                        }
                    )

                    Divider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    LanguageOption(
                        title = stringResource(R.string.english),
                        selected = currentLanguage == SettingsDataStore.LANGUAGE_EN,
                        onClick = {
                            onLanguageChange(SettingsDataStore.LANGUAGE_EN)
                        }
                    )

                    Divider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    LanguageOption(
                        title = stringResource(R.string.japanese),
                        selected = currentLanguage == SettingsDataStore.LANGUAGE_JA,
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
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f)
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "已选择",
                modifier = Modifier.size(Spacing.IconMedium),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun LogoutButton(
    onLogout: () -> Unit
) {
    Button(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding)
            .height(48.dp), // 使用标准的按钮高度
        shape = RoundedCornerShape(8.dp), // 使用标准的small圆角
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = stringResource(R.string.logout),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(Spacing.Small))
        Text(
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

private fun updateLocale(context: android.content.Context, languageCode: String) {
    val locale = when (languageCode) {
        SettingsDataStore.LANGUAGE_ZH -> Locale.SIMPLIFIED_CHINESE
        SettingsDataStore.LANGUAGE_EN -> Locale.ENGLISH
        SettingsDataStore.LANGUAGE_JA -> Locale.JAPANESE
        else -> Locale.SIMPLIFIED_CHINESE
    }
    Locale.setDefault(locale)
    val config = android.content.res.Configuration()
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
