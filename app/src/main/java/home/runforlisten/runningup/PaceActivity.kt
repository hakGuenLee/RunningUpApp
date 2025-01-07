package home.runforlisten.runningup


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import home.runforlisten.runningup.databinding.PaceSelectBinding

class PaceActivity : AppCompatActivity() {

    private lateinit var binding: PaceSelectBinding

    var pace = 0 //페이스 선택 값
    var minVolume = 0 // 최소 볼륨 값
    var maxVolume = 0 // 최대 볼륨 값


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaceSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //천천히 버튼 클릭
        binding.slowPaceBtn.setOnClickListener {
            val slideOut = ObjectAnimator.ofFloat(binding.slowPaceBtn, "translationX", 1000f) // 1000f만큼 오른쪽으로 이동
            pace = 5
            slideOut.duration = 500 // 애니메이션 시간
            slideOut.start()
            binding.slowPaceVolumeBtn.isVisible = true

        }

        //조금 빠르게 버튼 클릭
        binding.regularPaceBtn.setOnClickListener {
            val slideOut = ObjectAnimator.ofFloat(binding.regularPaceBtn, "translationX", 1000f)
            pace = 6
            slideOut.duration = 500
            slideOut.start()
            binding.regularPaceVolumeBtn.isVisible = true

        }

        //빠르게 버튼 클릭
        binding.fastPaceBtn.setOnClickListener {
            val slideOut = ObjectAnimator.ofFloat(binding.fastPaceBtn, "translationX", 1000f)
            pace = 7
            slideOut.duration = 500
            slideOut.start()
            binding.fastPaceVolumeBtn.isVisible = true
        }

        //확인 버튼 클릭
        binding.confirmBtn.setOnClickListener {
            if(pace == 0){
                Toast.makeText(this, "페이스를 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, RunActivity::class.java)
                intent.putExtra("pace", pace)
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