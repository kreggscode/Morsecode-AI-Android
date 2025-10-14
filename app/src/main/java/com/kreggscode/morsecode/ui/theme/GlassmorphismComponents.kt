package com.kreggscode.morsecode.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Glassmorphism card with blur effect and transparency
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.linearGradient(
                    colors = if (isDarkTheme) {
                        listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    } else {
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.7f)
                        )
                    }
                )
            )
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = if (isDarkTheme) {
                        listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    } else {
                        listOf(
                            Color.White.copy(alpha = 0.8f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    }
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}

/**
 * Glassmorphism surface with elevated blur effect
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius)),
        color = if (isDarkTheme) {
            Color(0xFF1A1F3A).copy(alpha = 0.6f)
        } else {
            Color.White.copy(alpha = 0.8f)
        },
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isDarkTheme) {
                            listOf(
                                Color.White.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        } else {
                            listOf(
                                Color.White.copy(alpha = 0.5f),
                                Color.White.copy(alpha = 0.3f)
                            )
                        }
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                )
        ) {
            content()
        }
    }
}

/**
 * Floating glassmorphism navigation bar
 */
@Composable
fun GlassNavigationBar(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(36.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (isDarkTheme) {
                        listOf(
                            Color(0xFF1E2337).copy(alpha = 0.7f),
                            Color(0xFF2A3150).copy(alpha = 0.7f),
                            Color(0xFF1E2337).copy(alpha = 0.7f)
                        )
                    } else {
                        listOf(
                            Color.White.copy(alpha = 0.85f),
                            Color(0xFFF5F7FA).copy(alpha = 0.85f),
                            Color.White.copy(alpha = 0.85f)
                        )
                    }
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = if (isDarkTheme) {
                        listOf(
                            Color(0xFF00E5FF).copy(alpha = 0.3f),
                            Color(0xFF2196F3).copy(alpha = 0.2f),
                            Color(0xFF00E5FF).copy(alpha = 0.3f)
                        )
                    } else {
                        listOf(
                            Color(0xFF2196F3).copy(alpha = 0.4f),
                            Color(0xFF00BCD4).copy(alpha = 0.3f),
                            Color(0xFF2196F3).copy(alpha = 0.4f)
                        )
                    }
                ),
                shape = RoundedCornerShape(36.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            content = content
        )
    }
}

/**
 * Accent colors for glassmorphism theme
 */
object GlassColors {
    val CyanGlow = Color(0xFF00E5FF)
    val BlueGlow = Color(0xFF2196F3)
    val PurpleGlow = Color(0xFF9C27B0)
    val PinkGlow = Color(0xFFE91E63)
    val GreenGlow = Color(0xFF00E676)
    val OrangeGlow = Color(0xFFFF6D00)
    
    val DarkBackground = Color(0xFF0A0E27)
    val DarkSurface = Color(0xFF1A1F3A)
    val LightBackground = Color(0xFFF5F7FA)
    val LightSurface = Color.White
}

/**
 * Gradient backgrounds for screens
 */
fun getScreenGradient(isDarkTheme: Boolean): Brush {
    return if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A0E27),
                Color(0xFF1A1F3A),
                Color(0xFF0F1629)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF5F7FA),
                Color(0xFFE8EAF6),
                Color(0xFFF0F2F5)
            )
        )
    }
}

/**
 * Neon glow effect for buttons
 */
fun getNeonGlow(color: Color): Brush {
    return Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.6f),
            color.copy(alpha = 0.3f),
            color.copy(alpha = 0.1f),
            Color.Transparent
        )
    )
}
