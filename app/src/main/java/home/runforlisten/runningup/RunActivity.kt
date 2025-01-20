package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import home.runforlisten.runningup.databinding.RunningupMainBinding

// 메인화면 엑티비티
class RunActivity : AppCompatActivity() {

    private lateinit var binding: RunningupMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RunningupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val targetPace = intent.getDoubleExtra("maxpace", 0.0)
        val targetPace2 = intent.getDoubleExtra("minpace", 0.0)
        val maxVolume = intent.getIntExtra("maxVolume", 0)
        val minVolume = intent.getIntExtra("minVolume", 0)

        // 서비스에 페이스와 맥스볼륨 값을 전달
        val serviceIntent = Intent(this, MainService::class.java)
        serviceIntent.putExtra("maxpace", targetPace)
        serviceIntent.putExtra("minpace", targetPace2)
        serviceIntent.putExtra("maxVolume", maxVolume)
        serviceIntent.putExtra("minVolume", minVolume)
        startService(serviceIntent)

       val viewPager: ViewPager2 = binding.viewPager

        // 화면 스와이프 처리
        val adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                // 위치에 따라 다른 Fragment를 반환
                return when (position) {
                    0 -> MainFragment3()
                    1 -> MainFragment1.newInstance(targetPace,maxVolume)
                    2 -> MainFragment2()
                    else -> throw IllegalArgumentException("Invalid position")
                }
            }

            override fun getItemCount(): Int {
                return 3 // 총 3개의 프래그먼트
            }
        }


        // Adapter 설정
      viewPager.adapter = adapter
//
//        // 초기 화면을 MainFragment1으로 설정
       viewPager.setCurrentItem(1, false)  // MainFragment1을 첫 번째 페이지로 설정

    }

}
