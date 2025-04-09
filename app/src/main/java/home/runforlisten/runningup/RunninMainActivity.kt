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
    private var lastLocation: Location? = null // 마지막 위치 정보(location 타입의 값)
    private var speed = 0f // 속도 값(float)
    private var speedHistory = mutableListOf<Float>() // 속도 히스토리 저장 (평균화 용)
    private val MAX_HISTORY_SIZE = 5 // 평균을 낼 데이터의 최대 크기


//    private var totalDistance: Float = 0f // 누적된 달린 거리 (미터)
//    private var totalTime: Long = 0 // 총 시간 (밀리초)
//    private var startTime: Long = 0 // 시작 시간 (밀리초)
//    private var currentTime: Long = 0 // 현재 시간 (밀리초)


    //페이스를 구하는 공식 : 이동한 총 거리(킬로미터) 분의 총 시간(초단위)를 60으로 나눔
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunningMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
//                startTime = System.currentTimeMillis()
                timeHandler!!.startTimer()
                isTimerPaused = false
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500L, 1f, locationListener)

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

            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }


    }

    private val locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location) {

            //일시 정지 상태일 때는 위치 업데이트 중지
            if(isTimerPaused) return

            // 속도 값을 5개까지 받음
            //locationlistener가 GPS기반으로 자동적으로 거리/시간을 측정해서 실시간으로 계산하는 속도값을 얻음(speed)
            speedHistory.add(location.speed)
            if (speedHistory.size > MAX_HISTORY_SIZE) {
                speedHistory.removeAt(0)  // 과거 데이터 삭제
            }

//            lastLocation?.let {

            if(speedHistory.size == MAX_HISTORY_SIZE){

                // 평균 속도 계산(저장된 속도값 5개의 평균값. 너무 지나치고 잦은 변동을 줄이기 위해..)
                speed = speedHistory.average().toFloat()

                //속도가 0보다 클 경우, 즉 사용자가 이동하고 있을 경우 페이스 계산
                if(speed > 0){
                    //현재 사용자의 속도라면 1km를 달리는데 얼마나 걸릴 지 알기 위해(즉 페이스) 현재 속도를 1000m로 나눔
                    val runningTimeOneKm = 1000 / speed

                    //속도가 갑자기 0이거나 무한대로 측정될 때를 대피해서 infinite가 아닐 경우에만
                    //페이스를 추출해서 표시
                    if(runningTimeOneKm.isFinite()){
                        //나눈 값에서 분, 초를 추출
                        val minutes = (runningTimeOneKm / 60).toInt()
                        val seconds = (runningTimeOneKm % 60).toInt()

                        //화면에 페이스값을 표시
                        val pace = String.format("%02d' %02d'' / km", minutes, seconds)
                        binding.paceStatusText.text = "$pace"
                    }else{
                        //정상적인 숫자가 아닐 경우에는 아래와 같이 표기
                        binding.paceStatusText.text = "--' --''/km"
                    }
                }else {
                    //속도가 0이면 아래의 텍스트를 표시
                    binding.paceStatusText.text = "--' --''/km"
                }
//            }

                lastLocation = location


            }

        }
    }

    override fun onTick(time: String) {
        binding.timer.text = time
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