package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.morsecode.ui.components.MorseVisualizer
import com.kreggscode.morsecode.ui.theme.GlassCard
import com.kreggscode.morsecode.ui.theme.GlassColors
import com.kreggscode.morsecode.util.shareText
import com.kreggscode.morsecode.viewmodel.TranslatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreenGlass(
    viewModel: TranslatorViewModel,
    onThemeToggle: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isDarkTheme = true // Always use dark theme for glassmorphism
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header with glassmorphism
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkTheme = isDarkTheme
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Morse Translator",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Translate text to Morse code",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GlassIconButton(
                            icon = Icons.Default.LightMode,
                            onClick = onThemeToggle,
                            tint = GlassColors.CyanGlow
                        )
                        
                        GlassIconButton(
                            icon = Icons.Default.Settings,
                            onClick = onNavigateToSettings,
                            tint = GlassColors.BlueGlow
                        )
                    }
                }
            }
            
            // Text Input Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkTheme = isDarkTheme
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            GlassColors.CyanGlow.copy(alpha = 0.3f),
                                            Color.Transparent
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.TextFields,
                                "Text Input",
                                tint = GlassColors.CyanGlow,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Text(
                            text = "Text Input",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                    
                    OutlinedTextField(
                        value = uiState.textInput,
                        onValueChange = { viewModel.onTextInputChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { 
                            Text(
                                "Type your message here...",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        },
                        minLines = 4,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GlassColors.CyanGlow,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = GlassColors.CyanGlow,
                            focusedContainerColor = Color.White.copy(alpha = 0.05f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.02f)
                        )
                    )
                    
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GlassButton(
                            text = "Translate",
                            icon = Icons.Default.Translate,
                            onClick = { viewModel.onTextInputChanged(uiState.textInput) },
                            modifier = Modifier.weight(1f),
                            gradient = Brush.linearGradient(
                                colors = listOf(GlassColors.CyanGlow, GlassColors.BlueGlow)
                            )
                        )
                        
                        GlassButton(
                            text = "Clear",
                            icon = Icons.Default.Clear,
                            onClick = { viewModel.clearAll() },
                            modifier = Modifier.weight(1f),
                            gradient = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.2f),
                                    Color.White.copy(alpha = 0.1f)
                                )
                            )
                        )
                    }
                }
            }
            
            // Morse Output Card
            AnimatedVisibility(
                visible = uiState.morseOutput.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    isDarkTheme = isDarkTheme
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                GlassColors.BlueGlow.copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.GraphicEq,
                                    "Morse Output",
                                    tint = GlassColors.BlueGlow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            
                            Text(
                                text = "Morse Code",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                        
                        // Morse code display
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.3f))
                                .border(
                                    width = 1.dp,
                                    color = GlassColors.BlueGlow.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(20.dp)
                        ) {
                            Text(
                                text = uiState.morseOutput,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = GlassColors.CyanGlow,
                                lineHeight = 28.sp
                            )
                        }
                        
                        // Visualizer
                        MorseVisualizer(
                            morse = uiState.morseOutput
                        )
                        
                        // Control buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GlassButton(
                                text = "Play",
                                icon = Icons.Default.PlayArrow,
                                onClick = { viewModel.playMorseAudio() },
                                modifier = Modifier.weight(1f),
                                gradient = Brush.linearGradient(
                                    colors = listOf(GlassColors.GreenGlow, GlassColors.CyanGlow)
                                )
                            )
                            
                            GlassButton(
                                text = "Flash",
                                icon = Icons.Default.FlashlightOn,
                                onClick = { viewModel.flashMorse() },
                                modifier = Modifier.weight(1f),
                                gradient = Brush.linearGradient(
                                    colors = listOf(GlassColors.OrangeGlow, GlassColors.PinkGlow)
                                )
                            )
                            
                            GlassButton(
                                text = "Share",
                                icon = Icons.Default.Share,
                                onClick = { context.shareText(uiState.morseOutput) },
                                modifier = Modifier.weight(1f),
                                gradient = Brush.linearGradient(
                                    colors = listOf(GlassColors.PurpleGlow, GlassColors.PinkGlow)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlassIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.White
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun GlassButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush
) {
    Button(
        onClick = onClick,
        modifier = modifier
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
                .background(gradient, RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
