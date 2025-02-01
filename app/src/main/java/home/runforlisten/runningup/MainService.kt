package home.runforlisten.runningup

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


//메인 기능을 실시간 구현하는 백그라운드 서비스
//외부와 통신할 필요는 없기 때문에 로컬브로드캐스트 사용

class MainService: Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var audioManager: AudioManager
    private lateinit var volumeHandler: VolumeHandler
    private var previousLocation: Location? = null
    private var totalDistance = 0.0 // 이동한 총 거리
    private var targetPace: Int = 0 //타겟페이스 최대값
    private var targetPace2: Int = 0 //타겟페이스 최소값
    private var maxVolume: Int = 0 //최대볼륨
    private var minVolume: Int = 0 //최소볼륨

    private val CHANNEL_ID = "MainServiceChannel"

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        volumeHandler = VolumeHandler(this)

    }

    //포그라운드 서비스 시작
    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Main Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Running Service")
            .setContentText("위치 추적 중...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()

        startForeground(1, notification)
    }

    
    //서비스가 시작될때 수행되는 메서드
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Intent로 전달받은 데이터를 초기화
        targetPace = intent.getIntExtra("maxpace", 0)
        targetPace2 = intent.getIntExtra("minpace", 0)
        maxVolume = intent.getIntExtra("maxVolume", 0)
        minVolume = intent.getIntExtra("minVolume", 0)

        // 포그라운드 서비스 시작
        startForegroundService()

        //이동거리 계산 시작
        startLocationUpdates()
        return START_STICKY  // 서비스가 강제로 종료될 때 자동으로 다시 시작되도록 설정
    }

    //GPS기반으로 측정 시작 메서드
    private fun startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L, // 1초마다 위치 갱신
                1f, // 최소 거리 1미터
                this
            )
        } catch (e: SecurityException) {
            // 위치 권한이 없을 경우 처리
        }
    }

    //GPS기반 측정 종료 메서드
    private fun stopLocationUpdates() {
        locationManager.removeUpdates(this)  // 위치 업데이트 중지
    }


    //사용자의 이동을 감지하며 내부적으로 계속 호출되는 메서드
    override fun onLocationChanged(location: Location) {

        val speed = location.speed * 3.6f // m/s를 km/h로 변환

        //현재 페이스 계산
        val pace = calculatePace(location)
        //볼륨 조절
        val currentVolume = volumeHandler.volumeChanger(pace,targetPace, targetPace2, maxVolume, minVolume)


        if (previousLocation != null) {
            // 이전 위치와 현재 위치 사이의 거리 계산
            val distance = previousLocation!!.distanceTo(location).toDouble()
            totalDistance += distance
        }
        previousLocation = location

        // 이동 거리 정보를 프래그먼트로 전달하기 위한 브로드캐스트
        val intent = Intent("ACTION_DISTANCE_UPDATED")
        intent.putExtra("distance", totalDistance)
        intent.putExtra("pace", pace) //측정되는 페이스를 넘김
        intent.putExtra("currentVolume",currentVolume)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }



    private fun calculatePace(location: Location): String {
        // 이전 위치와 현재 위치 사이의 거리를 계산
        if (previousLocation != null) {
            // 현재 위치와 이전 위치 사이의 거리 (미터 단위)
            val distanceInMeters = previousLocation!!.distanceTo(location)
            totalDistance += distanceInMeters  // 총 거리 갱신

            // 이동 시간 (초 단위)
            val timeInSeconds = (location.time - previousLocation!!.time) / 1000.0

            if (timeInSeconds > 0) {
                // 이동한 거리를 킬로미터로 변환
                val distanceInKm = totalDistance / 1000.0

                // 이동 시간 (분)
                val timeInMinutes = timeInSeconds / 60.0

                // 페이스 계산: 시간당 1킬로미터를 주파하는 데 걸린 시간 (분/km)
                val paceInMinutesPerKm = timeInMinutes / distanceInKm

                // 페이스를 분/초로 변환
                val minutes = paceInMinutesPerKm.toInt()
                val seconds = ((paceInMinutesPerKm - minutes) * 60).toInt()

                return String.format("%02d:%02d", minutes, seconds)
            }
        }
        // 초기에는 "00:00"을 반환 (페이스 계산 전)
        return "00:00"
    }





    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }



    



}