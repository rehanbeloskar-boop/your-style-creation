package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ProductCategory
import com.example.ui.MainViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CartSheet
import com.example.ui.components.LoginModal
import com.example.ui.components.NavTab
import com.example.ui.components.NotificationSheet
import com.example.ui.components.ProductDetailSheet
import com.example.ui.components.TopHeader
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.VioletPrimary
import com.example.ui.theme.YourStyleTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YourStyleTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    var selectedTab by remember { mutableStateOf(NavTab.HOME) }

    // Observe ViewModel States
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()

    val cartCount by viewModel.cartTotalCount.collectAsStateWithLifecycle()
    val cartTotalPrice by viewModel.cartTotalPrice.collectAsStateWithLifecycle()
    val cartDisplayItems by viewModel.cartDisplayItems.collectAsStateWithLifecycle()

    val wishlistProductIds by viewModel.wishlistProductIds.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val selectedDetailProduct by viewModel.selectedProductForDetail.collectAsStateWithLifecycle()
    val isCartSheetOpen by viewModel.isCartSheetOpen.collectAsStateWithLifecycle()
    val isNotificationSheetOpen by viewModel.isNotificationSheetOpen.collectAsStateWithLifecycle()
    val isLoginModalOpen by viewModel.isLoginModalOpen.collectAsStateWithLifecycle()
    val recentPlacedOrderId by viewModel.recentPlacedOrderId.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // Filter wishlisted products list for Wishlist screen
    val wishlistedProducts = remember(wishlistProductIds, viewModel.repository.sampleProducts) {
        viewModel.repository.sampleProducts.filter { wishlistProductIds.contains(it.id) }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        topBar = {
            TopHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                cartItemCount = cartCount,
                notificationCount = viewModel.notifications.size,
                onCartClick = { viewModel.openCartSheet() },
                onNotificationClick = { viewModel.openNotificationSheet() },
                isLoggedIn = userProfile != null,
                onLoginClick = { viewModel.openLoginModal() }
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                NavTab.HOME -> {
                    HomeScreen(
                        products = filteredProducts,
                        selectedCategory = selectedCategory,
                        selectedSort = selectedSort,
                        wishlistProductIds = wishlistProductIds,
                        onCategorySelected = { viewModel.setSelectedCategory(it) },
                        onSortSelected = { viewModel.setSelectedSort(it) },
                        onProductClick = { viewModel.openProductDetail(it) },
                        onWishlistToggle = { viewModel.toggleWishlist(it) },
                        isLoggedIn = userProfile != null,
                        userName = userProfile?.name,
                        onLoginClick = { viewModel.openLoginModal() },
                        isLoading = isLoading
                    )
                }

                NavTab.CATEGORIES -> {
                    CategoriesScreen(
                        onSelectCategory = { category ->
                            viewModel.setSelectedCategory(category)
                            selectedTab = NavTab.HOME
                        }
                    )
                }

                NavTab.WISHLIST -> {
                    WishlistScreen(
                        wishlistedProducts = wishlistedProducts,
                        onProductClick = { viewModel.openProductDetail(it) },
                        onWishlistToggle = { viewModel.toggleWishlist(it) }
                    )
                }

                NavTab.ORDERS -> {
                    OrdersScreen(orders = orders)
                }

                NavTab.PROFILE -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        onOpenLoginModal = { viewModel.openLoginModal() },
                        onLogout = { viewModel.logoutUser() }
                    )
                }
            }
        }
    }

    // Modal & Sheet Dialogs
    selectedDetailProduct?.let { product ->
        ProductDetailSheet(
            product = product,
            isWishlisted = wishlistProductIds.contains(product.id),
            onDismiss = { viewModel.closeProductDetail() },
            onAddToCart = { size, qty ->
                viewModel.addToCart(product, size, qty)
            },
            onToggleWishlist = { viewModel.toggleWishlist(product.id) }
        )
    }

    if (isCartSheetOpen) {
        CartSheet(
            cartItems = cartDisplayItems,
            totalPrice = cartTotalPrice,
            onDismiss = { viewModel.closeCartSheet() },
            onUpdateQuantity = { item, delta -> viewModel.updateCartQuantity(item, delta) },
            onRemoveItem = { cartItemId -> viewModel.removeFromCart(cartItemId) },
            onCheckout = { viewModel.checkoutCart() }
        )
    }

    if (isNotificationSheetOpen) {
        NotificationSheet(
            notifications = viewModel.notifications,
            onDismiss = { viewModel.closeNotificationSheet() }
        )
    }

    if (isLoginModalOpen) {
        LoginModal(
            onDismiss = { viewModel.closeLoginModal() },
            onLoginSuccess = { name, email ->
                viewModel.loginUser(name, email)
            }
        )
    }

    recentPlacedOrderId?.let { orderId ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissOrderSuccessDialog() },
            title = {
                Text(
                    text = "Order Placed Successfully! 🎉",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Your Order #$orderId has been placed. You can track express delivery status anytime in your Orders tab.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissOrderSuccessDialog()
                        selectedTab = NavTab.ORDERS
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                ) {
                    Text(text = "VIEW MY ORDERS", color = Color.White)
                }
            }
        )
    }
}
