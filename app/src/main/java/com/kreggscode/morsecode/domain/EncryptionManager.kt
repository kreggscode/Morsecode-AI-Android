package com.kreggscode.morsecode.domain

import com.kreggscode.morsecode.util.Constants

class EncryptionManager {
    
    /**
     * Caesar cipher encryption
     */
    fun caesarEncrypt(text: String, shift: Int): String {
        val normalizedShift = shift % 26
        return text.map { char ->
            when {
                char.isUpperCase() -> {
                    ((char - 'A' + normalizedShift + 26) % 26 + 'A'.code).toChar()
                }
                char.isLowerCase() -> {
                    ((char - 'a' + normalizedShift + 26) % 26 + 'a'.code).toChar()
                }
                else -> char
            }
        }.joinToString("")
    }
    
    /**
     * Caesar cipher decryption
     */
    fun caesarDecrypt(text: String, shift: Int): String {
        return caesarEncrypt(text, -shift)
    }
    
    /**
     * Reverse text encryption
     */
    fun reverseEncrypt(text: String): String {
        return text.reversed()
    }
    
    /**
     * Reverse text decryption (same as encryption)
     */
    fun reverseDecrypt(text: String): String {
        return text.reversed()
    }
    
    /**
     * Custom key encryption (simple substitution)
     */
    fun customKeyEncrypt(text: String, key: String): String {
        if (key.length < 26) return text
        
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val keyUpper = key.uppercase().take(26)
        
        return text.map { char ->
            when {
                char.isUpperCase() -> {
                    val index = alphabet.indexOf(char)
                    if (index != -1) keyUpper[index] else char
                }
                char.isLowerCase() -> {
                    val index = alphabet.indexOf(char.uppercaseChar())
                    if (index != -1) keyUpper[index].lowercaseChar() else char
                }
                else -> char
            }
        }.joinToString("")
    }
    
    /**
     * Custom key decryption
     */
    fun customKeyDecrypt(text: String, key: String): String {
        if (key.length < 26) return text
        
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val keyUpper = key.uppercase().take(26)
        
        return text.map { char ->
            when {
                char.isUpperCase() -> {
                    val index = keyUpper.indexOf(char)
                    if (index != -1) alphabet[index] else char
                }
                char.isLowerCase() -> {
                    val index = keyUpper.indexOf(char.uppercaseChar())
                    if (index != -1) alphabet[index].lowercaseChar() else char
                }
                else -> char
            }
        }.joinToString("")
    }
    
    /**
     * Combined encryption: Caesar + Reverse
     */
    fun combinedEncrypt(text: String, caesarShift: Int): String {
        val caesarEncrypted = caesarEncrypt(text, caesarShift)
        return reverseEncrypt(caesarEncrypted)
    }
    
    /**
     * Combined decryption: Reverse + Caesar
     */
    fun combinedDecrypt(text: String, caesarShift: Int): String {
        val reversed = reverseDecrypt(text)
        return caesarDecrypt(reversed, caesarShift)
    }
    
    /**
     * Encrypt text then convert to Morse
     */
    fun encryptAndMorse(text: String, encryptionType: EncryptionType, key: Any? = null): String {
        val translator = MorseTranslator()
        val encrypted = when (encryptionType) {
            EncryptionType.NONE -> text
            EncryptionType.CAESAR -> caesarEncrypt(text, key as? Int ?: Constants.DEFAULT_CAESAR_SHIFT)
            EncryptionType.REVERSE -> reverseEncrypt(text)
            EncryptionType.CUSTOM -> customKeyEncrypt(text, key as? String ?: "")
            EncryptionType.COMBINED -> combinedEncrypt(text, key as? Int ?: Constants.DEFAULT_CAESAR_SHIFT)
        }
        return translator.textToMorse(encrypted)
    }
    
    /**
     * Convert Morse to text then decrypt
     */
    fun morseAndDecrypt(morse: String, encryptionType: EncryptionType, key: Any? = null): String {
        val translator = MorseTranslator()
        val text = translator.morseToText(morse)
        
        return when (encryptionType) {
            EncryptionType.NONE -> text
            EncryptionType.CAESAR -> caesarDecrypt(text, key as? Int ?: Constants.DEFAULT_CAESAR_SHIFT)
            EncryptionType.REVERSE -> reverseDecrypt(text)
            EncryptionType.CUSTOM -> customKeyDecrypt(text, key as? String ?: "")
            EncryptionType.COMBINED -> combinedDecrypt(text, key as? Int ?: Constants.DEFAULT_CAESAR_SHIFT)
        }
    }
}

enum class EncryptionType {
    NONE,
    CAESAR,
    REVERSE,
    CUSTOM,
    COMBINED
}
