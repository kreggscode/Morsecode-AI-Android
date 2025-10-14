package com.kreggscode.morsecode.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var audioFrequency by remember { mutableFloatStateOf(800f) }
    var volume by remember { mutableFloatStateOf(0.8f) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var flashBrightness by remember { mutableFloatStateOf(1.0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDarkTheme) Color(0xFF1E2337) else Color.White,
                    titleContentColor = if (isDarkTheme) Color.White else Color.Black,
                    navigationIconContentColor = if (isDarkTheme) Color.White else Color.Black
                )
            )
        },
        containerColor = if (isDarkTheme) Color(0xFF0A0E27) else Color(0xFFF5F7FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Theme Section
            item {
                SettingsSection(
                    title = "Appearance",
                    isDarkTheme = isDarkTheme
                ) {
                    SettingsSwitchItem(
                        icon = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        title = "Dark Theme",
                        description = "Use dark theme for better night viewing",
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeToggle() },
                        isDarkTheme = isDarkTheme
                    )
                }
            }

            // Audio Settings Section
            item {
                SettingsSection(
                    title = "Audio Settings",
                    isDarkTheme = isDarkTheme
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        SettingsSliderItem(
                            icon = Icons.Default.Speed,
                            title = "Playback Speed",
                            value = playbackSpeed,
                            valueRange = 0.5f..2.0f,
                            steps = 5,
                            valueLabel = "${String.format("%.1f", playbackSpeed)}x",
                            onValueChange = { playbackSpeed = it },
                            isDarkTheme = isDarkTheme
                        )

                        SettingsSliderItem(
                            icon = Icons.Default.GraphicEq,
                            title = "Audio Frequency",
                            value = audioFrequency,
                            valueRange = 400f..1200f,
                            steps = 7,
                            valueLabel = "${audioFrequency.toInt()} Hz",
                            onValueChange = { audioFrequency = it },
                            isDarkTheme = isDarkTheme
                        )

                        SettingsSliderItem(
                            icon = Icons.Default.VolumeUp,
                            title = "Volume",
                            value = volume,
                            valueRange = 0f..1f,
                            steps = 9,
                            valueLabel = "${(volume * 100).toInt()}%",
                            onValueChange = { volume = it },
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }

            // Haptic Feedback Section
            item {
                SettingsSection(
                    title = "Haptic Feedback",
                    isDarkTheme = isDarkTheme
                ) {
                    SettingsSwitchItem(
                        icon = Icons.Default.Vibration,
                        title = "Vibration",
                        description = "Enable vibration feedback for Morse code output",
                        checked = vibrationEnabled,
                        onCheckedChange = { vibrationEnabled = it },
                        isDarkTheme = isDarkTheme
                    )
                }
            }

            // Flashlight Settings Section
            item {
                SettingsSection(
                    title = "Flashlight Settings",
                    isDarkTheme = isDarkTheme
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        SettingsSliderItem(
                            icon = Icons.Default.FlashlightOn,
                            title = "Flash Brightness",
                            value = flashBrightness,
                            valueRange = 0.3f..1f,
                            steps = 6,
                            valueLabel = "${(flashBrightness * 100).toInt()}%",
                            onValueChange = { flashBrightness = it },
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }

            // About Section
            item {
                SettingsSection(
                    title = "About",
                    isDarkTheme = isDarkTheme
                ) {
                    Column {
                        SettingsClickableItem(
                            icon = Icons.Default.Info,
                            title = "Version",
                            description = "1.1.0",
                            onClick = { },
                            isDarkTheme = isDarkTheme
                        )
                        Divider(
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.1f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        SettingsClickableItem(
                            icon = Icons.Default.Star,
                            title = "Rate this App",
                            description = "Share your feedback on Play Store",
                            onClick = {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                    data = android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.kreggscode.morsecode")
                                }
                                context.startActivity(intent)
                            },
                            isDarkTheme = isDarkTheme,
                            showArrow = true
                        )
                        Divider(
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.1f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        SettingsClickableItem(
                            icon = Icons.Default.Share,
                            title = "Share this App",
                            description = "Tell your friends about Morse Code Master",
                            onClick = {
                                val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out Morse Code Master!")
                                    putExtra(android.content.Intent.EXTRA_TEXT, 
                                        "Learn and practice Morse code with this amazing app! Download it now: https://play.google.com/store/apps/details?id=com.kreggscode.morsecode\n\nCheck out more apps: https://play.google.com/store/apps/dev?id=4822923174061161987")
                                }
                                context.startActivity(android.content.Intent.createChooser(shareIntent, "Share via"))
                            },
                            isDarkTheme = isDarkTheme,
                            showArrow = true
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = if (isDarkTheme) Color(0xFF1E2337) else Color.White,
            tonalElevation = 2.dp
        ) {
            content()
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isDarkTheme: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkTheme) Color.White else Color.Black
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}

@Composable
fun SettingsSliderItem(
    icon: ImageVector,
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    valueLabel: String,
    onValueChange: (Float) -> Unit,
    isDarkTheme: Boolean
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkTheme) Color.White else Color.Black
                )
            }
            Text(
                text = valueLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                activeTrackColor = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
                inactiveTrackColor = if (isDarkTheme) Color.White.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    isDarkTheme: Boolean,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF2196F3),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkTheme) Color.White else Color.Black
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
            )
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = if (isDarkTheme) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
