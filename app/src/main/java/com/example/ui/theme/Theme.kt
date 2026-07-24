package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LimeLight,
    onPrimary = BrandDarkCharcoal,
    primaryContainer = LimePrimaryVariant,
    onPrimaryContainer = Color.White,
    secondary = LimePrimary,
    onSecondary = BrandDarkCharcoal,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    outline = LimeBorder
)

private val LightColorScheme = lightColorScheme(
    primary = LimePrimary,
    onPrimary = BrandDarkCharcoal,
    primaryContainer = LimeContainer,
    onPrimaryContainer = OnLimeContainer,
    secondary = BrandDarkCharcoal,
    onSecondary = Color.White,
    background = LightBackground,
    surface = LightSurface,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    outline = LimePrimary
)

@Composable
fun YourStyleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve violet brand theme across devices
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
