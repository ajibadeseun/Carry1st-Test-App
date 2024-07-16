package com.carry1st_shop.events

import com.carry1st_shop.models.Product

sealed class ProductEvents {
    object FetchProducts: ProductEvents()
    data class UpdateSelectedProduct(val product: Product): ProductEvents()
}