package com.kreggscode.morsecode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kreggscode.morsecode.ui.navigation.NavGraph
import com.kreggscode.morsecode.ui.navigation.Screen
import com.kreggscode.morsecode.ui.screens.SplashScreen
import com.kreggscode.morsecode.ui.theme.MorseCodeTheme
import com.kreggscode.morsecode.ui.theme.GlassNavigationBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) } // Default to dark theme
            var showSplash by remember { mutableStateOf(true) }
            
            MorseCodeTheme(darkTheme = isDarkTheme) {
                if (showSplash) {
                    SplashScreen(
                        onSplashFinished = { showSplash = false }
                    )
                } else {
                    MorseCodeApp(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseCodeApp(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
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
                            Color(0xFFF5F7FA),
                            Color(0xFFE8EAF6),
                            Color(0xFFF0F2F5)
                        )
                    }
                )
            )
    ) {
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            NavGraph(
                navController = navController,
                onThemeToggle = onThemeToggle,
                isDarkTheme = isDarkTheme
            )
        }
        
        // Glassmorphism Navigation Bar
        GlassNavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .navigationBarsPadding(),
            isDarkTheme = isDarkTheme
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentDestination?.route == item.route
                
                FloatingNavItem(
                    icon = item.icon,
                    label = item.label,
                    isSelected = isSelected,
                    isDarkTheme = isDarkTheme,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun FloatingNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )
    
    val iconColor by animateColorAsState(
        targetValue = when {
            isSelected && isDarkTheme -> Color(0xFF00E5FF)
            isSelected && !isDarkTheme -> Color(0xFF2196F3)
            !isSelected && isDarkTheme -> Color(0xFF8E9AAF)
            else -> Color(0xFF6B7280)
        },
        animationSpec = tween(150),
        label = "iconColor"
    )
    
    Box(
        modifier = Modifier
            .size(56.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Selection indicator background
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = if (isDarkTheme) {
                                listOf(
                                    Color(0xFF00E5FF).copy(alpha = 0.2f),
                                    Color(0xFF00E5FF).copy(alpha = 0.05f)
                                )
                            } else {
                                listOf(
                                    Color(0xFF2196F3).copy(alpha = 0.15f),
                                    Color(0xFF2196F3).copy(alpha = 0.05f)
                                )
                            }
                        )
                    )
            )
        }
        
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Translator.route, Icons.Default.Translate, "Translator"),
    BottomNavItem(Screen.Encyclopedia.route, Icons.Default.MenuBook, "Guide"),
    BottomNavItem(Screen.Learn.route, Icons.Default.School, "Learn"),
    BottomNavItem(Screen.Games.route, Icons.Default.Games, "Games"),
    BottomNavItem(Screen.AiChat.route, Icons.Default.Chat, "AI"),
    BottomNavItem(Screen.History.route, Icons.Default.History, "History")
)
