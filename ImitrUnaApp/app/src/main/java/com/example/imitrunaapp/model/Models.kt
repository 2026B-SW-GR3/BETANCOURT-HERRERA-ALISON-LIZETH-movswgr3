package com.example.imitrunaapp.model

data class MenuCategory(
    val id: Int,
    val title: String,
    val imageResId: Int? = null,
    val isRedBorder: Boolean = false // Some items have red borders in screenshots
)

data class Store(
    val id: Int,
    val name: String,
    val distanceAddress: String,
    val scheduleText: String,
    var isSelected: Boolean = false
)

data class CouponCategory(
    val id: Int,
    val title: String,
    val imageResId: Int? = null,
    val iconResId: Int? = null
)
