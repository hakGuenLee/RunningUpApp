package home.runforlisten.runningup

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper

class VolumeHandler(context: Context) {

    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager


    // 볼륨을 0~100 범위로 설정하는 함수
    fun setVolume(progress: Int) {
        // 0~100 사이의 값을 시스템 볼륨 범위에 맞춰서 설정
        val volumeLevel = (progress / 100.0 * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0)
    }


    fun volumeChanger(speed: Float, maxPace: Double, minPace: Double, maxVolume: Int, minVolume: Int
    ): Int {
        var currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        // speed가 minPace와 maxPace 사이에 있을 경우, maxVolume으로 설정
        val targetVolume = when {
            speed in minPace..maxPace -> maxVolume  // 속도가 minPace와 maxPace 사이일 때
            else -> minVolume  // 그 외의 경우에는 minVolume
        }

        // currentVolume과 targetVolume을 0~100 범위로 변환
        var scaledCurrentVolume = (currentVolume.toFloat() / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) * 100
        val scaledTargetVolume = (targetVolume.toFloat() / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) * 100

        if (scaledCurrentVolume != scaledTargetVolume) {
            val handler = Handler(Looper.getMainLooper())
            val runnable = object : Runnable {
                override fun run() {
                    if (scaledCurrentVolume != scaledTargetVolume) {
                        // targetVolume으로 서서히 이동
                        val nextVolume = (scaledCurrentVolume + if (scaledTargetVolume > scaledCurrentVolume) 1 else -1)
                            .coerceIn(0f, 100f).toInt()  // 0~100 범위로 coerce
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            (nextVolume / 100f * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt(),
                            0)
                        scaledCurrentVolume = nextVolume.toFloat()
                        handler.postDelayed(this, 100)  // 100ms마다 업데이트
                    }
                }
            }
            handler.post(runnable)
        }

        // 0~100 범위로 변환된 targetVolume을 반환
        return scaledTargetVolume.toInt()
    }


    // 현재 볼륨을 0~100 사이로 반환하는 함수
    fun getCurrentVolumePercentage(): Int {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return ((currentVolume / maxVolume.toFloat()) * 100).toInt()
    }




}