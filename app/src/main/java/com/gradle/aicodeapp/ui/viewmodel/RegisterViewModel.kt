package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 注册ViewModel
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

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
     * 更新确认密码
     */
    fun updateRepassword(repassword: String) {
        _uiState.value = _uiState.value.copy(repassword = repassword)
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * 执行注册
     */
    fun register() {
        val currentState = _uiState.value

        if (currentState.username.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "用户名不能为空"
            )
            return
        }

        if (currentState.username.length < 3) {
            _uiState.value = currentState.copy(
                errorMessage = "用户名长度不能少于3位"
            )
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "密码不能为空"
            )
            return
        }

        if (currentState.password.length < 6) {
            _uiState.value = currentState.copy(
                errorMessage = "密码长度不能少于6位"
            )
            return
        }

        if (currentState.repassword.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "请确认密码"
            )
            return
        }

        if (currentState.password != currentState.repassword) {
            _uiState.value = currentState.copy(
                errorMessage = "两次输入的密码不一致"
            )
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null,
            isSuccess = false
        )

        viewModelScope.launch {
            val result = networkRepository.register(
                username = currentState.username,
                password = currentState.password,
                repassword = currentState.repassword
            )

            result.fold(
                onSuccess = { response ->
                    if (response.isSuccess()) {
                        val registerData = response.data
                        if (registerData != null) {
                            userManager.saveUserInfo(
                                username = registerData.username ?: currentState.username,
                                password = currentState.password,
                                userId = registerData.id,
                                nickname = registerData.nickname,
                                token = registerData.token,
                                icon = registerData.icon
                            )

                            _uiState.value = currentState.copy(
                                isLoading = false,
                                isSuccess = true,
                                registerResponse = registerData
                            )
                        } else {
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                errorMessage = "注册失败：服务器返回数据为空"
                            )
                        }
                    } else {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = response.errorMsg ?: "注册失败"
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
     * 重置注册状态
     */
    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}
