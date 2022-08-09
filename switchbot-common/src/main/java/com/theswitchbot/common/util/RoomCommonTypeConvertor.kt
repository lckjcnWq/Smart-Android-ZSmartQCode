package com.theswitchbot.common.util

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

class RoomCommonTypeConvertor {

    @TypeConverter
    fun stringToObject(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {

        }.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToBoolList(value: String): MutableList<Boolean> {
        val listType = object : TypeToken<MutableList<Boolean>>() {

        }.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun booListToString(list: MutableList<Boolean>): String {
        return gson.toJson(list)
    }


    @TypeConverter
    fun stringToLong(value: String): Long {
        return value.toLong()
    }

    @TypeConverter
    fun longToString(value: Long): String {
        return value.toString()
    }


}