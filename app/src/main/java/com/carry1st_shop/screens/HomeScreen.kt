package com.carry1st_shop.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.carry1st_shop.events.ProductEvents
import com.carry1st_shop.models.Product
import com.carry1st_shop.ui.theme.Purple40
import com.carry1st_shop.ui.theme.PurpleGrey80
import com.carry1st_shop.utils.OnLifecycleEvent
import com.carry1st_shop.viewmodels.ProductViewModel
import com.carry1st_shop.viewmodels.ProductsResultState
import okhttp3.internal.wait

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ProductViewModel, onNavigate: () -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val productsResultState by viewModel.productResultState.collectAsStateWithLifecycle()

    OnLifecycleEvent { owner, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_RESUME -> { /* onrResume */
                if (uiState.products.isEmpty()) {
                    viewModel.handleEvent(ProductEvents.FetchProducts)
                }

            }

            else -> { /* other stuff */
            }
        }
    }

    Scaffold() {
        Column(modifier = Modifier.padding(top = 21.dp, start = 24.dp, end = 24.dp)) {
            Text(
                text = "List of products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))
            LazyColumn {
                items(uiState.products) { item ->
                    ProductListItem(item) {
                        viewModel.handleEvent(ProductEvents.UpdateSelectedProduct(item))
                        onNavigate()
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

    }
    when (productsResultState) {
        ProductsResultState.Default -> Unit
        is ProductsResultState.Failure -> {
            Toast.makeText(
                context,
                "Error: ${(productsResultState as ProductsResultState.Failure).errorMessage}",
                Toast.LENGTH_LONG
            ).show()
        }

        ProductsResultState.Loading -> {
            LoadingScreen()
        }

        is ProductsResultState.Success -> {
            viewModel.resetState()
        }
    }
}

@Composable
fun ProductListItem(item: Product, onItemClick: () -> Unit) {
    val context = LocalContext.current
    Card(shape = RoundedCornerShape(
        topStart = 10.dp, bottomStart = 10.dp,
        topEnd = 10.dp, bottomEnd = 10.dp
    ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ), modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }) {
        Row(modifier = Modifier.padding(top = 5.dp, start = 6.dp, end = 6.dp, bottom = 5.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .data(item.imageLocation)
                    .build(),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15.dp)),
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = item.name ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "${item.currencySymbol}${String.format("%.2f", item.price)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsScreen(productViewModel: ProductViewModel, onBack: () -> Unit) {
    val uiState by productViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler {
        onBack()
    }

    Scaffold {
        Column {
            Carry1stTitleBar(
                title = "Product details",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .testTag("user-details-title-bar")
                    .semantics {
                        contentDescription = "User Details Title Bar"
                    },
                onBackNav = onBack,
                backgroundColor = PurpleGrey80
            )
            Spacer(Modifier.height(12.dp))
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .data(uiState.selectedProduct.imageLocation)
                    .build(),
                contentScale = ContentScale.Fit,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            )
            Spacer(Modifier.height(12.dp))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Name: ${uiState.selectedProduct.name ?: ""}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Price: ${uiState.selectedProduct.currencySymbol}${
                        String.format(
                            "%.2f",
                            uiState.selectedProduct.price
                        )
                    }",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Description: ${uiState.selectedProduct.description}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )

                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = {

                        }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                            containerColor = Purple40,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Add to Cart",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    TextButton(
                        onClick = {

                        }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                            containerColor = Purple40,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Buy Now",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}