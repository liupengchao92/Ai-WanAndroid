package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 登录ViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * 更新用户名
     */
    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    /**
     * 更新密码
     */
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * 执行登录
     */
    fun login() {
        val currentState = _uiState.value

        if (currentState.username.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "用户名不能为空"
            )
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "密码不能为空"
            )
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null,
            isSuccess = false
        )

        viewModelScope.launch {
            val result = networkRepository.login(
                username = currentState.username,
                password = currentState.password
            )

            result.fold(
                onSuccess = { response ->
                    if (response.isSuccess()) {
                        val loginData = response.data
                        if (loginData != null) {
                            userManager.saveUserInfo(
                                username = loginData.username ?: currentState.username,
                                password = currentState.password,
                                userId = loginData.id,
                                nickname = loginData.nickname,
                                token = loginData.token,
                                icon = loginData.icon
                            )

                            _uiState.value = currentState.copy(
                                isLoading = false,
                                isSuccess = true,
                                loginResponse = loginData
                            )
                        } else {
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                errorMessage = "登录失败：服务器返回数据为空"
                            )
                        }
                    } else {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = response.errorMsg ?: "登录失败"
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = "网络错误：${exception.message}"
                    )
                }
            )
        }
    }

    /**
     * 重置登录状态
     */
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}
