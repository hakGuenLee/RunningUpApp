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
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import home.runforlisten.runningup.databinding.RunningMainPageBinding

class RunninMainActivity : AppCompatActivity(), TimeHandler.TimerCallback {

    private lateinit var binding: RunningMainPageBinding
    private var maxPace: Int = 0 // 최고페이스
    private var minPace: Int = 0 // 최소페이스
    private var maxVolume: Int = 0 // 최대볼륨
    private var minVolume: Int = 0 // 최소볼륨
    private var maxPaceMinutes: Int = 0 //최대페이스 분
    private var maxPaceSeconds: Int = 0 //최대페이스 초
    private var minPaceMinutes: Int = 0 //최소페이스 분
    private var minPaceSeconds: Int = 0 //최소페이스 초

    private var timeHandler : TimeHandler? = null
    private var isTimerPaused = true

    private lateinit var locationManager: LocationManager //GPS기반으로 동작하는 로케이션매니저

    private var speed = 0f // 속도 값(float)
    private var speedHistory = mutableListOf<Float>() // 속도 히스토리 저장 (평균화 용)
    private val MAX_HISTORY_SIZE = 5 // 평균을 낼 데이터의 최대 크기




    private var lastLocation: Location? = null // 마지막 위치 정보(location 타입의 값)
    private var totalDistance: Float = 0f   //db에 평균 pace를 넣기 위한 계산에 필요한 총 거리값

    private var lastTimeString: String = "00:00:00" //계속 시간 값을 받음


//    private var totalDistance: Float = 0f // 누적된 달린 거리 (미터)
//    private var totalTime: Long = 0 // 총 시간 (밀리초)
//    private var startTime: Long = 0 // 시작 시간 (밀리초)
//    private var currentTime: Long = 0 // 현재 시간 (밀리초)


    //페이스를 구하는 공식 : 이동한 총 거리(킬로미터) 분의 총 시간(초단위)를 60으로 나눔
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunningMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)

        //타이머 객체 생성
        timeHandler = TimeHandler(this)

        //locationManager 초기화
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        // 권한 체크 (위치 권한)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        //메인기능에 필요한 모든 값들 초기화(intent로 값을 넘겨받고 할당함)
        //사용자가 정의한 최대,최소 페이스 & 최대, 최소 볼륨 값을 넘겨 받음
        maxPace = intent.getIntExtra("max_pace", 0)
        minPace = intent.getIntExtra("min_pace", 0)
        maxPaceMinutes = intent.getIntExtra("max_pace_minutes",0)
        maxPaceSeconds = intent.getIntExtra("max_pace_seconds",0)
        minPaceMinutes = intent.getIntExtra("min_pace_minutes",0)
        minPaceSeconds = intent.getIntExtra("min_pace_seconds",0)
        maxVolume = intent.getIntExtra("max_volume", 0)
        minVolume = intent.getIntExtra("min_volume", 0)

        //화면에 최대 볼륨, 최대 페이스 값 표시
        binding.maxVolume.text = "Max Volume : $maxVolume"
        binding.targetPace.text = "$maxPaceMinutes' $maxPaceSeconds''/km"


        //시작버튼 터치
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

        //종료 버튼 터치
        binding.stopBtn.setOnClickListener {

            //종료 버튼을 누르면 결과 페이지로 이동
            locationManager.removeUpdates(locationListener)

            // 1. 문자열 분해
            val parts = lastTimeString.split(":")
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            val seconds = parts[2].toLong()

            Log.d("hours", hours.toString())
            Log.d("minutes", minutes.toString())
            Log.d("seconds", seconds.toString())


            val totalTimeMinutes = hours * 60 + minutes + seconds / 60.0  // Double 타입

            Log.d("totalTime", totalTimeMinutes.toString())

            val totalDistanceKm = totalDistance / 1000.0


            val paceText = if (totalDistanceKm > 0) {
                val totalPace = totalTimeMinutes / totalDistanceKm
                val paceMinutes = totalPace.toInt()
                val paceSeconds = ((totalPace - paceMinutes) * 60).toInt()
                String.format("%02d:%02d/km", paceMinutes, paceSeconds) // "분:초/km"
            } else {
                "00:00/km" // 이동거리 없으면 기본값
            }


            Log.d("paceText", paceText)


            dbHelper.insertPaceTest(paceText)

            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }


    }

    private val locationListener = object : LocationListener{

        override fun onLocationChanged(location: Location) {
            if (isTimerPaused) return

            val currentSpeed = location.speed
            val MIN_SPEED_THRESHOLD = 0.5f            // 평균 속도 기준
            val EFFECTIVE_ZERO_THRESHOLD = 0.3f       // 사실상 멈춘 것으로 간주하는 최소 속도


            // 이전 위치가 있으면 거리 계산 (총 거리 정보를 알기 위해 누적시키기)
            lastLocation?.let { prev ->
                val distance = prev.distanceTo(location)  // 두 지점 사이 거리 (미터)
                if (distance > 1) { // 너무 작은 값(측정 오차) 제거
                    totalDistance += distance
                }
            }

            // 현재 위치를 lastLocation에 저장
            lastLocation = location

            // ------------------------
            // 1. 사실상 멈춘 상태라면: speedHistory를 0으로 채워버리기
            // ------------------------
            if (currentSpeed < EFFECTIVE_ZERO_THRESHOLD) {
                speedHistory.clear()
                repeat(MAX_HISTORY_SIZE) {
                    speedHistory.add(0f)
                }
            } else {
                // 움직이는 중이면 기존대로 speedHistory에 추가
                speedHistory.add(currentSpeed)
                if (speedHistory.size > MAX_HISTORY_SIZE) {
                    speedHistory.removeAt(0)
                }
            }

            // ------------------------
            // 2. 평균 속도 기반 페이스 계산
            // ------------------------
            if (speedHistory.size == MAX_HISTORY_SIZE) {
                val avgSpeed = speedHistory.average().toFloat()

                if (avgSpeed < MIN_SPEED_THRESHOLD) {
                    // 평균 속도가 기준치보다 낮으면 멈춘 것으로 간주
                    binding.paceStatusText.text = "--' --''/km"
                } else {
                    // 정상 이동 중 → pace 계산
                    val runningTimeOneKm = 1000 / avgSpeed
                    if (runningTimeOneKm.isFinite()) {
                        val minutes = (runningTimeOneKm / 60).toInt()
                        val seconds = (runningTimeOneKm % 60).toInt()
                        val pace = String.format("%02d' %02d'' / km", minutes, seconds)
                        binding.paceStatusText.text = pace
                    } else {
                        binding.paceStatusText.text = "--' --''/km"
                    }
                }
            }

            // ------------------------
            // 3. (선택) 현재 속도 텍스트 표시 (디버그용)
            // ------------------------
            binding.currentSpeedText.text = String.format("%.2f m/s", currentSpeed)
        }


    }

    override fun onTick(time: String) {
        binding.timer.text = time
        lastTimeString = time
    }

    override fun onTimerStop() {
        TODO("Not yet implemented")
    }

//    override fun onTick(time: String) {
//        binding.timer.text = time
//        if(!isTimerPaused){
//            currentTime = System.currentTimeMillis() - startTime
//        }
//    }
//
//    override fun onTimerStop() {
//        TODO("Not yet implemented")
//    }

    //GPS 기반으로 동작하는 로케이션리스너
//    private val locationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//
//            //일시 정지 상태일때는 위치업데이트 중지
//            if (isTimerPaused) return
//
//            //움직이기 시작하면 거리를 누적시키면서 업데이트해줌
//            if (lastLocation != null) {
//                // 이동 거리 계산 (미터 단위)
//                val distance = lastLocation!!.distanceTo(location)
//                totalDistance += distance  // 총 거리 업데이트
//            }
//            //로케이션 리스너가 제공해주는 location을 마지막 위치에 할당
//            lastLocation = location  // 마지막 위치 업데이트
//
//            //실제적으로 페이스를 계산하는 함수 호출
//            val pace = calculatePace()
//
//
//            binding.paceStatusText.text = "$pace"
//
//
//            val distanceInKm = totalDistance / 1000  // 킬로미터 단위로 변환
//            binding.distance.text = String.format("%.2f km", distanceInKm)
//        }
//
//        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//    }
//
//    //페이스를 구하는 함수
//    private fun calculatePace(): String {
//
//        if (totalDistance > 0 && currentTime > 0) {
//
//            // totalDistance와 totalTime을 이용하여 페이스를 계산 > 즉 총 거리 분의 총 시간을 60으로 나눔
//            val paceInMinutesPerKm = (currentTime / 1000.0) / (totalDistance / 1000.0) / 60.0
//            val minutes = paceInMinutesPerKm.toInt()
//            val seconds = ((paceInMinutesPerKm - minutes) * 60).toInt()
//            return String.format("%02d' %02d'' / km", minutes, seconds)
//        }
//        return "--' --''/km"  // 계산할 수 없을 때
//    }


}