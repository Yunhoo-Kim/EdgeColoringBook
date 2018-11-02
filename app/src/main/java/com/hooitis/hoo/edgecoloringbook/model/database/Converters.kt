package com.hooitis.hoo.edgecoloringbook.model.database

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Suppress("unused")
class Converters{
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromArray(list: List<String>): String{
        return Gson().toJson(list)
    }
}