package com.example.hrrestaurant.data.dataSources.localDataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Meal::class, Order::class], version = 3)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun dao(): Dao

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