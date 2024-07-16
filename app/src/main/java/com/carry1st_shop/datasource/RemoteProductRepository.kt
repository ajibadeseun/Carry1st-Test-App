package com.carry1st_shop.datasource

import com.carry1st_shop.models.Product
import com.carry1st_shop.utils.Result
import kotlinx.coroutines.flow.Flow

class RemoteProductRepository (
    private val productDataSource: RemoteProductDataSource,
): ProductRepository  {
    override  fun getProducts(): Flow<Result<List<Product>>> = productDataSource.getProducts()
}