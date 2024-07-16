package com.carry1st_shop.apiInterface

import com.carry1st_shop.models.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductsApiService {
    @GET("carry1stdeveloper/mock-product-api/productBundles")
    suspend fun getProducts(): Response<List<Product>>
}