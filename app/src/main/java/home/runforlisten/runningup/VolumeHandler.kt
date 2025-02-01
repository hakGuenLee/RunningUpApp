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


    fun volumeChanger(pace: String, maxPace: Int, minPace: Int, maxVolume: Int, minVolume: Int): Int {

        // 현재 볼륨
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolumeInSystem = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        // pace가 "00:00"이면, 볼륨을 minVolume으로 설정하고 종료
        if (pace == "00:00") {
            val systemMinVolume = convertToSystemVolume(minVolume, 100)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, systemMinVolume, 0)
            return currentVolume
        }

        // maxPace와 minPace를 km/h로 변환
        val maxSpeed = 60.0 / maxPace  // maxPace를 km/h로 변환
        val minSpeed = 60.0 / minPace  // minPace를 km/h로 변환

        // pace를 분/킬로미터로 받은 후, 이를 km/h로 변환
        val paceInMinutesPerKm = pace.split(":")
        val paceInMinutes = paceInMinutesPerKm[0].toInt()
        val paceInSeconds = paceInMinutesPerKm[1].toInt()

        // pace를 km/h로 변환 (분/킬로미터 -> km/h)
        val paceInKmPerH = 60.0 / (paceInMinutes + paceInSeconds / 60.0)

        // 목표 볼륨 계산
        val targetVolume = when {
            paceInKmPerH >= minSpeed && paceInKmPerH <= maxSpeed -> {
                // pace가 minSpeed와 maxSpeed 사이에 있을 때는 maxVolume으로 설정
                convertToSystemVolume(maxVolume, 100)
            }
            paceInKmPerH > maxSpeed -> convertToSystemVolume(maxVolume, 100)  // pace가 maxSpeed보다 빠를 때는 maxVolume
            paceInKmPerH < minSpeed -> convertToSystemVolume(minVolume, 100)  // pace가 minSpeed보다 느릴 때는 minVolume
            else -> currentVolume  // pace가 그 범위에 없으면 현재 볼륨을 유지
        }

        // targetVolume에 맞게 실제 볼륨을 설정
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0)

        // 현재 볼륨을 0~100 범위로 변환하여 리턴
        val scaledCurrentVolume = (currentVolume.toFloat() / maxVolumeInSystem) * 100
        return scaledCurrentVolume.toInt() // 0~100 범위로 반환
    }



    // 0~100 범위의 볼륨을 시스템의 0~maxVolumeInSystem 범위로 변환하는 함수
    private fun convertToSystemVolume(volume: Int, maxVolume: Int): Int {
        val maxSystemVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return (volume.toFloat() / maxVolume * maxSystemVolume).toInt()
    }




    // 현재 볼륨을 0~100 사이로 반환하는 함수
//    fun getCurrentVolumePercentage(): Int {
//        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
//        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
//        return ((currentVolume / maxVolume.toFloat()) * 100).toInt()
//    }




}