package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.HomePageBinding

class HomeActivity: AppCompatActivity() {

    private lateinit var binding: HomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //사용자가 뒤로가기 터치했을 때 앱을 완전히 종료시키는 콜백 함수
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)


        //하단의 아이콘을 누르면 pace 선택 메뉴 화면으로 넘어가기
//        binding.runStartBtn.setOnClickListener {
//           val intent = Intent(this, PaceActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }



}