package com.carry1st_shop.datasource

import com.carry1st_shop.models.Product
import kotlinx.coroutines.flow.Flow
import com.carry1st_shop.utils.Result

interface ProductDataSource {
     fun getProducts(): Flow<Result<List<Product>>>
}