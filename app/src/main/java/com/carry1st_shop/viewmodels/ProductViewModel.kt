package com.carry1st_shop.viewmodels

import androidx.lifecycle.viewModelScope
import com.carry1st_shop.events.ProductEvents
import com.carry1st_shop.models.Product
import com.carry1st_shop.usecases.GetProductsUseCase
import com.carry1st_shop.utils.BaseViewModel
import com.carry1st_shop.utils.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.carry1st_shop.utils.Result
import kotlinx.coroutines.flow.update
import com.carry1st_shop.events.ProductEvents.*

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _productResultsState =
        MutableStateFlow<ProductsResultState>(ProductsResultState.Default)
    val productResultState: StateFlow<ProductsResultState> = _productResultsState.asStateFlow()

    init {
        getProducts()
    }

    fun handleEvent(event: ProductEvents) {
        when (event) {
            is FetchProducts -> {
                getProducts()
            }
            is UpdateSelectedProduct ->{
                updateSelectedProduct(event.product)
            }
            else -> {

            }
        }
    }

    private fun updateSelectedProduct(product: Product){
        _uiState.update {
            it.copy(selectedProduct = product)
        }
    }
    private fun getProducts() {
        viewModelScope.launch {
            _productResultsState.value = ProductsResultState.Loading
            val sus = getProductsUseCase.invoke().map { result ->
                when (result) {
                    is Result.Error -> {
                        val errorMessage = result.errorResponse?.message
                            ?: result.errorResponse?.errors?.firstOrNull()
                        ProductsResultState.Failure(
                            errorMessage ?: "An error occurred",
                            result.errorResponse?.title ?: "Oops!"
                        )
                    }

                    is Result.Success -> {
                        updateProducts(result.data)
                        ProductsResultState.Success(
                            data = "Products retrieved successfully"
                        )
                    }
                }
            }

            handleBaseRequest(_productResultsState, object : CoroutinesErrorHandler {
                override fun onError(message: String, throwable: Throwable?) {
                    _productResultsState.value = ProductsResultState.Failure(message, "Oops!")
                }
            }) { sus }
        }
    }

    private fun updateProducts(listOfProducts: List<Product>) {
        _uiState.update {
            it.copy(products = listOfProducts)
        }
    }
    fun resetState() {
        _productResultsState.value = ProductsResultState.Default
    }
}


data class ProductUiState(
    val products: List<Product> = listOf(),
    val selectedProduct: Product = Product()
)

sealed interface ProductsResultState {
    data class Success(val data: String) : ProductsResultState

    data class Failure(val errorMessage: String, val title: String) : ProductsResultState

    data object Loading : ProductsResultState

    data object Default : ProductsResultState

}