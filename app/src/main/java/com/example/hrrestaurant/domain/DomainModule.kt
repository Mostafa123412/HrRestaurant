package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.MealRepository
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
    fun provideMealDetailsUseCase(mealRepository: MealRepository): GetMealDetailsUseCase =
        GetMealDetailsUseCase(mealRepository)
 @Singleton
    @Provides
    fun provideGetOrderStatusUseCase(repository: MealRepository): GetOrderStatusUseCase =
     GetOrderStatusUseCase(repository)

    @Singleton
    @Provides
    fun provideAddOrderToCacheUseCase(mealRepository: MealRepository): AddOrderToCacheUseCase =
        AddOrderToCacheUseCase(mealRepository)

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
    fun provideGetCartItemsUseCase(mealRepository: MealRepository): GetCartItemsUseCase =
        GetCartItemsUseCase(mealRepository)

    @Singleton
    @Provides
    fun provideGetOrderTableRowNumbers(mealRepository: MealRepository): GetOrderTableRowNumbers =
        GetOrderTableRowNumbers(mealRepository)

    @Singleton
    @Provides
    fun provideGetUserOrdersIdUseCase(mealRepository: MealRepository): GetUserOrdersIdUseCase =
        GetUserOrdersIdUseCase(mealRepository)
}