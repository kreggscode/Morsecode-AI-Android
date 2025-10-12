package com.kreggscode.morsecode.domain

import com.kreggscode.morsecode.util.Constants

class MorseTranslator {
    
    /**
     * Convert text to Morse code
     */
    fun textToMorse(text: String): String {
        if (text.isBlank()) return ""
        
        return text.uppercase().map { char ->
            Constants.CHAR_TO_MORSE[char] ?: ""
        }.filter { it.isNotEmpty() }
            .joinToString(" ")
    }
    
    /**
     * Convert Morse code to text
     */
    fun morseToText(morse: String): String {
        if (morse.isBlank()) return ""
        
        return morse.trim()
            .split(" ")
            .mapNotNull { code ->
                if (code.isNotEmpty()) {
                    Constants.MORSE_TO_CHAR[code]
                } else null
            }
            .joinToString("")
    }
    
    /**
     * Validate if string is valid Morse code
     */
    fun isValidMorse(morse: String): Boolean {
        if (morse.isBlank()) return true
        
        val morsePattern = Regex("^[.\\-/ ]+$")
        return morsePattern.matches(morse)
    }
    
    /**
     * Get Morse code for a single character
     */
    fun getCharMorse(char: Char): String? {
        return Constants.CHAR_TO_MORSE[char.uppercaseChar()]
    }
    
    /**
     * Get character from Morse code
     */
    fun getMorseChar(morse: String): Char? {
        return Constants.MORSE_TO_CHAR[morse]
    }
    
    /**
     * Get timing information for Morse code playback
     */
    fun getMorseTiming(morse: String, speedMultiplier: Float = 1f): List<MorseTiming> {
        val timings = mutableListOf<MorseTiming>()
        
        morse.forEach { symbol ->
            when (symbol) {
                '.' -> timings.add(
                    MorseTiming(
                        type = TimingType.DOT,
                        duration = (Constants.DOT_DURATION_MS / speedMultiplier).toLong()
                    )
                )
                '-' -> timings.add(
                    MorseTiming(
                        type = TimingType.DASH,
                        duration = (Constants.DASH_DURATION_MS / speedMultiplier).toLong()
                    )
                )
                ' ' -> timings.add(
                    MorseTiming(
                        type = TimingType.LETTER_GAP,
                        duration = (Constants.LETTER_GAP_MS / speedMultiplier).toLong()
                    )
                )
                '/' -> timings.add(
                    MorseTiming(
                        type = TimingType.WORD_GAP,
                        duration = (Constants.WORD_GAP_MS / speedMultiplier).toLong()
                    )
                )
            }
            
            // Add symbol gap after each symbol (except gaps)
            if (symbol in listOf('.', '-')) {
                timings.add(
                    MorseTiming(
                        type = TimingType.SYMBOL_GAP,
                        duration = (Constants.SYMBOL_GAP_MS / speedMultiplier).toLong()
                    )
                )
            }
        }
        
        return timings
    }
    
    /**
     * Split Morse code into individual characters
     */
    fun splitMorseIntoChars(morse: String): List<String> {
        return morse.trim().split(" ").filter { it.isNotEmpty() }
    }
}

data class MorseTiming(
    val type: TimingType,
    val duration: Long
)

enum class TimingType {
    DOT,
    DASH,
    SYMBOL_GAP,
    LETTER_GAP,
    WORD_GAP
}
