package com.carry1st_shop

import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.carry1st_shop.datasource.ProductRepository
import com.carry1st_shop.models.Product
import com.carry1st_shop.usecases.GetProductsUseCase
import com.carry1st_shop.utils.ErrorResponse
import com.carry1st_shop.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetProductsUseCaseTest {
    private lateinit var getProductsUseCase: GetProductsUseCase
    private val productRepository: ProductRepository = mockk()

    @Before
    fun setUp() {
        getProductsUseCase = GetProductsUseCase(productRepository)
    }

    @Test
    fun testInvokeSuccess() = runTest {
        val productList = listOf(
            Product(11, "10 Lives", "10 Lives product bundle.", 1.0, "USD", "$", 10, "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/74e517a3-0615-4019-bb08-cc697cf4e747.png", "ACTIVE")
        )

        coEvery { productRepository.getProducts() } returns flow {
            emit(Result.Success(productList))
        }

        val result = getProductsUseCase.invoke().toList()

        assertEquals(1, result.size)
        assert(result[0] is Result.Success)
        assertEquals(productList, (result[0] as Result.Success).data)
    }

    @Test
    fun testInvokeFailure() = runTest {
        val errorMessage = "An error has occurred"
        val exception = ErrorResponse(message =  errorMessage, errors = listOf(), isSuccessful = false, title = "Oops!")

        coEvery { productRepository.getProducts() } returns flow {
            emit(Result.Error(exception))
        }

        val result = getProductsUseCase.invoke().toList()

        assertEquals(1, result.size)
        assert(result[0] is Result.Error)
        assertEquals(exception, (result[0] as Result.Error).errorResponse)
    }
}