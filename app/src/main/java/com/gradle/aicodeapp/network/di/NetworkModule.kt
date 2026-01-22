package com.gradle.aicodeapp.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.network.api.ApiService
import com.gradle.aicodeapp.network.interceptor.GlobalErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.wanandroid.com/"
    private const val TIMEOUT_SECONDS = 30L

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideGlobalErrorInterceptor(gson: Gson): GlobalErrorInterceptor {
        return GlobalErrorInterceptor(gson)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        globalErrorInterceptor: GlobalErrorInterceptor,
        userManager: UserManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(globalErrorInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val cookie = userManager.getCookie()
                val requestBuilder = original.newBuilder()
                
                if (cookie != null) {
                    requestBuilder.addHeader("Cookie", cookie)
                }
                
                val request = requestBuilder.build()
                val response = chain.proceed(request)
                
                val setCookieHeader = response.headers("Set-Cookie")
                if (setCookieHeader.isNotEmpty()) {
                    val cookieValue = setCookieHeader.joinToString("; ")
                    userManager.saveCookie(cookieValue)
                }
                
                response
            }
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
