package home.runforlisten.runningup

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import home.runforlisten.runningup.databinding.MaxMinVolumeSettingPageBinding

class MaxMinVolumeSelectActivity : AppCompatActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var binding: MaxMinVolumeSettingPageBinding
    private var maxPace: Int = 0
    private var minPace: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MaxMinVolumeSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // 페이스 값 출력
        maxPace = intent.getIntExtra("max_pace", 0)
        minPace = intent.getIntExtra("min_pace", 0)
        binding.maxPaceText.text = "$maxPace' 00''/km"
        binding.minPaceText.text = "$minPace' 00''/km"

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

                setVolume(progress)

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

                setVolume(progress)
                // SeekBar 값이 변경될 때마다 min_volume_value_text 업데이트
                minVolumeText.text = "$progress" // SeekBar의 값으로 텍스트 업데이트

                // min_volume_box를 보이도록 설정
                minVolumeBox.visibility = CardView.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }


    //음악 볼륨을 progress대로 변경
    fun setVolume(progress: Int) {
        // 0~100 사이의 값을 시스템 볼륨 범위에 맞춰서 설정
        val volumeLevel = (progress / 100.0 * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0)
    }

}
