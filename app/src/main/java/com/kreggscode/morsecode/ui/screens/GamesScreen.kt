package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import com.kreggscode.morsecode.viewmodel.GameType
import com.kreggscode.morsecode.viewmodel.GamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(viewModel: GamesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = 100.dp) // Space for nav bar
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.isPlaying) {
                    IconButton(onClick = { viewModel.exitGame() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Text(
                    text = "Morse Games",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
            }
            
            if (!uiState.isPlaying && uiState.selectedGame == null) {
                GameSelection(
                    modifier = Modifier.fillMaxSize(),
                    onGameSelected = { viewModel.selectGame(it) }
                )
            } else if (uiState.selectedGame != null && !uiState.isPlaying) {
                DifficultySelection(
                    modifier = Modifier.fillMaxSize(),
                    gameType = uiState.selectedGame!!,
                    onDifficultySelected = { viewModel.startGame(it) },
                    onBack = { viewModel.exitGame() }
                )
            } else if (uiState.isPlaying) {
                GamePlay(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
        
        if (uiState.gameComplete) {
            GameCompleteDialog(
                score = uiState.score,
                onDismiss = { viewModel.exitGame() }
            )
        }
    }
}

@Composable
fun GameSelection(
    modifier: Modifier = Modifier,
    onGameSelected: (GameType) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Choose a Game",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        GameCard(
            title = "Morse Decoder",
            description = "Hear morse code and guess the letter or number",
            icon = Icons.Default.Hearing,
            onClick = { onGameSelected(GameType.DECODER) }
        )
        
        GameCard(
            title = "Speed Challenge",
            description = "Translate as fast as you can!",
            icon = Icons.Default.Speed,
            onClick = { onGameSelected(GameType.SPEED_CHALLENGE) }
        )
        
        GameCard(
            title = "Memory Match",
            description = "Match letters with their morse patterns",
            icon = Icons.Default.Psychology,
            onClick = { onGameSelected(GameType.MEMORY_MATCH) }
        )
        
        GameCard(
            title = "SOS Rescue",
            description = "Decode emergency messages",
            icon = Icons.Default.Emergency,
            onClick = { onGameSelected(GameType.SOS_RESCUE) }
        )
    }
}

@Composable
fun GameCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun DifficultySelection(
    modifier: Modifier = Modifier,
    gameType: GameType,
    onDifficultySelected: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Difficulty",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = { onDifficultySelected("EASY") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Easy")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { onDifficultySelected("MEDIUM") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Medium")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { onDifficultySelected("HARD") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hard")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        TextButton(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun GamePlay(
    modifier: Modifier = Modifier,
    viewModel: GamesViewModel,
    uiState: com.kreggscode.morsecode.viewmodel.GamesUiState
) {
    var answer by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question ${uiState.questionNumber}/${uiState.totalQuestions}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Score: ${uiState.score}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = "Listen",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.replayQuestion() }) {
                    Text("Replay")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AnimatedVisibility(visible = uiState.lastAnswerCorrect != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.lastAnswerCorrect == true)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (uiState.lastAnswerCorrect == true) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (uiState.lastAnswerCorrect == true)
                            MaterialTheme.colorScheme.onTertiaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (uiState.lastAnswerCorrect == true) "Correct!" else "Try again!",
                        color = if (uiState.lastAnswerCorrect == true)
                            MaterialTheme.colorScheme.onTertiaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it.uppercase() },
            label = { Text("Your Answer") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                viewModel.submitAnswer(answer)
                answer = ""
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = answer.isNotEmpty()
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun GameCompleteDialog(
    score: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Game Complete!") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Final Score: $score",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
