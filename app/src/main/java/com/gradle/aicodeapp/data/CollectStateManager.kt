package com.gradle.aicodeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.collectDataStore: DataStore<Preferences> by preferencesDataStore(name = "collect_preferences")

@Singleton
class CollectStateManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun setCollectState(articleId: Int, isCollected: Boolean) {
        context.collectDataStore.edit { preferences ->
            preferences[booleanPreferencesKey("collected_$articleId")] = isCollected
        }
    }

    fun getCollectState(articleId: Int): Flow<Boolean> {
        return context.collectDataStore.data.map { preferences ->
            preferences[booleanPreferencesKey("collected_$articleId")] ?: false
        }
    }

    suspend fun removeCollectState(articleId: Int) {
        context.collectDataStore.edit { preferences ->
            preferences.remove(booleanPreferencesKey("collected_$articleId"))
        }
    }

    suspend fun clearAllCollectStates() {
        context.collectDataStore.edit { preferences ->
            val keysToRemove = preferences.asMap().keys.filter { 
                it.name.startsWith("collected_") 
            }
            keysToRemove.forEach { key ->
                preferences.remove(key)
            }
        }
    }
}
