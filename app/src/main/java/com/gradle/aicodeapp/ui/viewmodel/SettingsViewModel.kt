package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.data.preferences.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsDataStore.darkMode.collect { mode ->
                _uiState.value = _uiState.value.copy(darkMode = mode)
            }
        }
        viewModelScope.launch {
            settingsDataStore.language.collect { language ->
                _uiState.value = _uiState.value.copy(language = language)
            }
        }
    }

    fun setDarkMode(mode: String) {
        viewModelScope.launch {
            settingsDataStore.setDarkMode(mode)
            _uiState.value = _uiState.value.copy(darkMode = mode)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsDataStore.setLanguage(languageCode)
            _uiState.value = _uiState.value.copy(language = languageCode)
        }
    }
}

data class SettingsUiState(
    val darkMode: String = SettingsDataStore.DARK_MODE_SYSTEM,
    val language: String = SettingsDataStore.LANGUAGE_ZH
)
