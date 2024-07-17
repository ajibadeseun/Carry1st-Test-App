//package com.carry1st_shop
//
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.carry1st_shop.MainActivity
//import com.carry1st_shop.models.Product
//import com.carry1st_shop.screens.HomeScreen
//import com.carry1st_shop.ui.theme.Carry1st_ShopTheme
//import com.carry1st_shop.usecases.GetProductsUseCase
//import com.carry1st_shop.viewmodels.ProductViewModel
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.flow.flowOf
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import com.carry1st_shop.utils.Result
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class ProductsScreenTest {
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<MainActivity>()
//
//    private lateinit var viewModel: ProductViewModel
//
//    @Before
//    fun setUp() {
//        hiltRule.inject()
//        val productList = listOf(
//            Product(
//                11,
//                "10 Lives",
//                "10 Lives product bundle.",
//                1.0,
//                "USD",
//                "$",
//                10,
//                "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/74e517a3-0615-4019-bb08-cc697cf4e747.png",
//                "ACTIVE"
//            )
//        )
//
//        val getProductsUseCase: GetProductsUseCase = mockk()
//        coEvery { getProductsUseCase.invoke() } returns flowOf(Result.Success(productList))
//
//        viewModel = ProductViewModel(getProductsUseCase)
//
//        composeTestRule.setContent {
//            Carry1st_ShopTheme {
//                HomeScreen(viewModel) {}
//            }
//        }
//    }
//
//    @Test
//    fun testProductsDisplayed() {
//        composeTestRule.onNodeWithText("10 Lives").assertExists()
//        composeTestRule.onNodeWithText("1 USD").assertExists()
//    }
//}