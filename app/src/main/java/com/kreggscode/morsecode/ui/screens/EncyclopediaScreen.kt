package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kreggscode.morsecode.ui.theme.GlassCard
import com.kreggscode.morsecode.ui.theme.GlassColors

data class MorseEntry(
    val character: String,
    val morse: String,
    val description: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncyclopediaScreen(
    isDarkTheme: Boolean,
    onBack: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedEntry by remember { mutableStateOf<MorseEntry?>(null) }
    
    val categories = listOf("All", "Letters", "Numbers", "Punctuation", "Special")
    
    val morseEntries = remember {
        buildList {
            // Letters
            val letters = mapOf(
                "A" to ".-", "B" to "-...", "C" to "-.-.", "D" to "-..", "E" to ".",
                "F" to "..-.", "G" to "--.", "H" to "....", "I" to "..", "J" to ".---",
                "K" to "-.-", "L" to ".-..", "M" to "--", "N" to "-.", "O" to "---",
                "P" to ".--.", "Q" to "--.-", "R" to ".-.", "S" to "...", "T" to "-",
                "U" to "..-", "V" to "...-", "W" to ".--", "X" to "-..-", "Y" to "-.--",
                "Z" to "--.."
            )
            letters.forEach { (char, morse) ->
                add(MorseEntry(
                    character = char,
                    morse = morse,
                    description = "Letter $char - ${getLetterDescription(char)}",
                    category = "Letters"
                ))
            }
            
            // Numbers
            val numbers = mapOf(
                "0" to "-----", "1" to ".----", "2" to "..---", "3" to "...--", "4" to "....-",
                "5" to ".....", "6" to "-....", "7" to "--...", "8" to "---..", "9" to "----."
            )
            numbers.forEach { (char, morse) ->
                add(MorseEntry(
                    character = char,
                    morse = morse,
                    description = "Number $char - ${getNumberDescription(char)}",
                    category = "Numbers"
                ))
            }
            
            // Punctuation
            val punctuation = mapOf(
                "." to ".-.-.-", "," to "--..--", "?" to "..--..", "'" to ".----.",
                "!" to "-.-.--", "/" to "-..-.", "(" to "-.--.", ")" to "-.--.-",
                "&" to ".-...", ":" to "---...", ";" to "-.-.-.", "=" to "-...-",
                "+" to ".-.-.", "-" to "-....-", "_" to "..--.-", "\"" to ".-..-.",
                "$" to "...-..-", "@" to ".--.-."
            )
            punctuation.forEach { (char, morse) ->
                add(MorseEntry(
                    character = char,
                    morse = morse,
                    description = getPunctuationDescription(char),
                    category = "Punctuation"
                ))
            }
            
            // Special
            add(MorseEntry(
                character = "SOS",
                morse = "... --- ...",
                description = "International distress signal - Save Our Souls/Ship",
                category = "Special"
            ))
            add(MorseEntry(
                character = "Space",
                morse = "/",
                description = "Word separator in Morse code",
                category = "Special"
            ))
        }
    }
    
    val filteredEntries = remember(selectedCategory, searchQuery) {
        morseEntries.filter { entry ->
            val categoryMatch = selectedCategory == "All" || entry.category == selectedCategory
            val searchMatch = searchQuery.isEmpty() || 
                entry.character.contains(searchQuery, ignoreCase = true) ||
                entry.morse.contains(searchQuery) ||
                entry.description.contains(searchQuery, ignoreCase = true)
            categoryMatch && searchMatch
        }
    }
    
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
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 120.dp)
        ) {
            // Header
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isDarkTheme) Color.White.copy(alpha = 0.1f) 
                                    else Color.White.copy(alpha = 0.7f)
                                )
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                "Back",
                                tint = if (isDarkTheme) Color.White else Color(0xFF1565C0)
                            )
                        }
                        
                        Column {
                            Text(
                                text = "Encyclopedia",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF1565C0)
                            )
                            Text(
                                text = "Morse Code Reference",
                                fontSize = 13.sp,
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.7f) 
                                    else Color(0xFF1976D2).copy(alpha = 0.8f)
                            )
                        }
                    }
                    
                    Icon(
                        Icons.Default.MenuBook,
                        "Encyclopedia",
                        tint = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkTheme = isDarkTheme
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = {
                        Text(
                            "Search characters, morse, or description...",
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.5f) 
                                else Color(0xFF64B5F6)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            "Search",
                            tint = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Clear,
                                    "Clear",
                                    tint = if (isDarkTheme) Color.White else Color(0xFF1976D2)
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3),
                        unfocusedBorderColor = if (isDarkTheme) Color.White.copy(alpha = 0.3f) 
                            else Color(0xFF90CAF9),
                        focusedTextColor = if (isDarkTheme) Color.White else Color(0xFF0D47A1),
                        unfocusedTextColor = if (isDarkTheme) Color.White else Color(0xFF1565C0),
                        cursorColor = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3),
                        focusedContainerColor = if (isDarkTheme) Color.White.copy(alpha = 0.05f) 
                            else Color.White.copy(alpha = 0.7f),
                        unfocusedContainerColor = if (isDarkTheme) Color.White.copy(alpha = 0.02f) 
                            else Color.White.copy(alpha = 0.5f)
                    ),
                    singleLine = true
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Category Tabs - Compact Design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { category ->
                    CategoryChip(
                        text = category,
                        isSelected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        isDarkTheme = isDarkTheme
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Entries List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredEntries) { entry ->
                    MorseEntryCard(
                        entry = entry,
                        isDarkTheme = isDarkTheme,
                        onClick = { selectedEntry = entry }
                    )
                }
            }
        }
        
        // Detail Dialog
        selectedEntry?.let { entry ->
            MorseDetailDialog(
                entry = entry,
                isDarkTheme = isDarkTheme,
                onDismiss = { selectedEntry = null }
            )
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDarkTheme: Boolean
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) {
                    if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3)
                } else {
                    if (isDarkTheme) Color.White.copy(alpha = 0.1f) 
                    else Color.White.copy(alpha = 0.7f)
                }
            )
            .border(
                width = 1.dp,
                color = if (isSelected) {
                    Color.Transparent
                } else {
                    if (isDarkTheme) Color.White.copy(alpha = 0.3f) else Color(0xFF90CAF9)
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) {
                if (isDarkTheme) Color.Black else Color.White
            } else {
                if (isDarkTheme) Color.White else Color(0xFF1976D2)
            },
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun MorseEntryCard(
    entry: MorseEntry,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        isDarkTheme = isDarkTheme
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Character
                Box(
                    modifier = Modifier
                        .size(56.dp)
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
                    Text(
                        text = entry.character,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF1565C0)
                    )
                }
                
                Column {
                    Text(
                        text = entry.morse,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF0D47A1)
                    )
                    Text(
                        text = entry.category,
                        fontSize = 12.sp,
                        color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) 
                            else Color(0xFF1976D2).copy(alpha = 0.7f)
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                "Details",
                tint = if (isDarkTheme) Color.White.copy(alpha = 0.5f) 
                    else Color(0xFF64B5F6)
            )
        }
    }
}

@Composable
fun MorseDetailDialog(
    entry: MorseEntry,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = if (isDarkTheme) Color(0xFF1A1F3A) else Color.White,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
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
                    Text(
                        text = entry.character,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF1565C0)
                    )
                }
                
                Column {
                    Text(
                        text = entry.morse,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF0D47A1)
                    )
                    Text(
                        text = entry.category,
                        fontSize = 14.sp,
                        color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) 
                            else Color(0xFF1976D2).copy(alpha = 0.7f)
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = entry.description,
                    fontSize = 16.sp,
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.9f) 
                        else Color(0xFF424242),
                    lineHeight = 24.sp
                )
                
                Divider(
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.2f) 
                        else Color(0xFFE0E0E0)
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Timing:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF1976D2)
                    )
                    Text(
                        text = "• Dot (.) = 1 unit\n• Dash (-) = 3 units\n• Gap between symbols = 1 unit\n• Gap between letters = 3 units\n• Gap between words = 7 units",
                        fontSize = 13.sp,
                        color = if (isDarkTheme) Color.White.copy(alpha = 0.8f) 
                            else Color(0xFF616161),
                        lineHeight = 20.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Close",
                    color = if (isDarkTheme) GlassColors.CyanGlow else Color(0xFF2196F3)
                )
            }
        }
    )
}

fun getLetterDescription(char: String): String {
    return when (char) {
        "A" -> "Alpha, first letter of the alphabet"
        "B" -> "Bravo, military phonetic"
        "C" -> "Charlie, third letter"
        "D" -> "Delta, fourth letter"
        "E" -> "Echo, most common letter in English, shortest Morse code (single dot)"
        "F" -> "Foxtrot, sixth letter"
        "G" -> "Golf, seventh letter"
        "H" -> "Hotel, eighth letter"
        "I" -> "India, ninth letter"
        "J" -> "Juliet, tenth letter"
        "K" -> "Kilo, eleventh letter"
        "L" -> "Lima, twelfth letter"
        "M" -> "Mike, thirteenth letter"
        "N" -> "November, fourteenth letter"
        "O" -> "Oscar, fifteenth letter"
        "P" -> "Papa, sixteenth letter"
        "Q" -> "Quebec, seventeenth letter"
        "R" -> "Romeo, eighteenth letter"
        "S" -> "Sierra, nineteenth letter, part of SOS signal"
        "T" -> "Tango, single dash, second shortest Morse code"
        "U" -> "Uniform, twenty-first letter"
        "V" -> "Victor, twenty-second letter"
        "W" -> "Whiskey, twenty-third letter"
        "X" -> "X-ray, twenty-fourth letter"
        "Y" -> "Yankee, twenty-fifth letter"
        "Z" -> "Zulu, last letter of the alphabet"
        else -> "Letter $char"
    }
}

fun getNumberDescription(char: String): String {
    return when (char) {
        "0" -> "Zero - Five dashes"
        "1" -> "One - One dot, four dashes"
        "2" -> "Two - Two dots, three dashes"
        "3" -> "Three - Three dots, two dashes"
        "4" -> "Four - Four dots, one dash"
        "5" -> "Five - Five dots"
        "6" -> "Six - One dash, four dots"
        "7" -> "Seven - Two dashes, three dots"
        "8" -> "Eight - Three dashes, two dots"
        "9" -> "Nine - Four dashes, one dot"
        else -> "Number $char"
    }
}

fun getPunctuationDescription(char: String): String {
    return when (char) {
        "." -> "Period/Full stop - End of sentence"
        "," -> "Comma - Pause in sentence"
        "?" -> "Question mark - Interrogative"
        "'" -> "Apostrophe - Possessive or contraction"
        "!" -> "Exclamation mark - Emphasis"
        "/" -> "Slash - Division or separator"
        "(" -> "Left parenthesis - Opening bracket"
        ")" -> "Right parenthesis - Closing bracket"
        "&" -> "Ampersand - And symbol"
        ":" -> "Colon - List introducer"
        ";" -> "Semicolon - Sentence connector"
        "=" -> "Equal sign - Mathematical equality"
        "+" -> "Plus sign - Addition"
        "-" -> "Hyphen/Minus - Subtraction or connector"
        "_" -> "Underscore - Emphasis or space"
        "\"" -> "Quotation mark - Direct speech"
        "$" -> "Dollar sign - Currency"
        "@" -> "At sign - Email addresses"
        else -> "Punctuation mark $char"
    }
}
