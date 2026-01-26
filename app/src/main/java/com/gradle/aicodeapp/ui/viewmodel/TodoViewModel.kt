package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.network.exception.ErrorHandler
import com.gradle.aicodeapp.network.model.Todo
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.TodoUiState
import com.gradle.aicodeapp.utils.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState

    private val TAG = "TodoViewModel"

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun loadTodoList() {
        if (!userManager.isLoggedIn()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "请先登录"
            )
            return
        }

        val currentState = _uiState.value

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            loadTodoListFromNetwork()
        }
    }

    private suspend fun loadTodoListFromNetwork() {
        val currentState = _uiState.value

        val result = networkRepository.getTodoList(
            page = currentState.currentPage,
            status = currentState.filterStatus,
            type = currentState.filterType,
            priority = currentState.filterPriority,
            orderby = currentState.orderby
        )

        if (result.isSuccess) {
            val response = result.getOrNull()
            if (response?.isSuccess() == true) {
                val newTodos = response.data?.datas ?: emptyList()
                val updatedTodos = if (currentState.currentPage == 1) {
                    newTodos
                } else {
                    currentState.todos + newTodos
                }

                _uiState.value = _uiState.value.copy(
                    todos = updatedTodos,
                    isLoading = false,
                    isRefreshing = false,
                    hasMore = response.data?.over != true
                )
                LogUtils.d(TAG, "Todo list loaded from network: ${newTodos.size}")
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "获取TODO列表失败: ${response?.errorMsg}",
                    isLoading = false,
                    isRefreshing = false
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    fun addTodo(
        title: String,
        content: String,
        date: Long,
        type: Int,
        priority: Int
    ) {
        if (!userManager.isLoggedIn()) {
            ErrorHandler.emitErrorEvent(
                ErrorHandler.ErrorEvent(
                    code = -1,
                    message = "TODO功能需要登录，请先登录您的账号",
                    type = ErrorHandler.ErrorType.COLLECT_NOT_LOGIN
                )
            )
            return
        }

        viewModelScope.launch {
            val result = networkRepository.addTodo(title, content, date, type, priority)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    LogUtils.d(TAG, "Todo added successfully")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                    refreshData()
                } else {
                    val errorMsg = "添加TODO失败: ${response?.errorMsg}"
                    LogUtils.e(TAG, errorMsg)
                    _uiState.value = _uiState.value.copy(
                        errorMessage = errorMsg
                    )
                    ErrorHandler.emitErrorEvent(
                        ErrorHandler.ErrorEvent(
                            code = response?.errorCode ?: -1,
                            message = errorMsg,
                            type = ErrorHandler.ErrorType.SERVER
                        )
                    )
                }
            } else {
                val errorMsg = "网络错误: ${result.exceptionOrNull()?.message}"
                LogUtils.e(TAG, errorMsg)
                _uiState.value = _uiState.value.copy(
                    errorMessage = errorMsg
                )
                ErrorHandler.emitErrorEvent(
                    ErrorHandler.ErrorEvent(
                        code = ErrorHandler.ErrorType.NETWORK.ordinal,
                        message = errorMsg,
                        type = ErrorHandler.ErrorType.NETWORK
                    )
                )
            }
        }
    }

    fun updateTodo(
        id: Int,
        title: String,
        content: String,
        date: Long,
        type: Int,
        priority: Int,
        status: Int
    ) {
        if (!userManager.isLoggedIn()) {
            ErrorHandler.emitErrorEvent(
                ErrorHandler.ErrorEvent(
                    code = -1,
                    message = "TODO功能需要登录，请先登录您的账号",
                    type = ErrorHandler.ErrorType.COLLECT_NOT_LOGIN
                )
            )
            return
        }

        viewModelScope.launch {
            val result = networkRepository.updateTodo(id, title, content, date, type, priority, status)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    LogUtils.d(TAG, "Todo updated successfully: $id")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                    refreshData()
                } else {
                    val errorMsg = "更新TODO失败: ${response?.errorMsg}"
                    LogUtils.e(TAG, errorMsg)
                    _uiState.value = _uiState.value.copy(
                        errorMessage = errorMsg
                    )
                    ErrorHandler.emitErrorEvent(
                        ErrorHandler.ErrorEvent(
                            code = response?.errorCode ?: -1,
                            message = errorMsg,
                            type = ErrorHandler.ErrorType.SERVER
                        )
                    )
                }
            } else {
                val errorMsg = "网络错误: ${result.exceptionOrNull()?.message}"
                LogUtils.e(TAG, errorMsg)
                _uiState.value = _uiState.value.copy(
                    errorMessage = errorMsg
                )
                ErrorHandler.emitErrorEvent(
                    ErrorHandler.ErrorEvent(
                        code = ErrorHandler.ErrorType.NETWORK.ordinal,
                        message = errorMsg,
                        type = ErrorHandler.ErrorType.NETWORK
                    )
                )
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        if (!userManager.isLoggedIn()) {
            ErrorHandler.emitErrorEvent(
                ErrorHandler.ErrorEvent(
                    code = -1,
                    message = "TODO功能需要登录，请先登录您的账号",
                    type = ErrorHandler.ErrorType.COLLECT_NOT_LOGIN
                )
            )
            return
        }

        viewModelScope.launch {
            val result = networkRepository.deleteTodo(todo.id)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    LogUtils.d(TAG, "Todo deleted successfully: ${todo.id}")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null,
                        showDeleteDialog = false,
                        todoToDelete = null
                    )
                    refreshData()
                } else {
                    val errorMsg = "删除TODO失败: ${response?.errorMsg}"
                    LogUtils.e(TAG, errorMsg)
                    _uiState.value = _uiState.value.copy(
                        errorMessage = errorMsg,
                        showDeleteDialog = false,
                        todoToDelete = null
                    )
                    ErrorHandler.emitErrorEvent(
                        ErrorHandler.ErrorEvent(
                            code = response?.errorCode ?: -1,
                            message = errorMsg,
                            type = ErrorHandler.ErrorType.SERVER
                        )
                    )
                }
            } else {
                val errorMsg = "网络错误: ${result.exceptionOrNull()?.message}"
                LogUtils.e(TAG, errorMsg)
                _uiState.value = _uiState.value.copy(
                    errorMessage = errorMsg,
                    showDeleteDialog = false,
                    todoToDelete = null
                )
                ErrorHandler.emitErrorEvent(
                    ErrorHandler.ErrorEvent(
                        code = ErrorHandler.ErrorType.NETWORK.ordinal,
                        message = errorMsg,
                        type = ErrorHandler.ErrorType.NETWORK
                    )
                )
            }
        }
    }

    fun toggleTodoStatus(todo: Todo) {
        if (!userManager.isLoggedIn()) {
            ErrorHandler.emitErrorEvent(
                ErrorHandler.ErrorEvent(
                    code = -1,
                    message = "TODO功能需要登录，请先登录您的账号",
                    type = ErrorHandler.ErrorType.COLLECT_NOT_LOGIN
                )
            )
            return
        }

        val newStatus = if (todo.status == Todo.STATUS_INCOMPLETE) {
            Todo.STATUS_COMPLETED
        } else {
            Todo.STATUS_INCOMPLETE
        }

        viewModelScope.launch {
            val result = networkRepository.toggleTodoStatus(todo.id, newStatus)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    LogUtils.d(TAG, "Todo status toggled successfully: ${todo.id} -> $newStatus")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                    refreshData()
                } else {
                    val errorMsg = "切换状态失败: ${response?.errorMsg}"
                    LogUtils.e(TAG, errorMsg)
                    _uiState.value = _uiState.value.copy(
                        errorMessage = errorMsg
                    )
                    ErrorHandler.emitErrorEvent(
                        ErrorHandler.ErrorEvent(
                            code = response?.errorCode ?: -1,
                            message = errorMsg,
                            type = ErrorHandler.ErrorType.SERVER
                        )
                    )
                }
            } else {
                val errorMsg = "网络错误: ${result.exceptionOrNull()?.message}"
                LogUtils.e(TAG, errorMsg)
                _uiState.value = _uiState.value.copy(
                    errorMessage = errorMsg
                )
                ErrorHandler.emitErrorEvent(
                    ErrorHandler.ErrorEvent(
                        code = ErrorHandler.ErrorType.NETWORK.ordinal,
                        message = errorMsg,
                        type = ErrorHandler.ErrorType.NETWORK
                    )
                )
            }
        }
    }

    fun setFilterStatus(status: Int?) {
        _uiState.value = _uiState.value.copy(
            filterStatus = status,
            currentPage = 1,
            todos = emptyList()
        )
        loadTodoList()
    }

    fun setFilterType(type: Int?) {
        _uiState.value = _uiState.value.copy(
            filterType = type,
            currentPage = 1,
            todos = emptyList()
        )
        loadTodoList()
    }

    fun setFilterPriority(priority: Int?) {
        _uiState.value = _uiState.value.copy(
            filterPriority = priority,
            currentPage = 1,
            todos = emptyList()
        )
        loadTodoList()
    }

    fun setOrderBy(orderby: Int?) {
        _uiState.value = _uiState.value.copy(
            orderby = orderby,
            currentPage = 1,
            todos = emptyList()
        )
        loadTodoList()
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                currentPage = 1,
                isRefreshing = true,
                todos = emptyList()
            )
            loadTodoListFromNetwork()
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.value = currentState.copy(
                currentPage = currentState.currentPage + 1
            )
            viewModelScope.launch {
                loadTodoListFromNetwork()
            }
        }
    }

    fun showDeleteDialog(todo: Todo) {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = true,
            todoToDelete = todo
        )
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = false,
            todoToDelete = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun getTodayDate(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }
}
