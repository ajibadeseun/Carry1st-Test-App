package com.carry1st_shop.datasource

import com.carry1st_shop.models.Product
import com.carry1st_shop.utils.Result
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
     fun getProducts(): Flow<Result<List<Product>>>
}