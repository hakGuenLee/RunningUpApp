package home.runforlisten.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.VolumeSettingPageBinding

class VolumeSelectActivity : AppCompatActivity() {

    private lateinit var binding: VolumeSettingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VolumeSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}