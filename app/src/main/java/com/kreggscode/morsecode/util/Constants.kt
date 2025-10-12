package com.kreggscode.morsecode.util

object Constants {
    // Morse Code Mappings
    val CHAR_TO_MORSE = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
        'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
        'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
        'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
        'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
        'Z' to "--..",
        '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--", '4' to "....-",
        '5' to ".....", '6' to "-....", '7' to "--...", '8' to "---..", '9' to "----.",
        '.' to ".-.-.-", ',' to "--..--", '?' to "..--..", '\'' to ".----.",
        '!' to "-.-.--", '/' to "-..-.", '(' to "-.--.", ')' to "-.--.-",
        '&' to ".-...", ':' to "---...", ';' to "-.-.-.", '=' to "-...-",
        '+' to ".-.-.", '-' to "-....-", '_' to "..--.-", '"' to ".-..-.",
        '$' to "...-..-", '@' to ".--.-.", ' ' to "/"
    )
    
    val MORSE_TO_CHAR = CHAR_TO_MORSE.entries.associate { (k, v) -> v to k }
    
    // Audio Settings
    const val DOT_DURATION_MS = 100L
    const val DASH_DURATION_MS = 300L
    const val SYMBOL_GAP_MS = 100L
    const val LETTER_GAP_MS = 300L
    const val WORD_GAP_MS = 700L
    const val AUDIO_FREQUENCY = 800 // Hz
    const val AUDIO_SAMPLE_RATE = 44100
    
    // API Settings
    const val POLLINATIONS_BASE_URL = "https://text.pollinations.ai/"
    const val API_TIMEOUT_SECONDS = 60L
    
    // Database
    const val DATABASE_NAME = "morse_code_database"
    const val DATABASE_VERSION = 1
    
    // DataStore
    const val PREFERENCES_NAME = "morse_code_preferences"
    const val KEY_THEME_MODE = "theme_mode"
    const val KEY_AUDIO_VOLUME = "audio_volume"
    const val KEY_PLAYBACK_SPEED = "playback_speed"
    
    // Theme
    const val THEME_LIGHT = 0
    const val THEME_DARK = 1
    const val THEME_SYSTEM = 2
    
    // Learning Progress
    const val TOTAL_LETTERS = 26
    const val TOTAL_NUMBERS = 10
    const val TOTAL_PUNCTUATION = 18
    
    // Game Settings
    const val GAME_EASY_TIME_LIMIT = 10
    const val GAME_MEDIUM_TIME_LIMIT = 7
    const val GAME_HARD_TIME_LIMIT = 5
    const val GAME_POINTS_CORRECT = 10
    const val GAME_POINTS_WRONG = -5
    
    // Encryption
    const val DEFAULT_CAESAR_SHIFT = 3
    const val MAX_CAESAR_SHIFT = 25
}
