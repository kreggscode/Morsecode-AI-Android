package com.kreggscode.morsecode.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originalText: String,
    val morseCode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val translationType: String = "TEXT_TO_MORSE", // TEXT_TO_MORSE or MORSE_TO_TEXT
    val encryptionType: String? = null,
    val encryptionKey: String? = null
)
