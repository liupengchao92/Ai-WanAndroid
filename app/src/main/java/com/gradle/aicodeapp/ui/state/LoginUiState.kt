package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.LoginResponse

/**
 * 登录UI状态
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val username: String = "",
    val password: String = "",
    val loginResponse: LoginResponse? = null
)
