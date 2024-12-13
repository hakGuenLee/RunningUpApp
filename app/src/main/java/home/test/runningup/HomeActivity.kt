package home.test.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.test.runningup.databinding.HomePageBinding

class HomeActivity: AppCompatActivity() {

    private lateinit var binding: HomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}