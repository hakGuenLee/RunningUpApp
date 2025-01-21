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
    private var previousLocation: Location? = null
    private var totalDistance = 0.0 // 이동한 총 거리
    private var targetPace: Double = 0.0
    private var targetPace2: Double = 0.0
    private var maxVolume: Int = 0
    private var minVolume: Int = 0

    private val CHANNEL_ID = "MainServiceChannel"

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

    }

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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Intent로 전달받은 데이터를 초기화
        targetPace = intent.getDoubleExtra("maxpace", 0.0)
        targetPace2 = intent.getDoubleExtra("minpace", 0.0)
        maxVolume = intent.getIntExtra("maxVolume", 0)
        minVolume = intent.getIntExtra("minVolume", 0)

        // 포그라운드 서비스 시작
        startForegroundService()

        //이동거리 계산 시작
        startLocationUpdates()
        return START_STICKY  // 서비스가 강제로 종료될 때 자동으로 다시 시작되도록 설정
    }

    override fun onLocationChanged(location: Location) {
        if (previousLocation != null) {
            // 이전 위치와 현재 위치 사이의 거리 계산
            val distance = previousLocation!!.distanceTo(location).toDouble()
            totalDistance += distance
        }
        previousLocation = location

        // 이동 거리 정보를 프래그먼트로 전달하기 위한 브로드캐스트
        val intent = Intent("ACTION_DISTANCE_UPDATED")
        intent.putExtra("distance", totalDistance)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }


    private fun stopLocationUpdates() {
        locationManager.removeUpdates(this)  // 위치 업데이트 중지
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


}