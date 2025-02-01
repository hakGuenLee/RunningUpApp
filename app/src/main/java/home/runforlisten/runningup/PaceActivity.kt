package home.runforlisten.runningup


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import home.runforlisten.runningup.databinding.PaceSelectBinding

class PaceActivity : AppCompatActivity() {

    private lateinit var binding: PaceSelectBinding
    private lateinit var volumeHandler: VolumeHandler

    //5~6km/1분의 방식으로 작동하기 때문에, 각각 선택한 메뉴마다 두개의 값을 설정
    var maxpace = 0 //목표 최대 페이스
    var minpace = 0 //목표 최소 페이스

    //사용자가 설정하는 최대 볼륨(목표 페이스 범위에 실제 페이스가 도달했을 때 조정할 볼륨값)
    // 최소 볼륨(목표 페이스보다 떨어질 때 볼륨도 해당 값으로 떨어뜨림)
    var minVolume = 0 // 최소 볼륨 값
    var maxVolume = 0 // 최대 볼륨 값
    val distance = 1.0 // 거리(킬로미터)

    private var currentButton: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaceSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // SeekBar 객체들을 찾기
        val slowPaceMaxSeekBar: SeekBar = binding.slowPaceMaxVolume
        val slowPaceMinSeekBar: SeekBar = binding.slowPaceMinVolume
        val regularPaceMaxSeekBar: SeekBar = binding.regularPaceMaxVolume
        val regularPaceMinSeekBar: SeekBar = binding.regularPaceMinVolume
        val fastPaceMaxSeekBar: SeekBar = binding.fastPaceMaxVolume
        val fastPaceMinSeekBar: SeekBar = binding.fastPaceMinVolume

        // TextView 객체들을 찾기 (각 SeekBar에 대응하는 TextView)
        val slowPaceMaxText: TextView = binding.slowPaceMaxVolumeText
        val slowPaceMinText: TextView = binding.slowPaceMinVolumeText
        val regularPaceMaxText: TextView = binding.regularPaceMaxVolumeText
        val regularPaceMinText: TextView = binding.regularPaceMinVolumeText
        val fastPaceMaxText: TextView = binding.fastPaceMaxVolumeText
        val fastPaceMinText: TextView = binding.fastPaceMinVolumeText


        // SeekBar와 TextView를 리스트로 묶어서 전달
        val seekBars = listOf(slowPaceMaxSeekBar, slowPaceMinSeekBar, regularPaceMaxSeekBar, regularPaceMinSeekBar, fastPaceMaxSeekBar, fastPaceMinSeekBar)
        val textViews = listOf(slowPaceMaxText, slowPaceMinText, regularPaceMaxText, regularPaceMinText, fastPaceMaxText, fastPaceMinText)

        setupMultipleSeekBars(seekBars, textViews, onVolumeChanged = {progress -> volumeHandler.setVolume(progress)})


        volumeHandler = VolumeHandler(this)

        //천천히 버튼 클릭 : 9~10분/1km
        binding.slowPaceBtn.setOnClickListener {


            //애니메이션 처리 설정
            val slideOut = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 1000f) // 1000f만큼 오른쪽으로 이동
            val slideInRegular = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 0f)
            val slideInFast = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 0f)
            slideOut.duration = 200
            slideInRegular.duration = 200
            slideInFast.duration = 200

            //애니메이션 시작
            slideOut.start()
            slideInRegular.start()
            slideInFast.start()

            //볼륨 선택 화면 보이기
            binding.slowPaceVolumeBtn.isVisible = true
            binding.regularPaceVolumeBtn.isVisible = false
            binding.fastPaceVolumeBtn.isVisible = false

            //페이스 값 할당
            maxpace = 9
            minpace = 10

        }

        //조금 빠르게 버튼 클릭 : 7~8분/1km
        binding.regularPaceBtn.setOnClickListener {

            //애니메이션 처리 설정
            val slideOut = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 1000f)
            val slideInSlow = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 0f)
            val slideInFast = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 0f)
            slideOut.duration = 200
            slideInSlow.duration = 200
            slideInFast.duration = 200

            //애니메이션 시작
            slideOut.start()
            slideInSlow.start()
            slideInFast.start()

            //볼륨 선택 화면 보이기
            binding.regularPaceVolumeBtn.isVisible = true
            binding.slowPaceVolumeBtn.isVisible = false
            binding.fastPaceVolumeBtn.isVisible = false

            //페이스 값 할당
            maxpace = 7
            minpace = 8

        }

        //빠르게 버튼 클릭 : 5~6분
        binding.fastPaceBtn.setOnClickListener {

            //애니메이션 처리 설정
            val slideOut = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 1000f)
            val slideInSlow = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 0f)
            val slideInRegular = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 0f)
            slideOut.duration = 200
            slideInSlow.duration = 200
            slideInRegular.duration = 200

            //애니메이션 시작
            slideOut.start()
            slideInSlow.start()
            slideInRegular.start()
            binding.fastPaceVolumeBtn.isVisible = true
            binding.regularPaceVolumeBtn.isVisible = false
            binding.slowPaceVolumeBtn.isVisible = false

            //페이스 값 할당
            maxpace = 5
            minpace = 6
        }

        //확인 버튼 클릭
        binding.confirmBtn.setOnClickListener {
            if(maxpace == 0 || minpace == 0 || maxVolume == 0 || minVolume == 0){
                Toast.makeText(this, "페이스와 볼륨을 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, RunActivity::class.java)
                intent.putExtra("maxpace", maxpace)
                intent.putExtra("minpace", minpace)
                intent.putExtra("minVolume", minVolume)
                intent.putExtra("maxVolume", maxVolume)

                startActivity(intent)
            }

        }

        // SeekBar의 값을 실시간으로 업데이트
        // 수정된 코드 적용: 각 SeekBar와 텍스트 뷰를 setup하고, 값을 설정하는 부분
        setupSeekBar(binding.slowPaceMinVolume, binding.slowPaceMinVolumeText) { minVolume = it; volumeHandler.setVolume(it) }
        setupSeekBar(binding.slowPaceMaxVolume, binding.slowPaceMaxVolumeText) { maxVolume = it; volumeHandler.setVolume(it) }

        setupSeekBar(binding.regularPaceMinVolume, binding.regularPaceMinVolumeText) { minVolume = it; volumeHandler.setVolume(it) }
        setupSeekBar(binding.regularPaceMaxVolume, binding.regularPaceMaxVolumeText) { maxVolume = it; volumeHandler.setVolume(it) }

        setupSeekBar(binding.fastPaceMinVolume, binding.fastPaceMinVolumeText) { minVolume = it; volumeHandler.setVolume(it) }
        setupSeekBar(binding.fastPaceMaxVolume, binding.fastPaceMaxVolumeText) { maxVolume = it; volumeHandler.setVolume(it) }


    }



    private fun setupMultipleSeekBars(seekBars: List<SeekBar>, textViews: List<TextView>, onVolumeChanged: (Int) -> Unit) {
        seekBars.forEachIndexed { index, seekBar ->
            // 각 SeekBar에 해당하는 TextView를 받아서 값을 업데이트하는 리스너 설정
            setupSeekBar(seekBar, textViews[index], onVolumeChanged)
        }
    }


    // SeekBar 리스너 설정 공통화
    // 기존 setupSeekBar 함수에서 volumeHandler와 TextView 값을 업데이트하는 코드 수정
    private fun setupSeekBar(seekBar: SeekBar, textView: TextView, onVolumeChanged: (Int) -> Unit) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 각 SeekBar의 진행 상태에 맞게 TextView의 값을 업데이트
                textView.text = progress.toString()

                // SeekBar의 진행 상태에 맞게 TextView의 위치를 이동
                val seekBarXPosition = seekBar?.x ?: 0f
                val seekBarWidth = seekBar?.width ?: 0
                textView.x = seekBarXPosition + (seekBarWidth * progress / 100f) - (textView.width / 2)

                // TextView를 보이게 함
                textView.visibility = View.VISIBLE

                // 볼륨값 변경
                onVolumeChanged(progress)  // volumeHandler 호출
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }



    // 속도 계산 함수
//    private fun calculateSpeed(distance: Double, time: Int): Double {
//        // 시간은 분 단위로 입력받고, 속도는 km/h로 반환
//        return (distance / time) * 60
//    }



}