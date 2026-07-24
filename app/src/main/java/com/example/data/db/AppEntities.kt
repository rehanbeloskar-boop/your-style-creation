package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val cartItemId: String, // productId + "_" + size
    val productId: String,
    val selectedSize: String,
    val quantity: Int,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey val productId: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val dateString: String,
    val totalAmount: Double,
    val status: String,
    val itemCount: Int,
    val itemsSummary: String,
    val deliveryAddress: String
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoggedIn: Boolean = false,
    val address: String = "124 Market Street, Suite 400, New York, NY"
)
