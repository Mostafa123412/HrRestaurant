package com.example.hrrestaurant.data.dataSources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [Meal::class], version = 1)
@TypeConverters(Converters::class)
abstract class MyDatabase: RoomDatabase() {
    abstract fun dao():Dao

//        companion object {
//        @Volatile
//        var databaseInstance: MyDatabase? = null
//
//        fun getDatabaseInstance(context: Context): MyDatabase {
//            return databaseInstance ?: synchronized(this) {
//                databaseInstance = Room.databaseBuilder(
//                    context.applicationContext,
//                    MyDatabase::class.java,
//                    "my_database"
//                ).fallbackToDestructiveMigration().build()
//                return databaseInstance!!
//            }
//        }
//
//    }
}