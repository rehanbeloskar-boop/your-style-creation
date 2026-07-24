package com.example.data

import androidx.annotation.DrawableRes

enum class ProductCategory(val displayName: String, val isFootwear: Boolean) {
    ALL("All Men's", false),
    T_SHIRTS("T-Shirts", false),
    CASUAL_SHIRTS("Casual Shirts", false),
    FORMAL_SHIRTS("Formal Shirts", false),
    JACKETS_COATS("Jackets & Blazers", false),
    JEANS_TROUSERS("Jeans & Trousers", false),
    SNEAKERS("Sneakers", true),
    FORMAL_SHOES("Formal Footwear", true),
    SANDALS_SLIDES("Sandals & Slides", true)
}

data class Product(
    val id: String,
    val title: String,
    val brand: String,
    val category: ProductCategory,
    val price: Double,
    val originalPrice: Double,
    val rating: Float,
    val reviewCount: Int,
    @DrawableRes val imageRes: Int? = null,
    val availableSizes: List<String>,
    val description: String,
    val isTrending: Boolean = false,
    val isNewArrival: Boolean = false
) {
    val discountPercent: Int
        get() = (((originalPrice - price) / originalPrice) * 100).toInt()
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val iconType: String = "OFFER" // "ORDER", "OFFER", "PRICE_DROP"
)
