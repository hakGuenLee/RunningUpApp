package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.MinPaceSettingPageBinding

class MinPaceSelectActivity : AppCompatActivity() {

    private lateinit var binding: MinPaceSettingPageBinding
    private lateinit var minPaceSelectBar: SeekBar
    private lateinit var minutesPicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker
    private var maxPace: Int = 0
    private var minPace: Int = 0
    private var maxPaceMinutes: Int = 0
    private var maxPaceSeconds: Int = 0
    private var minutes: Int = 0
    private var seconds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MinPaceSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        maxPace = intent.getIntExtra("max_pace", 0)
        maxPaceMinutes = intent.getIntExtra("max_pace_minutes",0)
        maxPaceSeconds = intent.getIntExtra("max_pace_seconds", 0)
        minutes = intent.getIntExtra("min_pace_minutes", 0)
        seconds = intent.getIntExtra("min_pace_seconds", 0)

        binding.maxPaceText.text = "$maxPaceMinutes' $maxPaceSeconds''/km"
        binding.minPaceText.text = "$minutes' $seconds''/km"



        minutesPicker = binding.minutesPicker
        secondsPicker = binding.secondsPicker


        minPaceSelectBar = binding.paceSettingSeekbar

        numberPickerValueSetter()

        minPaceSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(maxPaceSelectBar: SeekBar?, progress: Int, p2: Boolean) {

                minutes = progress / 60;
                seconds = progress % 60;

                minutesPicker.value = minutes
                secondsPicker.value = seconds


                val selectPaceValue = 20 - progress
                val realValue = selectPaceValue.toString()

                val minPaceText = "$minutes' $seconds''/km"
//                val paceValue = "$realValue' 00''"
                minPace = selectPaceValue

                binding.minPaceText.text = "$minPaceText"
//                binding.paceValueText.text = "$paceValue"

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        minutesPicker.setOnValueChangedListener { _, oldVal, newVal ->
            minutes = newVal

            updateSeekBar()
        }

        secondsPicker.setOnValueChangedListener { _, oldVal, newVal ->
            seconds = newVal
            updateSeekBar()
        }

        binding.confirmBtn.setOnClickListener {

            // 총 시간을 초 단위로 변환
            val minPaceInSeconds = minutes * 60 + seconds
            val maxPaceInSeconds = maxPaceMinutes * 60 + maxPaceSeconds



            if(minPaceInSeconds == 0){
                Toast.makeText(this, "최소 페이스를 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else if(minPaceInSeconds <= maxPaceInSeconds){
                Toast.makeText(this, "최고 페이스보다 느린 페이스를 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, MaxMinVolumeSelectActivity::class.java)
                intent.putExtra("max_pace", maxPace)
                intent.putExtra("min_pace", minPace)
                intent.putExtra("max_pace_minutes", maxPaceMinutes)
                intent.putExtra("max_pace_seconds", maxPaceSeconds)
                intent.putExtra("min_pace_minutes", minutes)
                intent.putExtra("min_pace_seconds", seconds)
                startActivity(intent)
                finish()
            }


        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MinPaceSelectActivity, MaxPaceSelectActivity::class.java)
                intent.putExtra("max_pace_minutes", maxPaceMinutes)
                intent.putExtra("max_pace_seconds", maxPaceSeconds)
                intent.putExtra("min_pace_minutes", minutes)
                intent.putExtra("min_pace_seconds", seconds)
                startActivity(intent)
            }
        }

        onBackPressedDispatcher.addCallback(this@MinPaceSelectActivity, callback)



    }

    private fun updateSeekBar() {
        val minutes = minutesPicker.value
        val seconds = secondsPicker.value

        // 전체 시간을 초로 변환 후 SeekBar에 설정
        val totalSeconds = minutes * 60 + seconds
        minPaceSelectBar.progress = totalSeconds

    }



    private fun numberPickerValueSetter(){
        minutesPicker.minValue = 0
        minutesPicker.maxValue = 20

        secondsPicker.minValue = 0
        secondsPicker.maxValue = 59

        minutesPicker.wrapSelectorWheel = true
        secondsPicker.wrapSelectorWheel = true

        minutesPicker.setFormatter { String.format("%02d", it) } // 두 자리 숫자 형식
        secondsPicker.setFormatter { String.format("%02d", it) }

    }


}