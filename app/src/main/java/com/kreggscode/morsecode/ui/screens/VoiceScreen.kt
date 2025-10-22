package com.kreggscode.morsecode.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kreggscode.morsecode.ui.components.AnimatedButton
import com.kreggscode.morsecode.ui.components.MorseVisualizer
import com.kreggscode.morsecode.ui.components.PulsingMicrophone
import com.kreggscode.morsecode.viewmodel.VoiceViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun VoiceScreen(viewModel: VoiceViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val audioPermission = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)
    val context = LocalContext.current
    val formattedMorse = remember(uiState.morseCode) {
        if (uiState.morseCode.isBlank()) {
            ""
        } else {
            uiState.morseCode
                .trim()
                .split('/')
                .joinToString(separator = "   /   ") { word ->
                    word.trim()
                        .split(' ')
                        .filter { it.isNotBlank() }
                        .joinToString(separator = "   ")
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .padding(bottom = 100.dp), // Space for nav bar
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Voice to Morse",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = { viewModel.clearResults() }) {
                Icon(Icons.Default.Clear, "Clear")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Microphone Button
        PulsingMicrophone(
            isListening = uiState.isListening,
            onClick = {
                if (audioPermission.status.isGranted) {
                    if (uiState.isListening) {
                        viewModel.stopListening()
                    } else {
                        viewModel.startListening()
                    }
                } else {
                    audioPermission.launchPermissionRequest()
                }
            }
        )

        // Status Text
        Text(
            text = uiState.recognitionState,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        // Partial Text (while listening)
        AnimatedVisibility(visible = uiState.partialText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = uiState.partialText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // Recognized Text
        AnimatedVisibility(visible = uiState.recognizedText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recognized Text",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Recognized Text", uiState.recognizedText))
                                Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy recognized text",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = uiState.recognizedText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 0.25.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Morse Code Output
        AnimatedVisibility(visible = uiState.morseCode.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Morse Code",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Morse Code", uiState.morseCode))
                                Toast.makeText(context, "Morse code copied", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Morse code",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    MorseVisualizer(
                        morse = uiState.morseCode,
                        isAnimating = uiState.isPlaying || uiState.isFlashing
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = formattedMorse,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
            
            // Control Buttons
            AnimatedVisibility(visible = uiState.morseCode.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedButton(
                        onClick = { viewModel.playMorse() },
                        icon = Icons.Default.PlayArrow,
                        text = "Play Audio",
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isPlaying,
                        isAnimating = uiState.isPlaying
                    )
                    
                    AnimatedButton(
                        onClick = { viewModel.flashMorse() },
                        icon = Icons.Default.FlashOn,
                        text = "Flash",
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isFlashing,
                        isAnimating = uiState.isFlashing
                    )
                }
            }
            
            // Permission Info
            if (!audioPermission.status.isGranted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Microphone permission required for voice recognition",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
    }
}
