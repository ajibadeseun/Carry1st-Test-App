package com.carry1st_shop

import com.carry1st_shop.events.ProductEvents
import com.carry1st_shop.models.Product
import com.carry1st_shop.usecases.GetProductsUseCase
import com.carry1st_shop.utils.ErrorResponse
import com.carry1st_shop.utils.Result
import com.carry1st_shop.viewmodels.ProductViewModel
import com.carry1st_shop.viewmodels.ProductsResultState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductViewModelTest {
//        @get:Rule
//    val rule: TestRule = InstantTaskExecutorRule()
//    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @MockK
    private lateinit var viewModel: ProductViewModel
    @MockK
    private lateinit var getProductsUseCase: GetProductsUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = ProductViewModel(getProductsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetProductsSuccess() = runTest {
        val productList = listOf(
            Product(
                11,
                "10 Lives",
                "10 Lives product bundle.",
                1.0,
                "USD",
                "$",
                10,
                "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/74e517a3-0615-4019-bb08-cc697cf4e747.png",
                "ACTIVE"
            )
        )

        coEvery { getProductsUseCase.invoke() } returns flowOf(Result.Success(productList))

        viewModel.handleEvent(ProductEvents.FetchProducts)

        coVerify { getProductsUseCase.invoke() }
        assertEquals(productList, viewModel.uiState.value.products)
        assertEquals(
            ProductsResultState.Success("Products retrieved successfully"),
            viewModel.productResultState.value
        )
    }


    @Test
    fun testGetProductsFailure() = runTest {
        val errorMessage = "Network error"
        val exception = ErrorResponse(
            message = errorMessage,
            errors = listOf(),
            isSuccessful = false,
            title = "Oops!"
        )
        coEvery { getProductsUseCase.invoke() } returns flowOf(Result.Error(exception))
        viewModel.handleEvent(ProductEvents.FetchProducts)
        coVerify { getProductsUseCase.invoke() }



        val productResultState = viewModel.productResultState.value
        assert(productResultState is ProductsResultState.Failure)
        assertEquals(errorMessage, (productResultState as ProductsResultState.Failure).errorMessage)
    }


    @Test
    fun testUpdateSelectedProduct() {
        val product = Product(
            11,
            "10 Lives",
            "10 Lives product bundle.",
            1.0,
            "USD",
            "$",
            10,
            "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/74e517a3-0615-4019-bb08-cc697cf4e747.png",
            "ACTIVE"
        )

        viewModel.handleEvent(ProductEvents.UpdateSelectedProduct(product))

        assertEquals(product, viewModel.uiState.value.selectedProduct)
    }
}