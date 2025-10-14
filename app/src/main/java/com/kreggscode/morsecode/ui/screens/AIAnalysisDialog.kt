package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kreggscode.morsecode.ui.theme.GlassColors
import kotlinx.coroutines.delay

@Composable
fun AIAnalysisDialog(
    morseCode: String,
    text: String,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit
) {
    var isAnalyzing by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        delay(2000) // Simulate AI processing
        analysis = generateAIAnalysis(morseCode, text)
        isAnalyzing = false
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isDarkTheme) {
                            listOf(
                                Color(0xFF1A1F3A),
                                Color(0xFF0F1629)
                            )
                        } else {
                            listOf(
                                Color.White,
                                Color(0xFFF5F7FA)
                            )
                        }
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = if (isDarkTheme) {
                            listOf(
                                Color(0xFFFF6B9D),
                                Color(0xFFC239B3),
                                Color(0xFF6B5FFF)
                            )
                        } else {
                            listOf(
                                Color(0xFFE91E63),
                                Color(0xFF9C27B0),
                                Color(0xFF673AB7)
                            )
                        }
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = if (isDarkTheme) {
                                            listOf(
                                                Color(0xFFFF6B9D).copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        } else {
                                            listOf(
                                                Color(0xFFE91E63).copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        }
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Psychology,
                                "AI",
                                tint = if (isDarkTheme) Color(0xFFFF6B9D) else Color(0xFFE91E63),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        Column {
                            Text(
                                text = "AI Analysis",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF1565C0)
                            )
                            Text(
                                text = "Powered by AI",
                                fontSize = 12.sp,
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) 
                                    else Color(0xFF1976D2).copy(alpha = 0.7f)
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDarkTheme) Color.White.copy(alpha = 0.1f) 
                                else Color(0xFFF5F5F5)
                            )
                    ) {
                        Icon(
                            Icons.Default.Close,
                            "Close",
                            tint = if (isDarkTheme) Color.White else Color(0xFF424242)
                        )
                    }
                }
                
                Divider(
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.2f) 
                        else Color(0xFFE0E0E0)
                )
                
                // Content
                if (isAnalyzing) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = if (isDarkTheme) Color(0xFFFF6B9D) else Color(0xFFE91E63),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Analyzing with AI...",
                            fontSize = 16.sp,
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.8f) 
                                else Color(0xFF616161)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Input Section
                        AnalysisSection(
                            title = "Input",
                            icon = Icons.Default.Input,
                            content = text,
                            isDarkTheme = isDarkTheme
                        )
                        
                        // Morse Code Section
                        AnalysisSection(
                            title = "Morse Code",
                            icon = Icons.Default.GraphicEq,
                            content = morseCode,
                            isDarkTheme = isDarkTheme
                        )
                        
                        // AI Analysis Section
                        AnalysisSection(
                            title = "AI Insights",
                            icon = Icons.Default.Lightbulb,
                            content = analysis,
                            isDarkTheme = isDarkTheme,
                            highlight = true
                        )
                    }
                }
                
                // Action Button
                if (!isAnalyzing) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = if (isDarkTheme) {
                                            listOf(
                                                Color(0xFFFF6B9D),
                                                Color(0xFFC239B3),
                                                Color(0xFF6B5FFF)
                                            )
                                        } else {
                                            listOf(
                                                Color(0xFFE91E63),
                                                Color(0xFF9C27B0),
                                                Color(0xFF673AB7)
                                            )
                                        }
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Got it!",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnalysisSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String,
    isDarkTheme: Boolean,
    highlight: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                title,
                tint = if (highlight) {
                    if (isDarkTheme) Color(0xFFFF6B9D) else Color(0xFFE91E63)
                } else {
                    if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3)
                },
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color(0xFF1565C0)
            )
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (highlight) {
                        if (isDarkTheme) Color(0xFFFF6B9D).copy(alpha = 0.1f) 
                        else Color(0xFFE91E63).copy(alpha = 0.1f)
                    } else {
                        if (isDarkTheme) Color.Black.copy(alpha = 0.3f) 
                        else Color(0xFFF5F5F5)
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (highlight) {
                        if (isDarkTheme) Color(0xFFFF6B9D).copy(alpha = 0.3f) 
                        else Color(0xFFE91E63).copy(alpha = 0.3f)
                    } else {
                        if (isDarkTheme) Color.White.copy(alpha = 0.2f) 
                        else Color(0xFFE0E0E0)
                    },
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = content,
                fontSize = 14.sp,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.9f) 
                    else Color(0xFF424242),
                lineHeight = 20.sp
            )
        }
    }
}

fun generateAIAnalysis(morseCode: String, text: String): String {
    val dotCount = morseCode.count { it == '.' }
    val dashCount = morseCode.count { it == '-' }
    val totalSymbols = dotCount + dashCount
    val wordCount = text.split(" ").size
    val charCount = text.length
    val avgSymbolsPerChar = if (charCount > 0) totalSymbols.toFloat() / charCount else 0f
    
    return buildString {
        appendLine("📊 Statistical Analysis:")
        appendLine("• Total characters: $charCount")
        appendLine("• Word count: $wordCount")
        appendLine("• Morse symbols: $totalSymbols (${dotCount} dots, ${dashCount} dashes)")
        appendLine("• Average symbols per character: ${"%.2f".format(avgSymbolsPerChar)}")
        appendLine()
        appendLine("⚡ Transmission Insights:")
        val estimatedTime = (dotCount + dashCount * 3 + charCount * 3 + wordCount * 7) / 20f
        appendLine("• Estimated transmission time: ${"%.1f".format(estimatedTime)}s at standard speed")
        appendLine("• Efficiency: ${if (avgSymbolsPerChar < 3) "High" else if (avgSymbolsPerChar < 4) "Medium" else "Low"}")
        appendLine()
        appendLine("💡 Recommendations:")
        if (text.any { it.isUpperCase() }) {
            appendLine("• Message contains uppercase letters - Morse code is case-insensitive")
        }
        if (wordCount > 10) {
            appendLine("• Long message - Consider breaking into smaller segments for clarity")
        }
        if (dashCount > dotCount * 2) {
            appendLine("• Dash-heavy pattern - May take longer to transmit")
        }
        appendLine("• Message is ready for transmission via audio, light, or radio")
    }
}
