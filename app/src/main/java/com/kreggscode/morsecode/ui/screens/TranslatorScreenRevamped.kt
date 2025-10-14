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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun TranslatorScreenRevamped(
    viewModel: TranslatorViewModel,
    onThemeToggle: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    isDarkTheme: Boolean
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var translationMode by remember { mutableStateOf(true) } // true = Text to Morse, false = Morse to Text
    var showAIDialog by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDarkTheme) {
                        listOf(
                            Color(0xFF0A0E27),
                            Color(0xFF1A1F3A),
                            Color(0xFF0F1629)
                        )
                    } else {
                        listOf(
                            Color(0xFFE3F2FD),
                            Color(0xFFBBDEFB),
                            Color(0xFFE1F5FE)
                        )
                    }
                )
            )
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 120.dp),
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
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Morse Translator",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color(0xFF1565C0)
                        )
                        Text(
                            text = if (translationMode) "Text → Morse" else "Morse → Text",
                            fontSize = 13.sp,
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.7f) else Color(0xFF1976D2).copy(alpha = 0.8f)
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlassIconButton(
                            icon = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            onClick = onThemeToggle,
                            tint = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF1976D2),
                            isDarkTheme = isDarkTheme
                        )
                        
                        GlassIconButton(
                            icon = Icons.Default.Settings,
                            onClick = onNavigateToSettings,
                            tint = if (isDarkTheme) GlassColors.BlueGlow else Color(0xFF1565C0),
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }
            
            // Translation Mode Toggle
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkTheme = isDarkTheme
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ModeButton(
                        text = "Text → Morse",
                        icon = Icons.Default.TextFields,
                        isSelected = translationMode,
                        onClick = { translationMode = true },
                        isDarkTheme = isDarkTheme
                    )
                    
                    ModeButton(
                        text = "Morse → Text",
                        icon = Icons.Default.GraphicEq,
                        isSelected = !translationMode,
                        onClick = { translationMode = false },
                        isDarkTheme = isDarkTheme
                    )
                }
            }
            
            // Input Card
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
                                        colors = if (isDarkTheme) {
                                            listOf(
                                                GlassColors.CyanGlow.copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        } else {
                                            listOf(
                                                Color(0xFF2196F3).copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        }
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (translationMode) Icons.Default.TextFields else Icons.Default.GraphicEq,
                                "Input",
                                tint = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF1976D2),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Text(
                            text = if (translationMode) "Text Input" else "Morse Input",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkTheme) Color.White else Color(0xFF1565C0)
                        )
                    }
                    
                    OutlinedTextField(
                        value = uiState.textInput,
                        onValueChange = { 
                            viewModel.onTextInputChanged(it)
                            if (!translationMode) {
                                viewModel.translateMorseToText(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { 
                            Text(
                                if (translationMode) "Type your message here..." else "Enter Morse code (. - / for space)",
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color(0xFF64B5F6)
                            )
                        },
                        minLines = 4,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3),
                            unfocusedBorderColor = if (isDarkTheme) Color.White.copy(alpha = 0.3f) else Color(0xFF90CAF9),
                            focusedTextColor = if (isDarkTheme) Color.White else Color(0xFF0D47A1),
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color(0xFF1565C0),
                            cursorColor = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3),
                            focusedContainerColor = if (isDarkTheme) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.7f),
                            unfocusedContainerColor = if (isDarkTheme) Color.White.copy(alpha = 0.02f) else Color.White.copy(alpha = 0.5f)
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
                            onClick = { 
                                if (translationMode) {
                                    viewModel.onTextInputChanged(uiState.textInput)
                                } else {
                                    viewModel.translateMorseToText(uiState.textInput)
                                }
                                viewModel.saveToHistory()
                            },
                            modifier = Modifier.weight(1f),
                            gradient = if (isDarkTheme) {
                                Brush.linearGradient(
                                    colors = listOf(GlassColors.CyanGlow, GlassColors.BlueGlow)
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF2196F3), Color(0xFF1976D2))
                                )
                            },
                            isDarkTheme = isDarkTheme
                        )
                        
                        GlassButton(
                            text = "Clear",
                            icon = Icons.Default.Clear,
                            onClick = { viewModel.clearAll() },
                            modifier = Modifier.weight(1f),
                            gradient = if (isDarkTheme) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.2f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF64B5F6).copy(alpha = 0.5f),
                                        Color(0xFF42A5F5).copy(alpha = 0.4f)
                                    )
                                )
                            },
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }
            
            // Output Card
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
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
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
                                                colors = if (isDarkTheme) {
                                                    listOf(
                                                        GlassColors.BlueGlow.copy(alpha = 0.3f),
                                                        Color.Transparent
                                                    )
                                                } else {
                                                    listOf(
                                                        Color(0xFF1976D2).copy(alpha = 0.3f),
                                                        Color.Transparent
                                                    )
                                                }
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        if (translationMode) Icons.Default.GraphicEq else Icons.Default.TextFields,
                                        "Output",
                                        tint = if (isDarkTheme) GlassColors.BlueGlow else Color(0xFF1565C0),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Text(
                                    text = if (translationMode) "Morse Code" else "Translated Text",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1565C0)
                                )
                            }
                            
                            // Copy Button
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Morse Code", uiState.morseOutput)
                                    clipboard.setPrimaryClip(clip)
                                    android.widget.Toast.makeText(context, "Copied to clipboard!", android.widget.Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color(0xFF2196F3).copy(alpha = 0.1f)
                                    )
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    "Copy",
                                    tint = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        
                        // Output display
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isDarkTheme) Color.Black.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.8f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isDarkTheme) GlassColors.BlueGlow.copy(alpha = 0.3f) else Color(0xFF2196F3).copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(20.dp)
                        ) {
                            Text(
                                text = uiState.morseOutput,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF0D47A1),
                                lineHeight = 28.sp
                            )
                        }
                        
                        // Visualizer (only for Morse output)
                        if (translationMode) {
                            MorseVisualizer(
                                morse = uiState.morseOutput
                            )
                        }
                        
                        // Control buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (translationMode) {
                                GlassButton(
                                    text = if (uiState.isPlaying) "Stop" else "Play",
                                    icon = if (uiState.isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    onClick = { 
                                        if (uiState.isPlaying) {
                                            viewModel.stopPlayback()
                                        } else {
                                            viewModel.playMorseAudio()
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    gradient = if (isDarkTheme) {
                                        Brush.linearGradient(
                                            colors = listOf(GlassColors.GreenGlow, GlassColors.CyanGlow)
                                        )
                                    } else {
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFF4CAF50), Color(0xFF2196F3))
                                        )
                                    },
                                    isDarkTheme = isDarkTheme
                                )
                                
                                GlassButton(
                                    text = if (uiState.isFlashing) "Stop" else "Flash",
                                    icon = if (uiState.isFlashing) Icons.Default.Stop else Icons.Default.FlashlightOn,
                                    onClick = { 
                                        if (uiState.isFlashing) {
                                            viewModel.stopFlashlight()
                                        } else {
                                            viewModel.flashMorse()
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    gradient = if (isDarkTheme) {
                                        Brush.linearGradient(
                                            colors = listOf(GlassColors.OrangeGlow, GlassColors.PinkGlow)
                                        )
                                    } else {
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFFFF9800), Color(0xFFFF5722))
                                        )
                                    },
                                    isDarkTheme = isDarkTheme
                                )
                            }
                            
                            GlassButton(
                                text = "Share",
                                icon = Icons.Default.Share,
                                onClick = { context.shareText(uiState.morseOutput) },
                                modifier = Modifier.weight(1f),
                                gradient = if (isDarkTheme) {
                                    Brush.linearGradient(
                                        colors = listOf(GlassColors.PurpleGlow, GlassColors.PinkGlow)
                                    )
                                } else {
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF9C27B0), Color(0xFFE91E63))
                                    )
                                },
                                isDarkTheme = isDarkTheme
                            )
                        }
                    }
                }
            }
            
            // AI Analysis Button
            AnimatedVisibility(
                visible = uiState.morseOutput.isNotEmpty() || uiState.textInput.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                GlassButton(
                    text = "Analyze with AI",
                    icon = Icons.Default.Psychology,
                    onClick = { showAIDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    gradient = if (isDarkTheme) {
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF6B9D),
                                Color(0xFFC239B3),
                                Color(0xFF6B5FFF)
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE91E63),
                                Color(0xFF9C27B0),
                                Color(0xFF673AB7)
                            )
                        )
                    },
                    isDarkTheme = isDarkTheme
                )
            }
        }
        
        // AI Analysis Dialog
        if (showAIDialog) {
            AIAnalysisDialog(
                morseCode = uiState.morseOutput,
                text = uiState.textInput,
                isDarkTheme = isDarkTheme,
                onDismiss = { showAIDialog = false }
            )
        }
    }
}

@Composable
fun ModeButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDarkTheme: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3)
            } else {
                Color.Transparent
            }
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (!isSelected) {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDarkTheme) Color.White.copy(alpha = 0.3f) else Color(0xFF90CAF9)
            )
        } else null
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) {
                    if (isDarkTheme) Color.Black else Color.White
                } else {
                    if (isDarkTheme) Color.White else Color(0xFF1976D2)
                },
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                color = if (isSelected) {
                    if (isDarkTheme) Color.Black else Color.White
                } else {
                    if (isDarkTheme) Color.White else Color(0xFF1976D2)
                },
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun GlassIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.White,
    isDarkTheme: Boolean
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = if (isDarkTheme) {
                        listOf(
                            Color.White.copy(alpha = 0.15f),
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
                width = 1.dp,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.3f) else Color(0xFF90CAF9),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun GlassButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush,
    isDarkTheme: Boolean
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
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
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.6f),
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
