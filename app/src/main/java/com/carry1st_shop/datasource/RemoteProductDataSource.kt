package com.carry1st_shop.datasource

import com.carry1st_shop.apiInterface.ProductsApiService
import com.carry1st_shop.models.Product
import com.carry1st_shop.utils.Result
import com.carry1st_shop.utils.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteProductDataSource @Inject constructor(
    private val productsApiService: ProductsApiService,
)  {
      fun getProducts(): Flow<Result<List<Product>>> {
        return apiRequestFlow {
            productsApiService.getProducts().let {
                if (it.isSuccessful) {
                // Other operations/manipulations can be done after successful calls
                }
                it
            }
        }
    }
}