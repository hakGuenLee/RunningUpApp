package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.HomePageBinding
import home.runforlisten.runningup.databinding.TutorialTestBinding

class TutorialActivity: AppCompatActivity() {

    private lateinit var binding: TutorialTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TutorialTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }



}