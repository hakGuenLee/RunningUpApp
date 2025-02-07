package home.runforlisten.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.PaceSettingPageBinding

class PaceSelectActivity : AppCompatActivity() {

    private lateinit var binding: PaceSettingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaceSettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}