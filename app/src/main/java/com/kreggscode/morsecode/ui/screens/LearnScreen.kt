package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import com.kreggscode.morsecode.viewmodel.LearnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(viewModel: LearnViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val learnedCount by viewModel.learnedCount.collectAsState()
    var showQuizDialog by remember { mutableStateOf(false) }
    
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
            Text(
                text = "Learn Morse Code",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = { showQuizDialog = true }) {
                Icon(Icons.Default.Quiz, "Start Quiz")
            }
        }
            // Progress Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Learning Progress",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = learnedCount / 54f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$learnedCount / 54 characters learned",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Category Tabs
            ScrollableTabRow(
                selectedTabIndex = when (uiState.selectedCategory) {
                    "Letters" -> 0
                    "Numbers" -> 1
                    else -> 2
                }
            ) {
                Tab(
                    selected = uiState.selectedCategory == "Letters",
                    onClick = { viewModel.selectCategory("Letters") },
                    text = { Text("Letters") }
                )
                Tab(
                    selected = uiState.selectedCategory == "Numbers",
                    onClick = { viewModel.selectCategory("Numbers") },
                    text = { Text("Numbers") }
                )
                Tab(
                    selected = uiState.selectedCategory == "Punctuation",
                    onClick = { viewModel.selectCategory("Punctuation") },
                    text = { Text("Punctuation") }
                )
            }
            
            // Character Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.filteredCharacters) { character ->
                    CharacterCard(
                        character = character.char,
                        morse = character.morse,
                        isPlaying = uiState.playingCharacter == character.char,
                        onClick = { viewModel.playCharacter(character.char) }
                    )
                }
            }
    }
    
    // Quiz Dialog
    if (showQuizDialog && !uiState.isQuizMode) {
        AlertDialog(
            onDismissRequest = { showQuizDialog = false },
            title = { Text("Start Quiz") },
            text = { Text("Test your knowledge of ${uiState.selectedCategory}!") },
            confirmButton = {
                Button(onClick = {
                    viewModel.startQuiz()
                    showQuizDialog = false
                }) {
                    Text("Start")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuizDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Quiz Mode
    if (uiState.isQuizMode) {
        QuizMode(viewModel, uiState)
    }
    
    // Quiz Complete Dialog
    if (uiState.quizComplete) {
        AlertDialog(
            onDismissRequest = { viewModel.exitQuiz() },
            title = { Text("Quiz Complete!") },
            text = {
                Text("Score: ${uiState.quizScore} / ${uiState.quizCharacters.size}")
            },
            confirmButton = {
                Button(onClick = { viewModel.exitQuiz() }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun CharacterCard(
    character: String,
    morse: String,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = character,
                style = MaterialTheme.typography.headlineMedium,
                color = if (isPlaying) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = morse,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isPlaying) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuizMode(viewModel: LearnViewModel, uiState: com.kreggscode.morsecode.viewmodel.LearnUiState) {
    var answer by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Question ${uiState.currentQuizIndex + 1} / ${uiState.quizCharacters.size}",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Score: ${uiState.quizScore}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Listen",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.replayQuizCharacter() }) {
                        Text("Replay Sound")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (!uiState.quizAnswered) {
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
                        viewModel.submitQuizAnswer(answer)
                        answer = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = answer.isNotEmpty()
                ) {
                    Text("Submit")
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (uiState.lastAnswerCorrect == true)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            if (uiState.lastAnswerCorrect == true) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = if (uiState.lastAnswerCorrect == true)
                                MaterialTheme.colorScheme.onTertiaryContainer
                            else
                                MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (uiState.lastAnswerCorrect == true) "Correct!" else "Incorrect",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Answer: ${uiState.quizCharacters[uiState.currentQuizIndex].char}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { viewModel.nextQuizQuestion() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next Question")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = { viewModel.exitQuiz() }) {
                Text("Exit Quiz")
            }
        }
    }
}
