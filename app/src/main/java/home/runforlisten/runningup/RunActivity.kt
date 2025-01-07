package home.runforlisten.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import home.runforlisten.runningup.databinding.RunningupMainBinding

//메인화면 엑티비티
class RunActivity : AppCompatActivity() {

    private lateinit var binding: RunningupMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunningupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager : ViewPager2 = binding.viewPager

        val minVolume = intent?.getIntExtra("minVolume", 0)


    }







}