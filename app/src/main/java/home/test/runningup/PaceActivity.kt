package home.test.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.test.runningup.databinding.PaceSelectBinding

class PaceActivity : AppCompatActivity() {

    private lateinit var binding: PaceSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaceSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}