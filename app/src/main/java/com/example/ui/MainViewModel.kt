package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NotificationItem
import com.example.data.Product
import com.example.data.ProductCategory
import com.example.data.db.AppDatabase
import com.example.data.db.CartEntity
import com.example.data.db.OrderEntity
import com.example.data.db.UserProfileEntity
import com.example.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOption(val title: String) {
    POPULARITY("Popularity"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    RATING("Customer Rating"),
    NEWEST("New Arrivals")
}

data class CartDisplayItem(
    val cartEntity: CartEntity,
    val product: Product
) {
    val itemTotalPrice: Double
        get() = product.price * cartEntity.quantity
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = ShopRepository(db.appDao())

    // Search & Filter State
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    private val _selectedSort = MutableStateFlow(SortOption.POPULARITY)
    val selectedSort: StateFlow<SortOption> = _selectedSort.asStateFlow()

    init {
        simulateProductFetch(800L)
    }

    private fun simulateProductFetch(delayMs: Long = 800L) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(delayMs)
            _isLoading.value = false
        }
    }

    fun refreshProducts() {
        simulateProductFetch(1000L)
    }

    // Navigation & Sheet States
    private val _selectedProductForDetail = MutableStateFlow<Product?>(null)
    val selectedProductForDetail: StateFlow<Product?> = _selectedProductForDetail.asStateFlow()

    private val _isCartSheetOpen = MutableStateFlow(false)
    val isCartSheetOpen: StateFlow<Boolean> = _isCartSheetOpen.asStateFlow()

    private val _isNotificationSheetOpen = MutableStateFlow(false)
    val isNotificationSheetOpen: StateFlow<Boolean> = _isNotificationSheetOpen.asStateFlow()

    private val _isLoginModalOpen = MutableStateFlow(false)
    val isLoginModalOpen: StateFlow<Boolean> = _isLoginModalOpen.asStateFlow()

    private val _recentPlacedOrderId = MutableStateFlow<String?>(null)
    val recentPlacedOrderId: StateFlow<String?> = _recentPlacedOrderId.asStateFlow()

    // Database Reactive Flows
    val cartEntities: StateFlow<List<CartEntity>> = repository.cartFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistEntities = repository.wishlistFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistProductIds: StateFlow<List<String>> = repository.wishlistFlow
        .map { list -> list.map { it.productId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.ordersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val notifications: List<NotificationItem> = repository.sampleNotifications

    // Computed Cart Display Items
    val cartDisplayItems: StateFlow<List<CartDisplayItem>> = combine(cartEntities) { entitiesArray ->
        val entities = entitiesArray[0]
        entities.mapNotNull { entity ->
            val product = repository.sampleProducts.find { it.id == entity.productId }
            product?.let { CartDisplayItem(entity, it) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotalCount: StateFlow<Int> = combine(cartEntities) { entitiesArray ->
        entitiesArray[0].sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartTotalPrice: StateFlow<Double> = combine(cartDisplayItems) { displayItemsArray ->
        displayItemsArray[0].sumOf { it.itemTotalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Filtered & Sorted Products List
    val filteredProducts: StateFlow<List<Product>> = combine(
        searchQuery,
        selectedCategory,
        selectedSort
    ) { query, category, sort ->
        var list = repository.sampleProducts

        // Category Filter (Strictly Men's Apparel & Footwear)
        if (category != ProductCategory.ALL) {
            list = list.filter { it.category == category }
        }

        // Search Query Filter
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                        it.brand.lowercase().contains(q) ||
                        it.category.displayName.lowercase().contains(q) ||
                        it.description.lowercase().contains(q)
            }
        }

        // Sorting
        when (sort) {
            SortOption.POPULARITY -> list.sortedByDescending { it.reviewCount }
            SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
            SortOption.RATING -> list.sortedByDescending { it.rating }
            SortOption.NEWEST -> list.sortedByDescending { it.isNewArrival }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.sampleProducts)

    // User Actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: ProductCategory) {
        if (_selectedCategory.value != category) {
            _selectedCategory.value = category
            simulateProductFetch(350L)
        }
    }

    fun setSelectedSort(sort: SortOption) {
        _selectedSort.value = sort
    }

    fun openProductDetail(product: Product) {
        _selectedProductForDetail.value = product
    }

    fun closeProductDetail() {
        _selectedProductForDetail.value = null
    }

    fun openCartSheet() {
        _isCartSheetOpen.value = true
    }

    fun closeCartSheet() {
        _isCartSheetOpen.value = false
    }

    fun openNotificationSheet() {
        _isNotificationSheetOpen.value = true
    }

    fun closeNotificationSheet() {
        _isNotificationSheetOpen.value = false
    }

    fun openLoginModal() {
        _isLoginModalOpen.value = true
    }

    fun closeLoginModal() {
        _isLoginModalOpen.value = false
    }

    fun addToCart(product: Product, size: String, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(product.id, size, quantity)
        }
    }

    fun updateCartQuantity(cartDisplayItem: CartDisplayItem, delta: Int) {
        viewModelScope.launch {
            val newQty = cartDisplayItem.cartEntity.quantity + delta
            repository.updateCartQuantity(
                cartItemId = cartDisplayItem.cartEntity.cartItemId,
                productId = cartDisplayItem.product.id,
                selectedSize = cartDisplayItem.cartEntity.selectedSize,
                newQty = newQty
            )
        }
    }

    fun removeFromCart(cartItemId: String) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
        }
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            repository.toggleWishlist(productId, wishlistProductIds.value)
        }
    }

    fun checkoutCart() {
        val currentProfile = userProfile.value
        if (currentProfile?.isLoggedIn != true) {
            // Prompt login
            _isLoginModalOpen.value = true
            return
        }

        viewModelScope.launch {
            val address = currentProfile.address.ifEmpty { "124 Market Street, New York, NY" }
            val orderId = repository.placeOrder(
                cartEntities = cartEntities.value,
                totalPrice = cartTotalPrice.value,
                deliveryAddress = address
            )
            _recentPlacedOrderId.value = orderId
            _isCartSheetOpen.value = false
        }
    }

    fun dismissOrderSuccessDialog() {
        _recentPlacedOrderId.value = null
    }

    fun loginUser(name: String, email: String) {
        viewModelScope.launch {
            repository.saveUserProfile(
                UserProfileEntity(
                    id = 1,
                    name = name.ifEmpty { "Alex Rivera" },
                    email = email.ifEmpty { "alex.rivera@style.com" },
                    phone = "+1 (555) 019-2834",
                    isLoggedIn = true
                )
            )
            _isLoginModalOpen.value = false
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.saveUserProfile(
                UserProfileEntity(id = 1, isLoggedIn = false)
            )
        }
    }
}
