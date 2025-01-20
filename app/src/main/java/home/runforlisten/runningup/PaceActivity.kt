package home.runforlisten.runningup


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import home.runforlisten.runningup.databinding.PaceSelectBinding

class PaceActivity : AppCompatActivity() {

    private lateinit var binding: PaceSelectBinding

    var maxpace = 0 //페이스 선택 값 1
    var minpace = 0 //페이스 선택 값 2
    var realMaxPace = 0.0 // 실제 페이스 값 1
    var realMinPace = 0.0 // 실제 페이스 값 2
    var minVolume = 0 // 최소 볼륨 값
    var maxVolume = 0 // 최대 볼륨 값
    val distance = 1.0 // 거리(킬로미터)

    private var currentButton: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaceSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 예시 데이터: 10분에 1km를 달림
//        val timeInMinutes = 10 // 시간 (분)
//        val distanceInKilometers = 1.0 // 거리 (킬로미터)

        // 속도 계산 (km/h 단위)
//        val speedInKmPerHour = calculateSpeed(distanceInKilometers, timeInMinutes)
        //천천히 버튼 클릭 : 9~10분
        binding.slowPaceBtn.setOnClickListener {

            val slideOut = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 1000f) // 1000f만큼 오른쪽으로 이동
            val slideInRegular = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 0f)
            val slideInFast = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 0f)
//            resetButtonAnimation(binding.slowPaceBtn)
            maxpace = 10
            minpace = 9
            realMaxPace = calculateSpeed(distance, maxpace)
            realMinPace = calculateSpeed(distance, minpace)

            slideOut.duration = 200 // 애니메이션 시간
            slideInRegular.duration = 200
            slideInFast.duration = 200
            slideOut.start()
            slideInRegular.start()
            slideInFast.start()

            binding.slowPaceVolumeBtn.isVisible = true
            binding.regularPaceVolumeBtn.isVisible = false
            binding.fastPaceVolumeBtn.isVisible = false

        }

        //조금 빠르게 버튼 클릭 : 7~8분
        binding.regularPaceBtn.setOnClickListener {
//            resetButtonAnimation(binding.regularPaceBtn)
            val slideOut = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 1000f)
            val slideInSlow = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 0f)
            val slideInFast = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 0f)
            maxpace = 8
            minpace = 7
            realMaxPace = calculateSpeed(distance, maxpace)
            realMinPace = calculateSpeed(distance, minpace)

            slideOut.duration = 200
            slideInSlow.duration = 200
            slideInFast.duration = 200
            slideOut.start()
            slideInSlow.start()
            slideInFast.start()
            binding.regularPaceVolumeBtn.isVisible = true
            binding.slowPaceVolumeBtn.isVisible = false
            binding.fastPaceVolumeBtn.isVisible = false

        }

        //빠르게 버튼 클릭 : 5~6분
        binding.fastPaceBtn.setOnClickListener {
//            resetButtonAnimation(binding.fastPaceBtn)
            val slideOut = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 1000f)
            val slideInSlow = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 0f)
            val slideInRegular = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 0f)
            maxpace = 6
            minpace = 5
            realMaxPace = calculateSpeed(distance, maxpace)
            realMinPace = calculateSpeed(distance, minpace)

            slideOut.duration = 200
            slideInSlow.duration = 200
            slideInRegular.duration = 200
            slideOut.start()
            slideInSlow.start()
            slideInRegular.start()
            binding.fastPaceVolumeBtn.isVisible = true
            binding.regularPaceVolumeBtn.isVisible = false
            binding.slowPaceVolumeBtn.isVisible = false
        }

        //확인 버튼 클릭
        binding.confirmBtn.setOnClickListener {
            if(maxpace == 0 || minpace == 0 || maxVolume == 0 || minVolume == 0){
                Toast.makeText(this, "페이스와 볼륨을 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, RunActivity::class.java)
                intent.putExtra("maxpace", realMaxPace)
                intent.putExtra("minpace", realMinPace)
                intent.putExtra("minVolume", minVolume)
                intent.putExtra("maxVolume", maxVolume)

                startActivity(intent)
            }

        }

        // SeekBar의 값을 실시간으로 업데이트
        setupSeekBar(binding.slowPaceMinVolume) { minVolume = it }
        setupSeekBar(binding.slowPaceMaxVolume) { maxVolume = it }

        setupSeekBar(binding.regularPaceMinVolume) { minVolume = it }
        setupSeekBar(binding.regularPaceMaxVolume) { maxVolume = it }

        setupSeekBar(binding.fastPaceMinVolume) { minVolume = it }
        setupSeekBar(binding.fastPaceMaxVolume) { maxVolume = it }

    }

    // 속도 계산 함수
    private fun calculateSpeed(distance: Double, time: Int): Double {
        // 시간은 분 단위로 입력받고, 속도는 km/h로 반환
        return (distance / time) * 60
    }

    private fun resetButtonAnimation(buttons: View) {
            if(buttons == binding.slowPaceBtn){

                // 각 버튼에 대해 ObjectAnimator로 애니메이션을 되돌리기
                val slideInRegular = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 0f)
                val slideInFast = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 0f)

                // 애니메이션 시간 설정
                slideInRegular.duration = 200
                slideInFast.duration = 200

                // 애니메이션 시작
                slideInRegular.start()
                slideInFast.start()
            }else if(buttons == binding.fastPaceBtn){

                // 각 버튼에 대해 ObjectAnimator로 애니메이션을 되돌리기
                val slideInRegular = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 0f)
                val slideInSlow = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 0f)

                // 애니메이션 시간 설정
                slideInRegular.duration = 200
                slideInSlow.duration = 200

                // 애니메이션 시작
                slideInRegular.start()
                slideInSlow.start()
            }else if(buttons == binding.regularPaceBtn){

                // 각 버튼에 대해 ObjectAnimator로 애니메이션을 되돌리기
                val slideInFast = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 0f)
                val slideInSlow = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 0f)

                // 애니메이션 시간 설정
                slideInFast.duration = 200
                slideInSlow.duration = 200

                // 애니메이션 시작
                slideInFast.start()
                slideInSlow.start()

            }
    }

    // SeekBar 리스너 설정 공통화
    private fun setupSeekBar(seekBar: SeekBar, onProgressChanged: (Int) -> Unit) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }


}