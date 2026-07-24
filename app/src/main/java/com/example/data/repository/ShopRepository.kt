package com.example.data.repository

import com.example.R
import com.example.data.NotificationItem
import com.example.data.Product
import com.example.data.ProductCategory
import com.example.data.db.AppDao
import com.example.data.db.CartEntity
import com.example.data.db.OrderEntity
import com.example.data.db.UserProfileEntity
import com.example.data.db.WishlistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShopRepository(private val dao: AppDao) {

    val sampleProducts: List<Product> = listOf(
        // T-Shirts
        Product(
            id = "p1",
            title = "Solid Cotton Crew Neck Oversized Tee",
            brand = "Roadster",
            category = ProductCategory.T_SHIRTS,
            price = 24.99,
            originalPrice = 49.99,
            rating = 4.5f,
            reviewCount = 1240,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("S", "M", "L", "XL", "XXL"),
            description = "100% Premium combed cotton round neck t-shirt. Ultra soft breathable fabric designed for relaxed urban daily wear.",
            isTrending = true
        ),
        Product(
            id = "p2",
            title = "Rapid-Dry Performance Printed Active Tee",
            brand = "HRX by Hrithik Roshan",
            category = ProductCategory.T_SHIRTS,
            price = 29.99,
            originalPrice = 54.99,
            rating = 4.7f,
            reviewCount = 890,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("S", "M", "L", "XL"),
            description = "Engineered with anti-microbial finish and sweat-wicking technology for intense gym sessions and active lifestyle.",
            isNewArrival = true
        ),
        Product(
            id = "p3",
            title = "Club Fleece Graphic Minimalist T-Shirt",
            brand = "Nike",
            category = ProductCategory.T_SHIRTS,
            price = 39.99,
            originalPrice = 65.00,
            rating = 4.8f,
            reviewCount = 2100,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("M", "L", "XL"),
            description = "Classic fit graphic top crafted from soft mid-weight cotton with signature Nike swoosh heritage branding."
        ),

        // Casual Shirts
        Product(
            id = "p4",
            title = "Slim Fit Tartan Checkered Casual Shirt",
            brand = "Highlander",
            category = ProductCategory.CASUAL_SHIRTS,
            price = 34.99,
            originalPrice = 69.99,
            rating = 4.4f,
            reviewCount = 670,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("S", "M", "L", "XL"),
            description = "Versatile slim fit button-down shirt styled in classic plaid pattern. Pre-washed for maximum softness and vintage feel.",
            isTrending = true
        ),
        Product(
            id = "p5",
            title = "Pure Linen Mandarin Collar Casual Shirt",
            brand = "Dennis Lingo",
            category = ProductCategory.CASUAL_SHIRTS,
            price = 42.50,
            originalPrice = 75.00,
            rating = 4.6f,
            reviewCount = 450,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("M", "L", "XL", "XXL"),
            description = "Lightweight breathable pure linen shirt with modern band collar and tailored full sleeves.",
            isNewArrival = true
        ),
        Product(
            id = "p6",
            title = "Western Trucker Indigo Denim Shirt",
            brand = "Levi's",
            category = ProductCategory.CASUAL_SHIRTS,
            price = 59.99,
            originalPrice = 99.99,
            rating = 4.9f,
            reviewCount = 3120,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("S", "M", "L", "XL"),
            description = "Iconic heavy-gauge denim shirt featuring double snap-closure chest pockets and point collar."
        ),

        // Formal Shirts
        Product(
            id = "p7",
            title = "Non-Iron Fine Cotton Formal Dress Shirt",
            brand = "Louis Philippe",
            category = ProductCategory.FORMAL_SHIRTS,
            price = 49.99,
            originalPrice = 89.99,
            rating = 4.7f,
            reviewCount = 1530,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("S", "M", "L", "XL", "XXL"),
            description = "Precision tailored 100% Egyptian cotton dress shirt treated with wrinkle-free technology for sharp boardroom appearance."
        ),
        Product(
            id = "p8",
            title = "Crisp Micro-Check Tailored Fit Formal Shirt",
            brand = "Raymond",
            category = ProductCategory.FORMAL_SHIRTS,
            price = 54.99,
            originalPrice = 95.00,
            rating = 4.6f,
            reviewCount = 980,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("M", "L", "XL"),
            description = "Sophisticated subtle check weave formal shirt crafted from breathable long-staple cotton yarn."
        ),

        // Jackets & Blazers
        Product(
            id = "p9",
            title = "Quilted Lightweight Winter Bomber Jacket",
            brand = "Jack & Jones",
            category = ProductCategory.JACKETS_COATS,
            price = 79.99,
            originalPrice = 149.99,
            rating = 4.8f,
            reviewCount = 840,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("S", "M", "L", "XL"),
            description = "Wind-resistant insulated puffer jacket featuring ribbed cuffs, internal thermal fleece lining, and zip pockets.",
            isTrending = true
        ),
        Product(
            id = "p10",
            title = "Classic Faux Leather Biker Jacket",
            brand = "Roadster",
            category = ProductCategory.JACKETS_COATS,
            price = 89.99,
            originalPrice = 169.99,
            rating = 4.5f,
            reviewCount = 610,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("M", "L", "XL"),
            description = "Heavyweight matte finish synthetic leather biker jacket with asymmetric metal zipper detailing and shoulder epaulettes."
        ),

        // Jeans & Trousers
        Product(
            id = "p11",
            title = "511 Slim Fit Stretch Dark Wash Jeans",
            brand = "Levi's",
            category = ProductCategory.JEANS_TROUSERS,
            price = 64.99,
            originalPrice = 109.99,
            rating = 4.8f,
            reviewCount = 4200,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("30", "32", "34", "36"),
            description = "A modern slim with room to move. Crafted with elastane stretch denim for all-day flexibility and shape retention."
        ),
        Product(
            id = "p12",
            title = "Tactical Utility Cargo Joggers with Strap Details",
            brand = "Puma",
            category = ProductCategory.JEANS_TROUSERS,
            price = 45.99,
            originalPrice = 79.99,
            rating = 4.6f,
            reviewCount = 730,
            imageRes = R.drawable.img_hero_mens_collection_1784889455317,
            availableSizes = listOf("S", "M", "L", "XL"),
            description = "Urban streetwear tapered cargo pants with adjustable elastic ankle cuffs and multi-pocket utility layout."
        ),

        // Footwear - Sneakers
        Product(
            id = "p13",
            title = "Air Force 1 '07 Heritage Triple White Sneakers",
            brand = "Nike",
            category = ProductCategory.SNEAKERS,
            price = 115.00,
            originalPrice = 145.00,
            rating = 4.9f,
            reviewCount = 5600,
            imageRes = R.drawable.img_mens_footwear_collection_1784889469062,
            availableSizes = listOf("UK 7", "UK 8", "UK 9", "UK 10", "UK 11"),
            description = "The radiance lives on in the Nike Air Force 1 '07. Crisp leather, bold accents, and stitched overlays for legendary comfort.",
            isTrending = true
        ),
        Product(
            id = "p14",
            title = "Smash v2 Clean Leather Low-Top Sneakers",
            brand = "Puma",
            category = ProductCategory.SNEAKERS,
            price = 54.99,
            originalPrice = 89.99,
            rating = 4.6f,
            reviewCount = 1890,
            imageRes = R.drawable.img_mens_footwear_collection_1784889469062,
            availableSizes = listOf("UK 7", "UK 8", "UK 9", "UK 10"),
            description = "Tennis-inspired classic profile updated with a soft leather upper and durable non-marking rubber outsole."
        ),
        Product(
            id = "p15",
            title = "Forum Low Retro Basketball Style Sneakers",
            brand = "Adidas Originals",
            category = ProductCategory.SNEAKERS,
            price = 99.99,
            originalPrice = 130.00,
            rating = 4.8f,
            reviewCount = 1120,
            imageRes = R.drawable.img_mens_footwear_collection_1784889469062,
            availableSizes = listOf("UK 8", "UK 9", "UK 10", "UK 11"),
            description = "An 80s icon reimagined with premium ankle strap detailing, padded collar, and vintage rubber cupsole construction.",
            isNewArrival = true
        ),

        // Footwear - Formal Shoes
        Product(
            id = "p16",
            title = "Genuine Calfskin Leather Brogue Oxfords",
            brand = "Red Tape",
            category = ProductCategory.FORMAL_SHOES,
            price = 69.99,
            originalPrice = 129.99,
            rating = 4.7f,
            reviewCount = 820,
            imageRes = R.drawable.img_mens_footwear_collection_1784889469062,
            availableSizes = listOf("UK 7", "UK 8", "UK 9", "UK 10"),
            description = "Hand-finished full grain leather lace-up oxford shoes with wingtip decorative perforations and cushioned footbed."
        ),
        Product(
            id = "p17",
            title = "Double Monk Strap Formal Slip-On Shoes",
            brand = "Bata",
            category = ProductCategory.FORMAL_SHOES,
            price = 59.99,
            originalPrice = 99.99,
            rating = 4.5f,
            reviewCount = 490,
            imageRes = R.drawable.img_mens_footwear_collection_1784889469062,
            availableSizes = listOf("UK 7", "UK 8", "UK 9", "UK 10", "UK 11"),
            description = "Sleek burnished toe monk strap shoes featuring dual brass buckles and shock-absorbing rubber outsole."
        ),

        // Footwear - Sandals & Slides
        Product(
            id = "p18",
            title = "Heavy-Duty Nubuck Leather Outdoor Sandals",
            brand = "Woodland",
            category = ProductCategory.SANDALS_SLIDES,
            price = 49.99,
            originalPrice = 85.00,
            rating = 4.8f,
            reviewCount = 2300,
            imageRes = R.drawable.img_mens_footwear_collection_1784889469062,
            availableSizes = listOf("UK 7", "UK 8", "UK 9", "UK 10"),
            description = "Rugged all-terrain outdoor sandals crafted with oil-treated genuine leather and deep traction rubber lug soles."
        ),
        Product(
            id = "p19",
            title = "Offcourt Revive Ultra Cushion Comfort Slides",
            brand = "Nike",
            category = ProductCategory.SANDALS_SLIDES,
            price = 34.99,
            originalPrice = 50.00,
            rating = 4.7f,
            reviewCount = 1450,
            imageRes = R.drawable.img_mens_footwear_collection_1784889469062,
            availableSizes = listOf("UK 7", "UK 8", "UK 9", "UK 10", "UK 11"),
            description = "Dual-layer foam construction with contoured footbed cradling the foot for effortless post-workout recovery comfort."
        )
    )

    val sampleNotifications: List<NotificationItem> = listOf(
        NotificationItem(
            id = "n1",
            title = "Order Shipped!",
            message = "Your Order #YS-9482 with Levi's Slim Jeans & Nike AF1 is out for delivery today.",
            timeAgo = "10 mins ago",
            isRead = false,
            iconType = "ORDER"
        ),
        NotificationItem(
            id = "n2",
            title = "Exclusive Men's Footwear Drop",
            message = "New Arrival: Adidas Originals Forum Low Retro Sneakers now back in stock with 25% OFF!",
            timeAgo = "2 hours ago",
            isRead = false,
            iconType = "OFFER"
        ),
        NotificationItem(
            id = "n3",
            title = "Price Drop Alert!",
            message = "An item in your wishlist (Woodland Leather Sandals) dropped in price by $35.",
            timeAgo = "Yesterday",
            isRead = true,
            iconType = "PRICE_DROP"
        )
    )

    // Flow getters from database
    val cartFlow: Flow<List<CartEntity>> = dao.getCartItems()
    val wishlistFlow: Flow<List<WishlistEntity>> = dao.getWishlistItems()
    val ordersFlow: Flow<List<OrderEntity>> = dao.getOrders()
    val userProfileFlow: Flow<UserProfileEntity?> = dao.getUserProfile()

    suspend fun addToCart(productId: String, selectedSize: String, quantity: Int = 1) {
        val cartItemId = "${productId}_$selectedSize"
        dao.insertOrUpdateCartItem(
            CartEntity(
                cartItemId = cartItemId,
                productId = productId,
                selectedSize = selectedSize,
                quantity = quantity
            )
        )
    }

    suspend fun removeFromCart(cartItemId: String) {
        dao.deleteCartItem(cartItemId)
    }

    suspend fun updateCartQuantity(cartItemId: String, productId: String, selectedSize: String, newQty: Int) {
        if (newQty <= 0) {
            dao.deleteCartItem(cartItemId)
        } else {
            dao.insertOrUpdateCartItem(
                CartEntity(
                    cartItemId = cartItemId,
                    productId = productId,
                    selectedSize = selectedSize,
                    quantity = newQty
                )
            )
        }
    }

    suspend fun toggleWishlist(productId: String, currentWishlistIds: List<String>) {
        if (currentWishlistIds.contains(productId)) {
            dao.removeFromWishlist(productId)
        } else {
            dao.addToWishlist(WishlistEntity(productId = productId))
        }
    }

    suspend fun placeOrder(
        cartEntities: List<CartEntity>,
        totalPrice: Double,
        deliveryAddress: String
    ): String {
        val orderId = "YS-${(1000..9999).random()}"
        val summary = cartEntities.map { item ->
            val p = sampleProducts.find { it.id == item.productId }
            "${p?.title ?: "Item"} (${item.selectedSize}) x${item.quantity}"
        }.joinToString(", ")

        val order = OrderEntity(
            orderId = orderId,
            dateString = "Jul 24, 2026",
            totalAmount = totalPrice,
            status = "Processing & Shipping",
            itemCount = cartEntities.sumOf { it.quantity },
            itemsSummary = summary,
            deliveryAddress = deliveryAddress
        )
        dao.insertOrder(order)
        dao.clearCart()
        return orderId
    }

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        dao.saveUserProfile(profile)
    }
}
