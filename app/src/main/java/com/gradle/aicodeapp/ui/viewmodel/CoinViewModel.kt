package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.CoinUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinUiState())
    val uiState: StateFlow<CoinUiState> = _uiState

    private val TAG = "CoinViewModel"

    fun loadData() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            loadCoinUserInfo()
        }

        viewModelScope.launch {
            loadCoinRank(1)
        }
    }

    private suspend fun loadCoinUserInfo() {
        val result = networkRepository.getCoinUserInfo()
        if (result.isSuccess) {
            val response = result.getOrNull()
            if (response?.isSuccess() == true) {
                _uiState.value = _uiState.value.copy(
                    coinUserInfo = response.data,
                    isLoginRequired = false,
                    isLoading = false
                )
            } else {
                if (response?.isLoginExpired() == true) {
                    _uiState.value = _uiState.value.copy(
                        isLoginRequired = true,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "获取积分信息失败: ${response?.errorMsg}",
                        isLoading = false
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                isLoading = false
            )
        }
    }

    private suspend fun loadCoinRank(page: Int) {
        val result = networkRepository.getCoinRank(page)
        if (result.isSuccess) {
            val response = result.getOrNull()
            if (response?.isSuccess() == true) {
                val newRanks = response.data?.datas ?: emptyList()
                val updatedRanks = if (page == 1) {
                    newRanks
                } else {
                    _uiState.value.coinRankList + newRanks
                }

                _uiState.value = _uiState.value.copy(
                    coinRankList = updatedRanks,
                    currentRankPage = page,
                    hasMoreRankData = response.data?.over != true,
                    isLoading = false,
                    isLoadingMore = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "获取排行榜失败: ${response?.errorMsg}",
                    isLoading = false,
                    isLoadingMore = false
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                isLoading = false,
                isLoadingMore = false
            )
        }
    }

    fun loadMoreRank() {
        val currentState = _uiState.value
        if (!currentState.isLoadingMore && currentState.hasMoreRankData) {
            _uiState.value = currentState.copy(isLoadingMore = true)
            viewModelScope.launch {
                loadCoinRank(currentState.currentRankPage + 1)
            }
        }
    }

    fun refreshData() {
        _uiState.value = _uiState.value.copy(
            currentRankPage = 1,
            isLoading = true,
            isRefreshing = true,
            coinRankList = emptyList(),
            coinRecordList = emptyList()
        )

        viewModelScope.launch {
            loadCoinUserInfo()
        }

        viewModelScope.launch {
            loadCoinRank(1)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun loadCoinRecords(page: Int = 1) {
        viewModelScope.launch {
            val result = networkRepository.getCoinRecordList(page)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    val newRecords = response.data?.datas ?: emptyList()
                    val updatedRecords = if (page == 1) {
                        newRecords
                    } else {
                        _uiState.value.coinRecordList + newRecords
                    }

                    _uiState.value = _uiState.value.copy(
                        coinRecordList = updatedRecords,
                        currentRecordPage = page,
                        hasMoreRecordData = response.data?.over != true,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "获取积分记录失败: ${response?.errorMsg}",
                        isLoading = false
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadMoreRecords() {
        val currentState = _uiState.value
        if (!currentState.isLoadingMore && currentState.hasMoreRecordData) {
            _uiState.value = currentState.copy(isLoadingMore = true)
            viewModelScope.launch {
                val result = networkRepository.getCoinRecordList(currentState.currentRecordPage + 1)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response?.isSuccess() == true) {
                        val newRecords = response.data?.datas ?: emptyList()
                        val updatedRecords = currentState.coinRecordList + newRecords

                        _uiState.value = currentState.copy(
                            coinRecordList = updatedRecords,
                            currentRecordPage = currentState.currentRecordPage + 1,
                            hasMoreRecordData = response.data?.over != true,
                            isLoadingMore = false
                        )
                    } else {
                        _uiState.value = currentState.copy(
                            errorMessage = "获取积分记录失败: ${response?.errorMsg}",
                            isLoadingMore = false
                        )
                    }
                } else {
                    _uiState.value = currentState.copy(
                        errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                        isLoadingMore = false
                    )
                }
            }
        }
    }
}
