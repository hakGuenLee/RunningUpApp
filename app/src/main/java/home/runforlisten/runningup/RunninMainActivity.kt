package home.runforlisten.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.RunningMainPageBinding

class RunninMainActivity : AppCompatActivity() {

    private lateinit var binding: RunningMainPageBinding
    private var maxPace: Int = 0
    private var minPace: Int = 0
    private var maxVolume: Int = 0
    private var minVolume: Int = 0
    private var maxPaceMinutes: Int = 0
    private var maxPaceSeconds: Int = 0
    private var minPaceMinutes: Int = 0
    private var minPaceSeconds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunningMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        maxPace = intent.getIntExtra("max_pace", 0)
        minPace = intent.getIntExtra("min_pace", 0)
        maxPaceMinutes = intent.getIntExtra("max_pace_minutes",0)
        maxPaceSeconds = intent.getIntExtra("max_pace_seconds",0)
        minPaceMinutes = intent.getIntExtra("min_pace_minutes",0)
        minPaceSeconds = intent.getIntExtra("min_pace_seconds",0)
        maxVolume = intent.getIntExtra("max_volume", 0)
        minVolume = intent.getIntExtra("min_volume", 0)

        binding.maxVolume.text = "Max Volume : $maxVolume"
        binding.targetPace.text = "$maxPaceMinutes' $maxPaceSeconds''/km"
    }


}