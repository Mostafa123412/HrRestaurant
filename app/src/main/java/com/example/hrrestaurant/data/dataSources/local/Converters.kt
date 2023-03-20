package com.example.hrrestaurant.data.dataSources.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {

//    @TypeConverter
//    fun fromCroissantList(value: List<Meal>): String {
//        val gson = Gson()
//        val type = object : TypeToken<List<Meal>>() {}.type
//        return gson.toJson(value, type)
//    }

    @TypeConverter
    //tells room whenever you see ByteArray convert it into a bitmap
    fun fromBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()

    }

    @TypeConverter
    fun fromByteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    @TypeConverter
    fun stringToMap(value: String): HashMap<String, Int> {
        return Gson().fromJson(value,  object : TypeToken<HashMap<String, Int>>() {}.type)
    }

    @TypeConverter
    fun mapToString(value: HashMap<String, Int>): String {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun stringOrderInfoToMap(value: String): HashMap<String, String> {
        return Gson().fromJson(value,  object : TypeToken<HashMap<String, String>>() {}.type)
    }

    @TypeConverter
    fun mapToOrderInfoString(value: HashMap<String, String>): String {
        return Gson().toJson(value)
    }

//    @TypeConverter
//    fun toCroissantList(value: String): List<Meal> {
//        val gson = Gson()
//        val type = object : TypeToken<List<Meal>>() {}.type
//        return gson.fromJson(value, type)
//    }
}