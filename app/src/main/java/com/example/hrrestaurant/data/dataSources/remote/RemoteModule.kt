package com.example.hrrestaurant.data.dataSources.remote

import com.example.hrrestaurant.ui.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RemoteModule {
    @Provides
    @Singleton
    fun provideApiInstance():HrApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HrApi::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSourceInstance (api: HrApi):RemoteDataSource = RemoteDataSource(api)

}