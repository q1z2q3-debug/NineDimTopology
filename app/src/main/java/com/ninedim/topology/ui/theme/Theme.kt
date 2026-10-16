package com.ninedim.topology.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Ink = Color(0xFF1C1914)
val Ochre = Color(0xFFA35832)
val Pine = Color(0xFF3C4A42)
val Paper = Color(0xFFF1E9DA)
val PaperDark = Color(0xFFE8DFC8)
val TextPrimary = Color(0xFF1C1914)
val TextSecondary = Color(0xFF5B544B)

private val LightColorScheme = lightColorScheme(
    primary = Ochre,
    secondary = Pine,
    tertiary = Ink,
    background = Paper,
    surface = Paper,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = PaperDark
)

@Composable
fun NineDimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) LightColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
