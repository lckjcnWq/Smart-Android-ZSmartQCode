package com.theswitchbot.common.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .registerTypeAdapter(Int::class.java, JsonDeserializer { json, _, _ ->
            try {
                return@JsonDeserializer json.asInt
            } catch (e: Exception) {
                return@JsonDeserializer -1
            }
        })
        .registerTypeAdapter(Long::class.java, JsonDeserializer { json, _, _ ->
            try {
                return@JsonDeserializer json.asLong
            } catch (e: Exception) {
                return@JsonDeserializer (-1).toLong()
            }
        })
        .registerTypeAdapter(Float::class.java, JsonDeserializer { json, _, _ ->
            try {
                return@JsonDeserializer json.asFloat
            } catch (e: Exception) {
                return@JsonDeserializer (-1).toFloat()
            }
        })
        .registerTypeAdapter(Double::class.java, JsonDeserializer { json, _, _ ->
            try {
                return@JsonDeserializer json.asDouble
            } catch (e: Exception) {
                return@JsonDeserializer (-1).toDouble()
            }
        })
        .registerTypeAdapter(Boolean::class.java,  JsonDeserializer { json, _, _ ->
            try {
                return@JsonDeserializer json.asBoolean
            } catch (e: Exception) {
                return@JsonDeserializer false
            }
        })
        .registerTypeAdapter(object : TypeToken<Map<String, Any>>() {}.type,
            MapDeserializerDoubleAsIntFix())
//        .serializeNulls() //该选项会导致空值也被序列化
        .setPrettyPrinting()
        .create()
