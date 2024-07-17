package com.carry1st_shop.usecases

import com.carry1st_shop.datasource.ProductRepository
import com.carry1st_shop.models.Product
import com.carry1st_shop.utils.ErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.carry1st_shop.utils.Result

class GetProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {
    operator fun invoke(): Flow<Result<List<Product>>> {
        val errorMessage = "An error has occurred"
        return productRepository.getProducts().map { result ->
            when (result) {
                is Result.Error -> Result.Error(result.errorResponse?: ErrorResponse(
                    message =  errorMessage, errors = listOf(), isSuccessful = false, title = "Oops!"
                ))
                is Result.Success -> {
                    Result.Success(result.data ?: listOf())
                }
            }

        }
    }
}