package com.kreggscode.morsecode.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.kreggscode.morsecode.ui.theme.DashColor
import com.kreggscode.morsecode.ui.theme.DotColor
import com.kreggscode.morsecode.ui.theme.GlowColor

@Composable
fun MorseVisualizer(
    morse: String,
    modifier: Modifier = Modifier,
    isAnimating: Boolean = false
) {
    var animationProgress by remember { mutableStateOf(0f) }
    
    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) { value, _ ->
                animationProgress = value
            }
        }
    }
    
    Canvas(modifier = modifier.fillMaxWidth().height(80.dp)) {
        val symbols = morse.toList()
        val spacing = size.width / (symbols.size + 1)
        val centerY = size.height / 2
        
        symbols.forEachIndexed { index, symbol ->
            val x = spacing * (index + 1)
            
            when (symbol) {
                '.' -> drawDot(x, centerY, isAnimating, animationProgress)
                '-' -> drawDash(x, centerY, isAnimating, animationProgress)
                ' ', '/' -> {} // Skip spaces
            }
        }
    }
}

private fun DrawScope.drawDot(x: Float, y: Float, isAnimating: Boolean, progress: Float) {
    val radius = if (isAnimating) {
        12f + (kotlin.math.sin(progress * 2 * Math.PI).toFloat() * 4f)
    } else {
        12f
    }
    
    // Glow effect
    if (isAnimating) {
        drawCircle(
            color = GlowColor,
            radius = radius * 2,
            center = Offset(x, y)
        )
    }
    
    // Main dot
    drawCircle(
        color = DotColor,
        radius = radius,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawDash(x: Float, y: Float, isAnimating: Boolean, progress: Float) {
    val width = if (isAnimating) {
        40f + (kotlin.math.sin(progress * 2 * Math.PI).toFloat() * 8f)
    } else {
        40f
    }
    val height = 12f
    
    // Glow effect
    if (isAnimating) {
        drawRect(
            color = GlowColor,
            topLeft = Offset(x - width / 2 - 4, y - height - 4),
            size = androidx.compose.ui.geometry.Size(width + 8, height * 2 + 8)
        )
    }
    
    // Main dash
    drawRect(
        color = DashColor,
        topLeft = Offset(x - width / 2, y - height),
        size = androidx.compose.ui.geometry.Size(width, height * 2)
    )
}
