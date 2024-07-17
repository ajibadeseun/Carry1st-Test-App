package com.carry1st_shop
import com.carry1st_shop.datasource.RemoteProductDataSource
import com.carry1st_shop.datasource.RemoteProductRepository
import com.carry1st_shop.models.Product
import com.carry1st_shop.utils.ErrorResponse
import com.carry1st_shop.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RemoteProductRepositoryTest {
    private lateinit var repository: RemoteProductRepository
    private val productDataSource: RemoteProductDataSource = mockk()

    @Before
    fun setUp() {
        repository = RemoteProductRepository(productDataSource)
    }

    @Test
    fun testGetProductsSuccess() = runTest {
        val productList = listOf(
            Product(11, "10 Lives", "10 Lives product bundle.", 1.0, "USD", "$", 10, "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/74e517a3-0615-4019-bb08-cc697cf4e747.png", "ACTIVE")
        )

        coEvery { productDataSource.getProducts() } returns flow {
            emit(Result.Success(productList))
        }

        val result = repository.getProducts().toList()

        assertEquals(1, result.size)
        assert(result[0] is Result.Success)
        assertEquals(productList, (result[0] as Result.Success).data)
    }

    @Test
    fun testGetProductsFailure() = runTest {
        val errorMessage = "Network error"
        val exception = ErrorResponse(message =  errorMessage, errors = listOf(), isSuccessful = false, title = "Oops!")

        coEvery { productDataSource.getProducts() } returns flow {
            emit(Result.Error(errorResponse = exception))
        }

        val result = repository.getProducts().toList()

        assertEquals(1, result.size)
        assert(result[0] is Result.Error)
        assertEquals(errorMessage, (result[0] as Result.Error).errorResponse?.message?:"")
    }
}