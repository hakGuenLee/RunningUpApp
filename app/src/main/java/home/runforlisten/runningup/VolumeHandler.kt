package home.runforlisten.runningup

import android.content.Context
import android.media.AudioManager

class VolumeHandler(context: Context) {

    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager


    // 볼륨을 0~100 범위로 설정하는 함수
    fun setVolume(progress: Int) {
        // 0~100 사이의 값을 시스템 볼륨 범위에 맞춰서 설정
        val volumeLevel = (progress / 100.0 * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0)
    }

    // 현재 볼륨을 0~100 사이로 반환하는 함수
    fun getCurrentVolumePercentage(): Int {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return ((currentVolume / maxVolume.toFloat()) * 100).toInt()
    }




}