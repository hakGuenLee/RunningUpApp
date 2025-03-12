package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.RunResultPageBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding:RunResultPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunResultPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.saveBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }



        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ResultActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        onBackPressedDispatcher.addCallback(this@ResultActivity, callback)

    }
}