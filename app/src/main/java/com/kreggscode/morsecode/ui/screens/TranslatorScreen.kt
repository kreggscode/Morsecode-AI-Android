package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.morsecode.ui.components.MorseVisualizer
import com.kreggscode.morsecode.util.shareText
import com.kreggscode.morsecode.viewmodel.TranslatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel,
    onThemeToggle: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF0A0E27)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .padding(bottom = 120.dp), // Extra space for floating nav bar
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with theme toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Morse Translator",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                )
                
                IconButton(
                    onClick = onThemeToggle,
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .background(
                            if (isDarkTheme) Color(0xFF1E2337) else Color.White,
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        "Toggle Theme",
                        tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                    )
                }
            }
            
            // Text Input Box
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = if (isDarkTheme) Color(0xFF1E2337) else Color.White
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.TextFields,
                            "Text Input",
                            tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Text Input",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = uiState.textInput,
                        onValueChange = { viewModel.onTextInputChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { 
                            Text(
                                "Type your message here...",
                                color = if (isDarkTheme) Color(0xFF8E9AAF) else Color(0xFF9E9E9E)
                            )
                        },
                        minLines = 4,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                            unfocusedBorderColor = if (isDarkTheme) Color(0xFF3A4A5C) else Color(0xFFE0E0E0),
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black
                        )
                    )
                }
            }
            
            // Swap Icon
            AnimatedVisibility(
                visible = uiState.textInput.isNotEmpty() || uiState.morseInput.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.SwapVert,
                        "Bidirectional",
                        tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            // Morse Code Output/Input Box (Shows generated morse OR allows morse input)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = if (isDarkTheme) Color(0xFF1E2337) else Color.White
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.MoreHoriz,
                                "Morse Code",
                                tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Morse Code",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                            )
                        }
                        
                        // Copy button
                        if (uiState.morseOutput.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(uiState.morseOutput))
                                }
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    "Copy",
                                    tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Show generated morse code OR allow morse input
                    if (uiState.textInput.isEmpty()) {
                        // Allow morse input when no text
                        OutlinedTextField(
                            value = uiState.morseInput,
                            onValueChange = { viewModel.onMorseInputChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { 
                                Text(
                                    "Enter morse code (. - /)...",
                                    color = if (isDarkTheme) Color(0xFF8E9AAF) else Color(0xFF9E9E9E)
                                )
                            },
                            minLines = 4,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                                unfocusedBorderColor = if (isDarkTheme) Color(0xFF3A4A5C) else Color(0xFFE0E0E0),
                                focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                                unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black
                            )
                        )
                    } else {
                        // Show generated morse code (read-only)
                        Text(
                            text = uiState.morseOutput.ifEmpty { "Generating..." },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkTheme) Color.White else Color(0xFF1565C0),
                            letterSpacing = 2.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                    
                    // Show decoded text when morse is entered
                    if (uiState.morseInput.isNotEmpty() && uiState.textInput.isEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Decoded Text",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFF4CAF50) else Color(0xFF2E7D32)
                            )
                            
                            if (uiState.textOutput.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(uiState.textOutput))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        "Copy",
                                        tint = if (isDarkTheme) Color(0xFF4CAF50) else Color(0xFF2E7D32)
                                    )
                                }
                            }
                        }
                        
                        Text(
                            text = uiState.textOutput.ifEmpty { "Decoding..." },
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkTheme) Color.White else Color(0xFF1B5E20),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
            
            // Morse Visualizer & Controls (only when text is converted to morse)
            AnimatedVisibility(
                visible = uiState.morseOutput.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Morse Visualizer
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isDarkTheme) Color(0xFF1E2337) else Color.White
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            MorseVisualizer(
                                morse = uiState.morseOutput,
                                isAnimating = uiState.isPlaying || uiState.isFlashing
                            )
                        }
                    }
                    
                    // Play/Flash Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StunningButton(
                            onClick = { 
                                if (uiState.isPlaying) viewModel.stopMorseAudio()
                                else viewModel.playMorseAudio()
                            },
                            icon = if (uiState.isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            text = if (uiState.isPlaying) "Stop" else "Play",
                            modifier = Modifier.weight(1f),
                            isActive = uiState.isPlaying,
                            isDarkTheme = isDarkTheme
                        )
                        
                        StunningButton(
                            onClick = {
                                if (uiState.isFlashing) viewModel.stopFlashing()
                                else viewModel.flashMorse()
                            },
                            icon = if (uiState.isFlashing) Icons.Default.Stop else Icons.Default.FlashOn,
                            text = if (uiState.isFlashing) "Stop" else "Flash",
                            modifier = Modifier.weight(1f),
                            isActive = uiState.isFlashing,
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }
            
            // Speed Control with modern slider
            AnimatedVisibility(
                visible = uiState.morseOutput.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    color = if (isDarkTheme) Color(0xFF1E2337) else Color.White
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Playback Speed",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = String.format(java.util.Locale.US, "%.1fx", uiState.playbackSpeed),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = uiState.playbackSpeed,
                            onValueChange = { viewModel.setPlaybackSpeed(it) },
                            valueRange = 0.5f..2.0f,
                            steps = 5,
                            colors = SliderDefaults.colors(
                                thumbColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                                activeTrackColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                                inactiveTrackColor = if (isDarkTheme) Color(0xFF3A4A5C) else Color(0xFFE0E0E0)
                            )
                        )
                    }
                }
            }
            
            // Action Buttons - Save, Share, Clear
            AnimatedVisibility(
                visible = (uiState.textInput.isNotEmpty() && uiState.morseOutput.isNotEmpty()) || 
                         (uiState.morseInput.isNotEmpty() && uiState.textOutput.isNotEmpty()),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StunningButton(
                        onClick = { viewModel.saveToHistory() },
                        icon = Icons.Default.Save,
                        text = "Save",
                        modifier = Modifier.weight(1f),
                        enabled = true,
                        isDarkTheme = isDarkTheme
                    )
                    
                    StunningButton(
                        onClick = {
                            val textToShare = if (uiState.textInput.isNotEmpty()) {
                                "Text: ${uiState.textInput}\nMorse: ${uiState.morseOutput}"
                            } else {
                                "Morse: ${uiState.morseInput}\nText: ${uiState.textOutput}"
                            }
                            context.shareText(textToShare)
                        },
                        icon = Icons.Default.Share,
                        text = "Share",
                        modifier = Modifier.weight(1f),
                        enabled = true,
                        isDarkTheme = isDarkTheme
                    )
                    
                    StunningButton(
                        onClick = { viewModel.clearAll() },
                        icon = Icons.Default.Clear,
                        text = "Clear",
                        modifier = Modifier.weight(1f),
                        enabled = true,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}

@Composable
fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant)
    )
}

@Composable
fun StunningButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isActive: Boolean = false,
    isDarkTheme: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .scale(scale)
            .shadow(
                elevation = if (isActive) 16.dp else 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
            ),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
            contentColor = if (isDarkTheme) Color.Black else Color.White,
            disabledContainerColor = if (isDarkTheme) Color(0xFF3A4A5C) else Color(0xFFE0E0E0),
            disabledContentColor = if (isDarkTheme) Color(0xFF8E9AAF) else Color(0xFF9E9E9E)
        )
    ) {
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

