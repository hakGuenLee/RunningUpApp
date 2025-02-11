package home.runforlisten.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.RunningMainPageBinding

class RunninMainActivity : AppCompatActivity() {

    private lateinit var binding: RunningMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunningMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}