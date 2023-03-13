package com.example.hrrestaurant.data.dataSources.local

import android.content.Context
import androidx.room.Room
import com.example.hrrestaurant.data.dataSources.remote.RemoteDataSource
import com.example.hrrestaurant.data.repositories.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Provides
    @Singleton
    fun getLocalDataBaseInstance(@ApplicationContext context: Context): MyDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            MyDatabase::class.java,
            "database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideDao(database: MyDatabase): Dao = database.dao()


    @Provides
    @Singleton
    fun provideLocalDataSourceInstance(dao:Dao): LocalDataSource =
        LocalDataSource(dao)

    @Provides
    @Singleton
    fun getBaseRepositoryInstance(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        mealMapper: MealMapper
    ): Repository {
        return Repository(localDataSource, remoteDataSource,mealMapper)
    }

    @Provides
    fun provideMealDtoMapper(): MealMapper = MealMapper()

}