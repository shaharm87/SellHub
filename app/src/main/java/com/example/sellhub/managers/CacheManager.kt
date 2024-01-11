package com.example.sellhub.managers

import android.content.Context
import android.content.SharedPreferences
import com.example.sellhub.newitem.Item
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken

class CacheManager<T>(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Cache", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveList(key: String, list: List<T>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun <T> getList(key: String, classOfT: Class<T>): List<T> {
        val json = sharedPreferences.getString(key, "")
        val typeToken = object : TypeToken<List<LinkedTreeMap<String, Any>>>() {}.type
        val list: List<LinkedTreeMap<String, Any>> = gson.fromJson(json, typeToken) ?: emptyList()

        return list.map { gson.fromJson(gson.toJsonTree(it), classOfT) }
    }
}