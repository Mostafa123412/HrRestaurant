package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {


    @Singleton
    @Provides
    fun provideMealDetailsUseCase(repository: Repository): GetMealDetailsUseCase =
        GetMealDetailsUseCase(repository)
 @Singleton
    @Provides
    fun provideGetOrderStatusUseCase(repository: Repository): GetOrderStatusUseCase =
     GetOrderStatusUseCase(repository)

    @Singleton
    @Provides
    fun provideAddOrderToCacheUseCase(repository: Repository): AddOrderToCacheUseCase =
        AddOrderToCacheUseCase(repository)

    @Singleton
    @Provides
    fun provideCreateFireStoreOrderUseCase(
        addListenerRegistrationUseCase: AddListenerRegistrationUseCase,
    ): CreateFireStoreOrderUseCase =
        CreateFireStoreOrderUseCase(addListenerRegistrationUseCase)

    @Singleton
    @Provides
    fun provideAddListenerRegistrationUseCase(addOrderToCacheUseCase: AddOrderToCacheUseCase):
            AddListenerRegistrationUseCase = AddListenerRegistrationUseCase(addOrderToCacheUseCase)

    @Singleton
    @Provides
    fun provideCreateOrderUseCase() = CreateOrderUseCase()

    @Singleton
    @Provides
    fun provideGetCartItemsUseCase(repository: Repository): GetCartItemsUseCase =
        GetCartItemsUseCase(repository)

    @Singleton
    @Provides
    fun provideGetOrderTableRowNumbers(repository: Repository): GetOrderTableRowNumbers =
        GetOrderTableRowNumbers(repository)

    @Singleton
    @Provides
    fun provideGetUserOrdersIdUseCase(repository: Repository): GetUserOrdersIdUseCase =
        GetUserOrdersIdUseCase(repository)
}