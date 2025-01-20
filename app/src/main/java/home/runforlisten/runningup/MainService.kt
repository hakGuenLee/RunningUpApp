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


//메인 기능을 실시간 구현하는 백그라운드 서비스
//외부와 통신할 필요는 없기 때문에 로컬브로드캐스트 사용

class MainService: Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var audioManager: AudioManager
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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Intent로 전달받은 데이터를 초기화
        targetPace = intent.getDoubleExtra("maxpace", 0.0)
        targetPace2 = intent.getDoubleExtra("minpace", 0.0)
        maxVolume = intent.getIntExtra("maxVolume", 0)
        minVolume = intent.getIntExtra("minVolume", 0)

        // 포그라운드 서비스 시작
        startForegroundService()

        return START_STICKY  // 서비스가 강제로 종료될 때 자동으로 다시 시작되도록 설정
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
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