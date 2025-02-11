package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.MaxPaceSettingPageBinding

class MaxPaceSelectActivity : AppCompatActivity() {

    private lateinit var binding: MaxPaceSettingPageBinding
    private var maxPace: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MaxPaceSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val maxPaceSelectBar = binding.paceSettingSeekbar

        maxPaceSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(maxPaceSelectBar: SeekBar?, progress: Int, p2: Boolean) {
                val selectPaceValue = 20 - progress
                val realValue = selectPaceValue.toString()
                val maxPaceText = "$realValue' 00''/km"
                val paceValue = "$realValue' 00''"
                maxPace = selectPaceValue
                binding.maxPaceText.text = "$maxPaceText"
                binding.paceValueText.text = "$paceValue"

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.confirmBtn.setOnClickListener {



            val intent = Intent(this, MinPaceSelectActivity::class.java)
            intent.putExtra("max_pace", maxPace)
            startActivity(intent)
            finish()
        }

    }


}