package com.gradle.aicodeapp.network.interceptor

import com.gradle.aicodeapp.network.exception.ApiException
import com.gradle.aicodeapp.network.exception.ErrorCode
import com.gradle.aicodeapp.network.exception.ErrorLogger
import com.gradle.aicodeapp.network.exception.NotLoginException
import com.gradle.aicodeapp.network.exception.ServerException
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException

class GlobalErrorInterceptor(private val gson: Gson) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url.toString()
        val requestParams = getRequestParams(request)

        return try {
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
                val errorMsg = "HTTP ${response.code}: ${response.message}"
                ErrorLogger.logNetworkError(requestUrl, IOException(errorMsg))
                return response
            }

            val responseBody = response.body ?: return response

            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer
            buffer.clone()

            val contentType = responseBody.contentType()
            val charset = contentType?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
            val responseString = buffer.readString(charset)

            try {
                val apiResponse = gson.fromJson(responseString, com.gradle.aicodeapp.network.model.ApiResponse::class.java)
                
                if (apiResponse != null) {
                    val errorCode = apiResponse.errorCode
                    
                    if (errorCode != ErrorCode.SUCCESS) {
                        val errorMsg = apiResponse.errorMsg ?: "未知错误"
                        
                        ErrorLogger.logApiError(
                            errorCode = errorCode,
                            errorMsg = errorMsg,
                            requestUrl = requestUrl,
                            requestParams = requestParams
                        )

                        if (errorCode == ErrorCode.ERROR_NOT_LOGIN) {
                            throw NotLoginException(errorMsg)
                        } else {
                            throw ServerException(errorCode, errorMsg)
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is ApiException) {
                    throw e
                }
            }

            response.newBuilder()
                .body(responseString.toResponseBody(contentType))
                .build()

        } catch (e: IOException) {
            ErrorLogger.logNetworkError(requestUrl, e)
            throw e
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            ErrorLogger.logNetworkError(requestUrl, e)
            throw IOException(e)
        }
    }

    private fun getRequestParams(request: okhttp3.Request): String {
        val url = request.url
        val params = StringBuilder()
        
        url.queryParameterNames.forEach { name ->
            url.queryParameter(name)?.let { value ->
                if (params.isNotEmpty()) params.append("&")
                params.append("$name=$value")
            }
        }

        val body = request.body
        if (body != null) {
            val buffer = Buffer()
            body.writeTo(buffer)
            val bodyString = buffer.readUtf8()
            if (bodyString.isNotEmpty()) {
                if (params.isNotEmpty()) params.append("&")
                params.append(bodyString)
            }
        }

        return params.toString()
    }
}
