package com.kreggscode.morsecode.domain

import android.content.Context
import android.hardware.camera2.CameraManager
import kotlinx.coroutines.*
import android.os.Build

class FlashlightController(private val context: Context) {
    
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraId: String? = null
    private var flashJob: Job? = null
    private val translator = MorseTranslator()
    
    init {
        try {
            cameraId = cameraManager.cameraIdList.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Flash Morse code using device LED
     */
    suspend fun flashMorse(
        morse: String,
        speedMultiplier: Float = 1f,
        onProgress: ((Int, Int) -> Unit)? = null
    ) = withContext(Dispatchers.Main) {
        stopFlashing()
        
        val timings = translator.getMorseTiming(morse, speedMultiplier)
        if (timings.isEmpty()) return@withContext
        
        flashJob = launch {
            timings.forEachIndexed { index, timing ->
                if (!isActive) return@launch
                
                when (timing.type) {
                    TimingType.DOT, TimingType.DASH -> {
                        setFlashlight(true)
                        delay(timing.duration)
                        setFlashlight(false)
                    }
                    TimingType.SYMBOL_GAP, TimingType.LETTER_GAP, TimingType.WORD_GAP -> {
                        delay(timing.duration)
                    }
                }
                
                onProgress?.invoke(index + 1, timings.size)
            }
            
            stopFlashing()
        }
        
        flashJob?.join()
    }
    
    /**
     * Turn flashlight on/off
     */
    private fun setFlashlight(enabled: Boolean) {
        try {
            cameraId?.let { id ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(id, enabled)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Stop flashing
     */
    fun stopFlashing() {
        flashJob?.cancel()
        flashJob = null
        setFlashlight(false)
    }
    
    /**
     * Check if currently flashing
     */
    fun isFlashing(): Boolean {
        return flashJob?.isActive == true
    }
    
    /**
     * Check if flashlight is available
     */
    fun isFlashlightAvailable(): Boolean {
        return try {
            cameraId != null && context.packageManager.hasSystemFeature("android.hardware.camera.flash")
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Clean up resources
     */
    fun release() {
        stopFlashing()
    }
}
