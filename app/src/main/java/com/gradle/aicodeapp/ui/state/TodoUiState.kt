package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.Todo

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val filterStatus: Int? = null,
    val filterType: Int? = null,
    val filterPriority: Int? = null,
    val orderby: Int? = null,
    val showDeleteDialog: Boolean = false,
    val todoToDelete: Todo? = null
) {
    companion object {
        val STATUS_ALL: Int? = null
        val STATUS_INCOMPLETE = 0
        val STATUS_COMPLETED = 1

        val TYPE_ALL: Int? = null
        val TYPE_WORK = 1
        val TYPE_LIFE = 2
        val TYPE_ENTERTAINMENT = 3

        val PRIORITY_ALL: Int? = null
        val PRIORITY_HIGH = 1
        val PRIORITY_NORMAL = 2

        val ORDERBY_CREATE_DESC: Int? = null
        val ORDERBY_CREATE_ASC = 1
        val ORDERBY_COMPLETE_DESC = 2
        val ORDERBY_COMPLETE_ASC = 3
    }
}
