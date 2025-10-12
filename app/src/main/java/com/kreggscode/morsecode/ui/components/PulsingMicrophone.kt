package com.kreggscode.morsecode.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PulsingMicrophone(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var alpha by remember { mutableStateOf(0.3f) }
    
    LaunchedEffect(isListening) {
        if (isListening) {
            animate(
                initialValue = 1f,
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            ) { value, _ ->
                scale = value
                alpha = 0.3f + (value - 1f)
            }
        } else {
            scale = 1f
            alpha = 0.3f
        }
    }
    
    Box(
        modifier = modifier
            .size(120.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Outer pulsing circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                    shape = CircleShape
                )
        )
        
        // Inner circle
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = if (isListening) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Microphone",
                modifier = Modifier.size(40.dp),
                tint = if (isListening) MaterialTheme.colorScheme.onPrimary
                       else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
