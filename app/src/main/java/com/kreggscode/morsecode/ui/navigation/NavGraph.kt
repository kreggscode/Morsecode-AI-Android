package com.kreggscode.morsecode.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kreggscode.morsecode.ui.screens.*
import com.kreggscode.morsecode.viewmodel.*

sealed class Screen(val route: String) {
    object Translator : Screen("translator")
    object Voice : Screen("voice")
    object Learn : Screen("learn")
    object Games : Screen("games")
    object AiChat : Screen("ai_chat")
    object History : Screen("history")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Translator.route
    ) {
        composable(Screen.Translator.route) {
            val viewModel: TranslatorViewModel = viewModel()
            TranslatorScreen(viewModel = viewModel, onThemeToggle = onThemeToggle)
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
    }
}
