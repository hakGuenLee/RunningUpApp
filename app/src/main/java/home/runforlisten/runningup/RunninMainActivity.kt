package home.runforlisten.runningup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import home.runforlisten.runningup.databinding.RunningMainPageBinding

class RunninMainActivity : AppCompatActivity(), TimeHandler.TimerCallback {

    private lateinit var binding: RunningMainPageBinding
    private var maxPace: Int = 0
    private var minPace: Int = 0
    private var maxVolume: Int = 0
    private var minVolume: Int = 0
    private var maxPaceMinutes: Int = 0
    private var maxPaceSeconds: Int = 0
    private var minPaceMinutes: Int = 0
    private var minPaceSeconds: Int = 0

    private var timeHandler : TimeHandler? = null
    private var isTimerPaused = true

    private lateinit var locationManager: LocationManager
    private var lastLocation: Location? = null
    private var previousLocation: Location? = null
    private var totalDistance: Float = 0f // 누적된 달린 거리 (미터)
    private var totalTime: Long = 0 // 총 시간 (밀리초)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunningMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //타이머 객체
        timeHandler = TimeHandler(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        // 권한 체크 (위치 권한)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }


        maxPace = intent.getIntExtra("max_pace", 0)
        minPace = intent.getIntExtra("min_pace", 0)
        maxPaceMinutes = intent.getIntExtra("max_pace_minutes",0)
        maxPaceSeconds = intent.getIntExtra("max_pace_seconds",0)
        minPaceMinutes = intent.getIntExtra("min_pace_minutes",0)
        minPaceSeconds = intent.getIntExtra("min_pace_seconds",0)
        maxVolume = intent.getIntExtra("max_volume", 0)
        minVolume = intent.getIntExtra("min_volume", 0)

        binding.maxVolume.text = "Max Volume : $maxVolume"
        binding.targetPace.text = "$maxPaceMinutes' $maxPaceSeconds''/km"



        binding.startBtn.setOnClickListener {

            if (isTimerPaused) {
                // 타이머가 일시정지 상태일 때 -> 다시 시작
                binding.startBtn.setImageResource(R.drawable.run_pause_background)
                timeHandler!!.startTimer()
                isTimerPaused = false
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1f, locationListener)

            } else {
                // 타이머가 실행 중일 때 -> 일시정지

                binding.startBtn.setImageResource(R.drawable.run_start_icon)
                timeHandler!!.pauseTimer()
                isTimerPaused = true
            }

        }

        binding.stopBtn.setOnClickListener {
            locationManager.removeUpdates(locationListener)

            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onTick(time: String) {
        binding.timer.text = time
        if(!isTimerPaused){
            totalTime += 1000
        }
    }

    override fun onTimerStop() {
        TODO("Not yet implemented")
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (lastLocation != null) {
                // 이동 거리 계산 (미터 단위)
                val distance = lastLocation!!.distanceTo(location)
                totalDistance += distance  // 총 거리 업데이트
            }
            lastLocation = location  // 마지막 위치 업데이트

            val pace = calculatePace()


            binding.paceStatusText.text = "$pace"


            val distanceInKm = totalDistance / 1000  // 킬로미터 단위로 변환
            binding.distance.text = String.format("%.2f km", distanceInKm)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    private fun calculatePace(): String {
        // totalDistance와 totalTime을 이용하여 페이스를 계산
        if (totalDistance > 0 && totalTime > 0) {
            // 1킬로미터에 대한 시간 계산 (분/킬로미터)
            val paceInMinutesPerKm = (totalTime / 1000.0) / (totalDistance / 1000.0) / 60.0

            // 페이스를 분/초로 변환
            val minutes = paceInMinutesPerKm.toInt()
            val seconds = ((paceInMinutesPerKm - minutes) * 60).toInt()

            return String.format("%02d' %02d'' / km", minutes, seconds)
        }
        return "00' 00''"  // 계산할 수 없을 때
    }









}