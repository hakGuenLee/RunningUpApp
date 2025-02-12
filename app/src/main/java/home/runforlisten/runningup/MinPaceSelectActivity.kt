package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.MinPaceSettingPageBinding

class MinPaceSelectActivity : AppCompatActivity() {

    private lateinit var binding: MinPaceSettingPageBinding
    private var maxPace: Int = 0
    private var minPace: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MinPaceSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        maxPace = intent.getIntExtra("max_pace", 0)
        binding.maxPaceText.text = "$maxPace' 00''/km"



        val maxPaceSelectBar = binding.paceSettingSeekbar

        maxPaceSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(maxPaceSelectBar: SeekBar?, progress: Int, p2: Boolean) {
                val selectPaceValue = 20 - progress
                val realValue = selectPaceValue.toString()
                val minPaceText = "$realValue' 00''/km"
                val paceValue = "$realValue' 00''"
                minPace = selectPaceValue
                binding.minPaceText.text = "$minPaceText"
                binding.paceValueText.text = "$paceValue"

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.confirmBtn.setOnClickListener {

            if(minPace == 0){
                Toast.makeText(this, "최소 페이스를 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else if(minPace <= maxPace){
                Toast.makeText(this, "최고 페이스보다 느린 페이스를 선택해 주세요!", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, MaxMinVolumeSelectActivity::class.java)
                intent.putExtra("max_pace", maxPace)
                intent.putExtra("min_pace", minPace)
                startActivity(intent)
                finish()
            }


        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MinPaceSelectActivity, MaxPaceSelectActivity::class.java)
                startActivity(intent)
            }
        }

        onBackPressedDispatcher.addCallback(this@MinPaceSelectActivity, callback)

    }


}