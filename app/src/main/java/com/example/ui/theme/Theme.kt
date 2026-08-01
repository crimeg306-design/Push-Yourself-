package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TacticalColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = BgPrimary,
    secondary = AccentGold,
    onSecondary = BgPrimary,
    tertiary = AccentOrange,
    background = BgPrimary,
    surface = BgCard,
    surfaceVariant = BgElevated,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = AccentRed,
    onError = TextPrimary
)

@Composable
fun PushYourselfTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TacticalColorScheme,
        typography = TacticalTypography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    PushYourselfTheme(content = content)
}

