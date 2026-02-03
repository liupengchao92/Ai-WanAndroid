package com.gradle.aicodeapp.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * JSON工具类，封装Gson库提供便捷的JSON序列化和反序列化功能
 */
object JsonUtils {

    private const val TAG = "JsonUtils"

    /**
     * 默认的Gson实例，包含常用的配置
     */
    @JvmStatic
    val gson: Gson by lazy {
        GsonBuilder()
            .setPrettyPrinting() // 格式化输出
            .serializeNulls()    // 序列化null值
            .create()
    }

    /**
     * 紧凑格式的Gson实例（无格式化）
     */
    private val compactGson: Gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .create()
    }

    /**
     * 将对象序列化为JSON字符串
     * @param obj 要序列化的对象
     * @param prettyPrint 是否格式化输出，默认为false
     * @return JSON字符串，序列化失败返回null
     */
    @JvmStatic
    @JvmOverloads
    fun toJson(obj: Any?, prettyPrint: Boolean = false): String? {
        if (obj == null) return null
        return try {
            if (prettyPrint) {
                gson.toJson(obj)
            } else {
                compactGson.toJson(obj)
            }
        } catch (e: Exception) {
            LogUtils.e(TAG, "toJson failed: ${e.message}", e)
            null
        }
    }

    /**
     * 将JSON字符串反序列化为指定类型的对象
     * @param json JSON字符串
     * @param clazz 目标类型的Class
     * @return 反序列化后的对象，失败返回null
     */
    @JvmStatic
    fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        if (json.isNullOrBlank()) return null
        return try {
            gson.fromJson(json, clazz)
        } catch (e: JsonSyntaxException) {
            LogUtils.e(TAG, "fromJson failed: ${e.message}", e)
            null
        } catch (e: Exception) {
            LogUtils.e(TAG, "fromJson failed: ${e.message}", e)
            null
        }
    }

    /**
     * 将JSON字符串反序列化为指定类型的对象（支持泛型）
     * @param json JSON字符串
     * @param type TypeToken获取的类型
     * @return 反序列化后的对象，失败返回null
     */
    @JvmStatic
    fun <T> fromJson(json: String?, type: Type): T? {
        if (json.isNullOrBlank()) return null
        return try {
            gson.fromJson(json, type)
        } catch (e: JsonSyntaxException) {
            LogUtils.e(TAG, "fromJson failed: ${e.message}", e)
            null
        } catch (e: Exception) {
            LogUtils.e(TAG, "fromJson failed: ${e.message}", e)
            null
        }
    }

    /**
     * 将JSON字符串反序列化为List集合
     * @param json JSON字符串
     * @param clazz List中元素的Class类型
     * @return 反序列化后的List，失败返回空List
     */
    @JvmStatic
    fun <T> fromJsonToList(json: String?, clazz: Class<T>): List<T> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val type = TypeToken.getParameterized(List::class.java, clazz).type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            LogUtils.e(TAG, "fromJsonToList failed: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * 将JSON字符串反序列化为Map集合
     * @param json JSON字符串
     * @param keyClass Map中Key的Class类型
     * @param valueClass Map中Value的Class类型
     * @return 反序列化后的Map，失败返回空Map
     */
    @JvmStatic
    fun <K, V> fromJsonToMap(
        json: String?,
        keyClass: Class<K>,
        valueClass: Class<V>
    ): Map<K, V> {
        if (json.isNullOrBlank()) return emptyMap()
        return try {
            val type = TypeToken.getParameterized(Map::class.java, keyClass, valueClass).type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            LogUtils.e(TAG, "fromJsonToMap failed: ${e.message}", e)
            emptyMap()
        }
    }

    /**
     * 将JSON字符串反序列化为Set集合
     * @param json JSON字符串
     * @param clazz Set中元素的Class类型
     * @return 反序列化后的Set，失败返回空Set
     */
    @JvmStatic
    fun <T> fromJsonToSet(json: String?, clazz: Class<T>): Set<T> {
        if (json.isNullOrBlank()) return emptySet()
        return try {
            val type = TypeToken.getParameterized(Set::class.java, clazz).type
            gson.fromJson(json, type) ?: emptySet()
        } catch (e: Exception) {
            LogUtils.e(TAG, "fromJsonToSet failed: ${e.message}", e)
            emptySet()
        }
    }

    /**
     * 解析JSON字符串为JsonElement对象
     * @param json JSON字符串
     * @return JsonElement对象，解析失败返回null
     */
    @JvmStatic
    fun parseJson(json: String?): JsonElement? {
        if (json.isNullOrBlank()) return null
        return try {
            JsonParser.parseString(json)
        } catch (e: JsonSyntaxException) {
            LogUtils.e(TAG, "parseJson failed: ${e.message}", e)
            null
        }
    }

    /**
     * 解析JSON字符串为JsonObject
     * @param json JSON字符串
     * @return JsonObject对象，解析失败或非对象类型返回null
     */
    @JvmStatic
    fun parseJsonObject(json: String?): JsonObject? {
        if (json.isNullOrBlank()) return null
        return try {
            val element = JsonParser.parseString(json)
            if (element.isJsonObject) element.asJsonObject else null
        } catch (e: JsonSyntaxException) {
            LogUtils.e(TAG, "parseJsonObject failed: ${e.message}", e)
            null
        }
    }

    /**
     * 解析JSON字符串为JsonArray
     * @param json JSON字符串
     * @return JsonArray对象，解析失败或非数组类型返回null
     */
    @JvmStatic
    fun parseJsonArray(json: String?): JsonArray? {
        if (json.isNullOrBlank()) return null
        return try {
            val element = JsonParser.parseString(json)
            if (element.isJsonArray) element.asJsonArray else null
        } catch (e: JsonSyntaxException) {
            LogUtils.e(TAG, "parseJsonArray failed: ${e.message}", e)
            null
        }
    }

    /**
     * 从JsonObject中获取指定字段的字符串值
     * @param jsonObject JsonObject对象
     * @param key 字段名
     * @return 字段值，不存在或类型不匹配返回null
     */
    @JvmStatic
    fun getString(jsonObject: JsonObject?, key: String): String? {
        if (jsonObject == null || !jsonObject.has(key)) return null
        return try {
            val element = jsonObject.get(key)
            if (element.isJsonNull) null else element.asString
        } catch (e: Exception) {
            LogUtils.e(TAG, "getString failed for key '$key': ${e.message}", e)
            null
        }
    }

    /**
     * 从JsonObject中获取指定字段的Int值
     * @param jsonObject JsonObject对象
     * @param key 字段名
     * @param defaultValue 默认值
     * @return 字段值，不存在或类型不匹配返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun getInt(jsonObject: JsonObject?, key: String, defaultValue: Int = 0): Int {
        if (jsonObject == null || !jsonObject.has(key)) return defaultValue
        return try {
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asInt
        } catch (e: Exception) {
            LogUtils.e(TAG, "getInt failed for key '$key': ${e.message}", e)
            defaultValue
        }
    }

    /**
     * 从JsonObject中获取指定字段的Long值
     * @param jsonObject JsonObject对象
     * @param key 字段名
     * @param defaultValue 默认值
     * @return 字段值，不存在或类型不匹配返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun getLong(jsonObject: JsonObject?, key: String, defaultValue: Long = 0L): Long {
        if (jsonObject == null || !jsonObject.has(key)) return defaultValue
        return try {
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asLong
        } catch (e: Exception) {
            LogUtils.e(TAG, "getLong failed for key '$key': ${e.message}", e)
            defaultValue
        }
    }

    /**
     * 从JsonObject中获取指定字段的Double值
     * @param jsonObject JsonObject对象
     * @param key 字段名
     * @param defaultValue 默认值
     * @return 字段值，不存在或类型不匹配返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun getDouble(jsonObject: JsonObject?, key: String, defaultValue: Double = 0.0): Double {
        if (jsonObject == null || !jsonObject.has(key)) return defaultValue
        return try {
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asDouble
        } catch (e: Exception) {
            LogUtils.e(TAG, "getDouble failed for key '$key': ${e.message}", e)
            defaultValue
        }
    }

    /**
     * 从JsonObject中获取指定字段的Boolean值
     * @param jsonObject JsonObject对象
     * @param key 字段名
     * @param defaultValue 默认值
     * @return 字段值，不存在或类型不匹配返回默认值
     */
    @JvmStatic
    @JvmOverloads
    fun getBoolean(jsonObject: JsonObject?, key: String, defaultValue: Boolean = false): Boolean {
        if (jsonObject == null || !jsonObject.has(key)) return defaultValue
        return try {
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asBoolean
        } catch (e: Exception) {
            LogUtils.e(TAG, "getBoolean failed for key '$key': ${e.message}", e)
            defaultValue
        }
    }

    /**
     * 从JsonObject中获取指定字段的JsonObject值
     * @param jsonObject JsonObject对象
     * @param key 字段名
     * @return JsonObject值，不存在或类型不匹配返回null
     */
    @JvmStatic
    fun getJsonObject(jsonObject: JsonObject?, key: String): JsonObject? {
        if (jsonObject == null || !jsonObject.has(key)) return null
        return try {
            val element = jsonObject.get(key)
            if (element.isJsonNull || !element.isJsonObject) null else element.asJsonObject
        } catch (e: Exception) {
            LogUtils.e(TAG, "getJsonObject failed for key '$key': ${e.message}", e)
            null
        }
    }

    /**
     * 从JsonObject中获取指定字段的JsonArray值
     * @param jsonObject JsonObject对象
     * @param key 字段名
     * @return JsonArray值，不存在或类型不匹配返回null
     */
    @JvmStatic
    fun getJsonArray(jsonObject: JsonObject?, key: String): JsonArray? {
        if (jsonObject == null || !jsonObject.has(key)) return null
        return try {
            val element = jsonObject.get(key)
            if (element.isJsonNull || !element.isJsonArray) null else element.asJsonArray
        } catch (e: Exception) {
            LogUtils.e(TAG, "getJsonArray failed for key '$key': ${e.message}", e)
            null
        }
    }

    /**
     * 验证字符串是否为有效的JSON格式
     * @param json 要验证的字符串
     * @return true: 是有效的JSON；false: 不是有效的JSON
     */
    @JvmStatic
    fun isValidJson(json: String?): Boolean {
        if (json.isNullOrBlank()) return false
        return try {
            JsonParser.parseString(json)
            true
        } catch (e: JsonSyntaxException) {
            false
        }
    }

    /**
     * 验证字符串是否为有效的JSON对象格式
     * @param json 要验证的字符串
     * @return true: 是有效的JSON对象；false: 不是有效的JSON对象
     */
    @JvmStatic
    fun isValidJsonObject(json: String?): Boolean {
        if (json.isNullOrBlank()) return false
        return try {
            val element = JsonParser.parseString(json)
            element.isJsonObject
        } catch (e: JsonSyntaxException) {
            false
        }
    }

    /**
     * 验证字符串是否为有效的JSON数组格式
     * @param json 要验证的字符串
     * @return true: 是有效的JSON数组；false: 不是有效的JSON数组
     */
    @JvmStatic
    fun isValidJsonArray(json: String?): Boolean {
        if (json.isNullOrBlank()) return false
        return try {
            val element = JsonParser.parseString(json)
            element.isJsonArray
        } catch (e: JsonSyntaxException) {
            false
        }
    }

    /**
     * 深度克隆一个对象（通过JSON序列化和反序列化）
     * @param obj 要克隆的对象
     * @param clazz 目标类型的Class
     * @return 克隆后的对象，失败返回null
     */
    @JvmStatic
    fun <T> deepClone(obj: T?, clazz: Class<T>): T? {
        if (obj == null) return null
        val json = toJson(obj)
        return fromJson(json, clazz)
    }

    /**
     * 将JsonElement转换为指定类型的对象
     * @param jsonElement JsonElement对象
     * @param clazz 目标类型的Class
     * @return 转换后的对象，失败返回null
     */
    @JvmStatic
    fun <T> fromJsonElement(jsonElement: JsonElement?, clazz: Class<T>): T? {
        if (jsonElement == null || jsonElement.isJsonNull) return null
        return try {
            gson.fromJson(jsonElement, clazz)
        } catch (e: Exception) {
            LogUtils.e(TAG, "fromJsonElement failed: ${e.message}", e)
            null
        }
    }

    /**
     * 将对象转换为JsonElement
     * @param obj 要转换的对象
     * @return JsonElement对象，失败返回null
     */
    @JvmStatic
    fun toJsonElement(obj: Any?): JsonElement? {
        if (obj == null) return null
        return try {
            gson.toJsonTree(obj)
        } catch (e: Exception) {
            LogUtils.e(TAG, "toJsonElement failed: ${e.message}", e)
            null
        }
    }

    /**
     * 创建一个新的空JsonObject
     * @return 新的JsonObject实例
     */
    @JvmStatic
    fun createJsonObject(): JsonObject {
        return JsonObject()
    }

    /**
     * 创建一个新的空JsonArray
     * @return 新的JsonArray实例
     */
    @JvmStatic
    fun createJsonArray(): JsonArray {
        return JsonArray()
    }

}
