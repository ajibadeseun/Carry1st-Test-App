package com.carry1st_shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carry1st_shop.navigation.LANDING_PAGE_ROUTE
import com.carry1st_shop.navigation.PRODUCT_DETAILS_PAGE_ROUTE
import com.carry1st_shop.screens.HomeScreen
import com.carry1st_shop.screens.ProductDetailsScreen
import com.carry1st_shop.ui.theme.Carry1st_ShopTheme
import com.carry1st_shop.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val productViewModel: ProductViewModel by viewModels()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Carry1st_ShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics(mergeDescendants = true) {
                            testTagsAsResourceId = true
                        },
                    color = MaterialTheme.colorScheme.background
                ) {
                    val animationDuration = 400
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = LANDING_PAGE_ROUTE,
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(animationDuration)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                targetOffset = { IntOffset(-300, 0) },
                                animationSpec = tween(animationDuration)
                            )
                        },
                        popEnterTransition = {
                            slideIn(
                                initialOffset = { IntOffset(-300, 0) },
                                animationSpec = tween(animationDuration)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(animationDuration)
                            )
                        },
                    ) {
                        composable(LANDING_PAGE_ROUTE) {
                            HomeScreen(viewModel = productViewModel, onNavigate = {
                                navController.navigate(
                                    PRODUCT_DETAILS_PAGE_ROUTE
                                )
                            })
                        }
                        composable(PRODUCT_DETAILS_PAGE_ROUTE) {
                            ProductDetailsScreen(
                                productViewModel = productViewModel,
                                onBack = navController::popBackStack
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Carry1st_ShopTheme {
        Greeting("Android")
    }
}