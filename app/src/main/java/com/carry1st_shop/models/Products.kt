package com.carry1st_shop.models

data class Product(
    val id: Int?=0,
    val name: String?="",
    val description: String?="",
    val price: Double?=0.0,
    val currencyCode: String?="",
    val currencySymbol: String?="",
    val quantity: Int?=0,
    val imageLocation: String?="",
    val status: String?=""
)