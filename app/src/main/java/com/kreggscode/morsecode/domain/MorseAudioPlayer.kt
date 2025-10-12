package com.kreggscode.morsecode.domain

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlin.math.sin
import kotlin.math.PI
import com.kreggscode.morsecode.util.Constants

class MorseAudioPlayer {
    
    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val translator = MorseTranslator()
    
    private val sampleRate = Constants.AUDIO_SAMPLE_RATE
    private val frequency = Constants.AUDIO_FREQUENCY
    
    /**
     * Play Morse code as audio beeps
     */
    suspend fun playMorse(
        morse: String,
        speedMultiplier: Float = 1f,
        volume: Float = 1f,
        onProgress: ((Int, Int) -> Unit)? = null
    ) = withContext(Dispatchers.IO) {
        stopPlayback()
        
        val timings = translator.getMorseTiming(morse, speedMultiplier)
        if (timings.isEmpty()) return@withContext
        
        initAudioTrack(volume)
        
        playbackJob = launch {
            timings.forEachIndexed { index, timing ->
                if (!isActive) return@launch
                
                when (timing.type) {
                    TimingType.DOT, TimingType.DASH -> {
                        playBeep(timing.duration)
                    }
                    TimingType.SYMBOL_GAP, TimingType.LETTER_GAP, TimingType.WORD_GAP -> {
                        delay(timing.duration)
                    }
                }
                
                onProgress?.invoke(index + 1, timings.size)
            }
            
            stopPlayback()
        }
        
        playbackJob?.join()
    }
    
    /**
     * Play a single beep
     */
    private suspend fun playBeep(durationMs: Long) = withContext(Dispatchers.IO) {
        val numSamples = (durationMs * sampleRate / 1000).toInt()
        val buffer = ShortArray(numSamples)
        
        // Generate sine wave
        for (i in 0 until numSamples) {
            val angle = 2.0 * PI * i / (sampleRate / frequency)
            buffer[i] = (sin(angle) * Short.MAX_VALUE).toInt().toShort()
        }
        
        audioTrack?.write(buffer, 0, numSamples)
        audioTrack?.play()
        
        delay(durationMs)
    }
    
    /**
     * Initialize AudioTrack
     */
    private fun initAudioTrack(volume: Float) {
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
        
        audioTrack?.setVolume(volume.coerceIn(0f, 1f))
    }
    
    /**
     * Stop current playback
     */
    fun stopPlayback() {
        playbackJob?.cancel()
        playbackJob = null
        
        audioTrack?.apply {
            stop()
            release()
        }
        audioTrack = null
    }
    
    /**
     * Check if currently playing
     */
    fun isPlaying(): Boolean {
        return playbackJob?.isActive == true
    }
    
    /**
     * Clean up resources
     */
    fun release() {
        stopPlayback()
    }
}
