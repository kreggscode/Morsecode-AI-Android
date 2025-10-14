package com.kreggscode.morsecode.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kreggscode.morsecode.ui.screens.*
import com.kreggscode.morsecode.viewmodel.*

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Translator : Screen("translator")
    object Voice : Screen("voice")
    object Learn : Screen("learn")
    object Games : Screen("games")
    object AiChat : Screen("ai_chat")
    object History : Screen("history")
    object Settings : Screen("settings")
    object Encyclopedia : Screen("encyclopedia")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    onThemeToggle: () -> Unit,
    isDarkTheme: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Translator.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Translator.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Translator.route) {
            val viewModel: TranslatorViewModel = viewModel()
            TranslatorScreenRevamped(
                viewModel = viewModel,
                onThemeToggle = onThemeToggle,
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                isDarkTheme = isDarkTheme
            )
        }
        
        composable(Screen.Voice.route) {
            val viewModel: VoiceViewModel = viewModel()
            VoiceScreen(viewModel = viewModel)
        }
        
        composable(Screen.Learn.route) {
            val viewModel: LearnViewModel = viewModel()
            LearnScreen(viewModel = viewModel)
        }
        
        composable(Screen.Games.route) {
            val viewModel: GamesViewModel = viewModel()
            GamesScreen(viewModel = viewModel)
        }
        
        composable(Screen.AiChat.route) {
            val viewModel: AiChatViewModel = viewModel()
            AiChatScreen(viewModel = viewModel, onThemeToggle = onThemeToggle)
        }
        
        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = viewModel()
            HistoryScreen(viewModel = viewModel)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Encyclopedia.route) {
            EncyclopediaScreen(
                isDarkTheme = isDarkTheme,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
