package home.runforlisten.runningup

import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import home.runforlisten.runningup.databinding.MaxMinVolumeSettingPageBinding

class MaxMinVolumeSelectActivity : AppCompatActivity() {

    private lateinit var binding: MaxMinVolumeSettingPageBinding
    private lateinit var maxPace: String
    private lateinit var minPace: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MaxMinVolumeSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 페이스 값 출력
        maxPace = intent.getStringExtra("max_pace") ?: "0"
        minPace = intent.getStringExtra("min_pace") ?: "0"
        binding.maxPaceText.text = maxPace
        binding.minPaceText.text = minPace

        // SeekBar와 TextView 참조
        val maxVolumeSeekBar: SeekBar = binding.maxVolumeSettingSeekbar
        val minVolumeSeekBar: SeekBar = binding.minVolumeSettingSeekbar

        val maxVolumeText: TextView = binding.maxVolumeValueText
        val minVolumeText: TextView = binding.minVolumeValueText

        val maxVolumeBox: CardView = binding.maxVolumeBox
        val minVolumeBox: CardView = binding.minVolumeBox

        // SeekBar 값 변경 리스너 - 최고 볼륨
        maxVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // SeekBar 값이 변경될 때마다 max_volume_value_text 업데이트
                maxVolumeText.text = "$progress" // SeekBar의 값으로 텍스트 업데이트

                // max_volume_box를 보이도록 설정
                maxVolumeBox.visibility = CardView.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // SeekBar 값 변경 리스너 - 최소 볼륨
        minVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // SeekBar 값이 변경될 때마다 min_volume_value_text 업데이트
                minVolumeText.text = "$progress" // SeekBar의 값으로 텍스트 업데이트

                // min_volume_box를 보이도록 설정
                minVolumeBox.visibility = CardView.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
