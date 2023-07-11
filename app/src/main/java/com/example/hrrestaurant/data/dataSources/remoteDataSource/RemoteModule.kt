package com.example.hrrestaurant.data.dataSources.remoteDataSource

import com.example.hrrestaurant.ui.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun provideApiInstance(): HrApi = provideRetrofitInstance().create(HrApi::class.java)

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl("https://a947d433-4ea2-49ad-b982-0d03e0d461f4.mock.pstmn.io/")
        .addConverterFactory(provideGsonConverterFactory())
        .client(provideClient())
        .build()


    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSourceInstance(api: HrApi): RemoteDataSource = RemoteDataSource(api)

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .callTimeout(10000, TimeUnit.MILLISECONDS)
            .build()
    }

}