package com.gradle.aicodeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.pendingActionDataStore: DataStore<Preferences> by preferencesDataStore(name = "pending_action_preferences")

@Singleton
class PendingActionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val KEY_ACTION_TYPE = "action_type"
        private const val KEY_ARTICLE_ID = "article_id"
        private const val KEY_SHOULD_COLLECT = "should_collect"
        
        const val ACTION_COLLECT = "collect"
        const val ACTION_NONE = "none"
    }

    suspend fun setPendingCollectAction(articleId: Int, shouldCollect: Boolean) {
        context.pendingActionDataStore.edit { preferences ->
            preferences[stringPreferencesKey(KEY_ACTION_TYPE)] = ACTION_COLLECT
            preferences[stringPreferencesKey(KEY_ARTICLE_ID)] = articleId.toString()
            preferences[stringPreferencesKey(KEY_SHOULD_COLLECT)] = shouldCollect.toString()
        }
    }

    suspend fun clearPendingAction() {
        context.pendingActionDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getPendingAction(): Flow<PendingAction?> {
        return context.pendingActionDataStore.data.map { preferences ->
            val actionType = preferences[stringPreferencesKey(KEY_ACTION_TYPE)]
            if (actionType == ACTION_COLLECT) {
                val articleId = preferences[stringPreferencesKey(KEY_ARTICLE_ID)]?.toIntOrNull() ?: 0
                val shouldCollect = preferences[stringPreferencesKey(KEY_SHOULD_COLLECT)]?.toBoolean() ?: false
                PendingAction.Collect(articleId, shouldCollect)
            } else {
                null
            }
        }
    }
}

sealed class PendingAction {
    data class Collect(val articleId: Int, val shouldCollect: Boolean) : PendingAction()
}
