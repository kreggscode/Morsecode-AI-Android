package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kreggscode.morsecode.data.local.entities.ChatMessageEntity
import com.kreggscode.morsecode.util.toTimeAgo
import com.kreggscode.morsecode.viewmodel.AiChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    viewModel: AiChatViewModel,
    onThemeToggle: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF0A0E27)
    
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Messages List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 80.dp, bottom = 200.dp), // Space for header and floating input + nav
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    WelcomeMessage(isDarkTheme)
                }
            }
            
            items(messages) { message ->
                ChatBubble(message, isDarkTheme)
            }
            
            if (uiState.isLoading) {
                item {
                    TypingIndicator(isDarkTheme)
                }
            }
        }
        
        // Header with theme toggle
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            color = if (isDarkTheme) Color(0xFF0A0E27).copy(alpha = 0.95f) else Color(0xFFF5F7FA).copy(alpha = 0.95f),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.SmartToy,
                        "AI Chat",
                        tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "AI Assistant",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    
                    IconButton(
                        onClick = { viewModel.clearHistory() },
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(12.dp))
                            .background(
                                if (isDarkTheme) Color(0xFF1E2337) else Color.White,
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "Clear History",
                            tint = if (isDarkTheme) Color(0xFFFF6B6B) else Color(0xFFE53935)
                        )
                    }
                }
            }
        }
        
        // Error Message
        if (uiState.errorMessage != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 200.dp),
                shape = RoundedCornerShape(16.dp),
                color = if (isDarkTheme) Color(0xFFFF6B6B).copy(alpha = 0.2f) else Color(0xFFFFEBEE)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = if (isDarkTheme) Color(0xFFFF6B6B) else Color(0xFFE53935)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.clearError() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                }
            }
        }
        
        // Floating Input Area - Above nav bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 108.dp) // Above floating nav bar
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(28.dp),
                    spotColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                ),
            shape = RoundedCornerShape(28.dp),
            color = if (isDarkTheme) Color(0xFF1E2337) else Color.White,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { 
                        Text(
                            "Ask about Morse code...",
                            color = if (isDarkTheme) Color(0xFF8E9AAF) else Color(0xFF9E9E9E)
                        )
                    },
                    maxLines = 4,
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                        unfocusedBorderColor = if (isDarkTheme) Color(0xFF3A4A5C) else Color(0xFFE0E0E0),
                        focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                        unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black
                    )
                )
                
                FloatingActionButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    containerColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                    contentColor = if (isDarkTheme) Color.Black else Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Send,
                        "Send",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeMessage(isDarkTheme: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = if (isDarkTheme) Color(0xFF1E2337) else Color(0xFFE3F2FD)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.SmartToy,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to AI Chat!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ask me anything about Morse code, its history, or how to use it!",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDarkTheme) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessageEntity, isDarkTheme: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isUser) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = if (isDarkTheme) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF2196F3).copy(alpha = 0.2f)
            ) {
                Icon(
                    Icons.Default.SmartToy,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                modifier = Modifier.shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = if (message.isUser) 20.dp else 4.dp,
                    bottomEnd = if (message.isUser) 4.dp else 20.dp
                ),
                color = if (message.isUser) {
                    if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                } else {
                    if (isDarkTheme) Color(0xFF1E2337) else Color.White
                }
            ) {
                Text(
                    text = message.message,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (message.isUser) {
                        if (isDarkTheme) Color.Black else Color.White
                    } else {
                        if (isDarkTheme) Color.White else Color.Black
                    }
                )
            }
            
            Text(
                text = message.timestamp.toTimeAgo(),
                style = MaterialTheme.typography.labelSmall,
                color = if (isDarkTheme) Color(0xFF8E9AAF) else Color(0xFF9E9E9E),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        
        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = if (isDarkTheme) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF2196F3).copy(alpha = 0.2f)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
fun TypingIndicator(isDarkTheme: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = if (isDarkTheme) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF2196F3).copy(alpha = 0.2f)
        ) {
            Icon(
                Icons.Default.SmartToy,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        
        Surface(
            modifier = Modifier.shadow(8.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = if (isDarkTheme) Color(0xFF1E2337) else Color.White
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(3) { index ->
                    val infiniteTransition = rememberInfiniteTransition(label = "dot$index")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 0.5f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 200),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale$index"
                    )
                    Surface(
                        modifier = Modifier.size((8 * scale).dp),
                        shape = CircleShape,
                        color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
                    ) {}
                }
            }
        }
    }
}
