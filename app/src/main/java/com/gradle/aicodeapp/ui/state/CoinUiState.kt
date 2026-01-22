package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.CoinRank
import com.gradle.aicodeapp.network.model.CoinRecord
import com.gradle.aicodeapp.network.model.CoinUserInfo

data class CoinUiState(
    val coinUserInfo: CoinUserInfo? = null,
    val coinRankList: List<CoinRank> = emptyList(),
    val coinRecordList: List<CoinRecord> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val currentRankPage: Int = 1,
    val hasMoreRankData: Boolean = true,
    val currentRecordPage: Int = 1,
    val hasMoreRecordData: Boolean = true,
    val isLoginRequired: Boolean = false
)
