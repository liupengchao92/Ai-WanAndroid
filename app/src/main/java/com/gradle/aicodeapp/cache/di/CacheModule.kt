package com.gradle.aicodeapp.cache.di

import com.gradle.aicodeapp.cache.DataCacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideDataCacheManager(): DataCacheManager {
        return DataCacheManager()
    }
}
