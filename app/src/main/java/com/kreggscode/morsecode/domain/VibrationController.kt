package com.kreggscode.morsecode.domain

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.delay

class VibrationController(private val context: Context) {
    
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    companion object {
        private const val DOT_DURATION = 100L
        private const val DASH_DURATION = 300L
        private const val SYMBOL_GAP = 100L
        private const val LETTER_GAP = 300L
        private const val WORD_GAP = 700L
    }
    
    /**
     * Vibrate a Morse code pattern
     */
    suspend fun vibrateMorseCode(morseCode: String) {
        if (!vibrator.hasVibrator()) return
        
        val words = morseCode.split("   ")
        
        for ((wordIndex, word) in words.withIndex()) {
            val letters = word.split(" ")
            
            for ((letterIndex, letter) in letters.withIndex()) {
                for (symbol in letter) {
                    when (symbol) {
                        '.' -> vibrateDot()
                        '-' -> vibrateDash()
                    }
                    delay(SYMBOL_GAP)
                }
                
                if (letterIndex < letters.size - 1) {
                    delay(LETTER_GAP)
                }
            }
            
            if (wordIndex < words.size - 1) {
                delay(WORD_GAP)
            }
        }
    }
    
    /**
     * Vibrate a dot pattern
     */
    private fun vibrateDot() {
        vibrate(DOT_DURATION)
    }
    
    /**
     * Vibrate a dash pattern
     */
    private fun vibrateDash() {
        vibrate(DASH_DURATION)
    }
    
    /**
     * Vibrate for a specific duration
     */
    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
    
    /**
     * Vibrate a custom pattern
     */
    fun vibratePattern(pattern: LongArray, repeat: Int = -1) {
        if (!vibrator.hasVibrator()) return
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(pattern, repeat)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, repeat)
        }
    }
    
    /**
     * Stop vibration
     */
    fun stop() {
        vibrator.cancel()
    }
    
    /**
     * Simple haptic feedback
     */
    fun hapticFeedback() {
        vibrate(50L)
    }
    
    /**
     * Success pattern
     */
    fun vibrateSuccess() {
        val pattern = longArrayOf(0, 100, 50, 100)
        vibratePattern(pattern)
    }
    
    /**
     * Error pattern
     */
    fun vibrateError() {
        val pattern = longArrayOf(0, 200, 100, 200, 100, 200)
        vibratePattern(pattern)
    }
}
