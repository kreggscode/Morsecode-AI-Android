package com.kreggscode.morsecode.util

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

// String Extensions
fun String.toMorseCode(): String {
    return this.uppercase().map { char ->
        Constants.CHAR_TO_MORSE[char] ?: ""
    }.joinToString(" ")
}

fun String.fromMorseCode(): String {
    return this.split(" ").mapNotNull { morse ->
        Constants.MORSE_TO_CHAR[morse]
    }.joinToString("")
}

fun String.caesarEncrypt(shift: Int): String {
    return this.map { char ->
        when {
            char.isUpperCase() -> ((char - 'A' + shift) % 26 + 'A'.code).toChar()
            char.isLowerCase() -> ((char - 'a' + shift) % 26 + 'a'.code).toChar()
            else -> char
        }
    }.joinToString("")
}

fun String.caesarDecrypt(shift: Int): String {
    return this.caesarEncrypt(26 - shift)
}

fun String.reverseString(): String {
    return this.reversed()
}

// Date Extensions
fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toTimeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000} min ago"
        diff < 86400000 -> "${diff / 3600000} hours ago"
        diff < 604800000 -> "${diff / 86400000} days ago"
        else -> this.toFormattedDate()
    }
}

// Context Extensions
fun Context.shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(intent, "Share via"))
}

// Color Extensions
fun Color.withAlpha(alpha: Float): Color {
    return this.copy(alpha = alpha)
}

// List Extensions
fun <T> List<T>.randomItem(): T? {
    return if (this.isNotEmpty()) this.random() else null
}

// Number Extensions
fun Float.clamp(min: Float, max: Float): Float {
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}

fun Int.clamp(min: Int, max: Int): Int {
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}
