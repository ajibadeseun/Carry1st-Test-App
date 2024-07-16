package com.carry1st_shop.di

import com.carry1st_shop.apiInterface.ProductsApiService
import com.carry1st_shop.datasource.ProductDataSource
import com.carry1st_shop.datasource.ProductRepository
import com.carry1st_shop.datasource.RemoteProductDataSource
import com.carry1st_shop.datasource.RemoteProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideProductRepository(
        productDataSource: RemoteProductDataSource
    ): ProductRepository {
        return RemoteProductRepository(productDataSource)
    }
}