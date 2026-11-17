package com.example.eventhub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = EventPurple,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = EventPurpleLight,
    secondary = EventTeal,
    background = EventBackground
)

private val DarkColors = darkColorScheme(
    primary = EventPurpleLight,
    secondary = EventTeal
)

@Composable
fun EventHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colorScheme, content = content)
}
