package home.runforlisten.runningup

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.MinPaceSettingPageBinding

class MinPaceSelectActivity : AppCompatActivity() {

    private lateinit var binding: MinPaceSettingPageBinding
    private lateinit var maxPace: String
    private lateinit var minPace: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MinPaceSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        maxPace = intent.getStringExtra("max_pace")?: "0"
        binding.maxPaceText.text = maxPace



        val maxPaceSelectBar = binding.paceSettingSeekbar

        maxPaceSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(maxPaceSelectBar: SeekBar?, progress: Int, p2: Boolean) {
                val selectPaceValue = 20 - progress
                val realValue = selectPaceValue.toString()
                minPace = "$realValue' 00''/km"
                val paceValue = "$realValue' 00''"
                binding.minPaceText.text = "$minPace"
                binding.paceValueText.text = "$paceValue"

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

    }


}