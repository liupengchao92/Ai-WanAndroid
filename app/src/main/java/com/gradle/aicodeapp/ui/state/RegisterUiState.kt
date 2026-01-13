package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.RegisterResponse

/**
 * 注册UI状态
 */
data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val username: String = "",
    val password: String = "",
    val repassword: String = "",
    val registerResponse: RegisterResponse? = null
)
