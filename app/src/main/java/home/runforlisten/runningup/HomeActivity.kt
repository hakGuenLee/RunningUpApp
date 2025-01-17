package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.HomePageBinding

class HomeActivity: AppCompatActivity() {

    private lateinit var binding: HomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.runStartBtn.setOnClickListener {
           val intent = Intent(this, PaceActivity::class.java)
            startActivity(intent)
        }
    }


}