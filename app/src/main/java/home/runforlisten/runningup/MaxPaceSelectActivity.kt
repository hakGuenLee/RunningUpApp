package home.runforlisten.runningup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Scroller
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.MaxPaceSettingPageBinding
import java.lang.reflect.Field
import java.lang.reflect.Method

class MaxPaceSelectActivity : AppCompatActivity() {

    private lateinit var binding: MaxPaceSettingPageBinding
    private lateinit var maxPaceSelectBar: SeekBar
    private lateinit var minutesPicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker
    private var minutes: Int = 0
    private var seconds: Int = 0
    private var minPaceMinutes: Int = 0
    private var minPaceSeconds: Int = 0
    private var maxPace: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MaxPaceSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        minutesPicker = binding.minutesPicker
        secondsPicker = binding.secondsPicker

        minutes = intent.getIntExtra("max_pace_minutes", 0)
        seconds = intent.getIntExtra("max_pace_seconds", 0)
        minPaceMinutes = intent.getIntExtra("min_pace_minutes", 0)
        minPaceSeconds = intent.getIntExtra("min_pace_seconds", 0)

        binding.maxPaceText.text = "$minutes' $seconds''/km"

        //numberPicker 값 초기화
        numberPickerValueSetter()


        maxPaceSelectBar = binding.paceSettingSeekbar

        maxPaceSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(maxPaceSelectBar: SeekBar?, progress: Int, p2: Boolean) {

                minutes = progress / 60;
                seconds = progress % 60;

                minutesPicker.value = minutes
                secondsPicker.value = seconds


                val selectPaceValue = 20 - progress
                val realValue = selectPaceValue.toString()


                val maxPaceText = "$minutes' $seconds''/km"
//                val paceValue = "$realValue' 00''"
                maxPace = selectPaceValue

                binding.maxPaceText.text = "$maxPaceText"
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

            if(minutes == 0){
                Toast.makeText(this, "최고 페이스를 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, MinPaceSelectActivity::class.java)
                intent.putExtra("max_pace", maxPace)
                intent.putExtra("max_pace_minutes", minutes)
                intent.putExtra("max_pace_seconds", seconds)
                intent.putExtra("min_pace_minutes", minPaceMinutes)
                intent.putExtra("min_pace_seconds", minPaceSeconds)
                startActivity(intent)
                finish()
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MaxPaceSelectActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        onBackPressedDispatcher.addCallback(this@MaxPaceSelectActivity, callback)

    }

    private fun updateSeekBar() {
        val minutes = minutesPicker.value
        val seconds = secondsPicker.value

        // 전체 시간을 초로 변환 후 SeekBar에 설정
        val totalSeconds = minutes * 60 + seconds
        maxPaceSelectBar.progress = totalSeconds

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