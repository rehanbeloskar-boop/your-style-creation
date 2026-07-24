package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LimePrimary

/**
 * Custom Shimmer Effect Modifier tailored to Electric Lime & Charcoal theme.
 */
fun Modifier.shimmerLoadingAnimation(
    baseColor: Color = Color(0xFF27272A), // Dark Zinc/Charcoal
    highlightColor: Color = Color(0xFF84CC16).copy(alpha = 0.30f) // Electric Lime Glow
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "ElectricLimeShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerFloat"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                baseColor,
                highlightColor,
                baseColor
            ),
            start = Offset(translateAnim - 300f, translateAnim - 300f),
            end = Offset(translateAnim, translateAnim)
        )
    )
}

/**
 * Skeleton Loader Card for Product Grid Items in Electric Lime & Charcoal Theme.
 */
@Composable
fun ProductSkeletonCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_skeleton_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Skeleton Product Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .shimmerLoadingAnimation()
            ) {
                // Skeleton Wishlist Icon
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.40f))
                        .shimmerLoadingAnimation()
                )

                // Skeleton Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .width(54.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerLoadingAnimation(
                            baseColor = Color(0xFF3F3F46),
                            highlightColor = LimePrimary.copy(alpha = 0.40f)
                        )
                )
            }

            // Skeleton Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Brand Line
                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .shimmerLoadingAnimation()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Title Line
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .shimmerLoadingAnimation()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Price & Offer Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerLoadingAnimation()
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerLoadingAnimation()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerLoadingAnimation(
                                baseColor = Color(0xFF3F3F46),
                                highlightColor = LimePrimary.copy(alpha = 0.50f)
                            )
                    )
                }
            }
        }
    }
}

/**
 * Full Grid Skeleton Loader representing products fetching phase.
 */
@Composable
fun ProductGridSkeleton(
    itemCount: Int = 6,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .testTag("product_grid_skeleton")
    ) {
        // Grid items simulated in rows of 2
        for (i in 0 until itemCount step 2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ProductSkeletonCard()
                }
                if (i + 1 < itemCount) {
                    Box(modifier = Modifier.weight(1f)) {
                        ProductSkeletonCard()
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
