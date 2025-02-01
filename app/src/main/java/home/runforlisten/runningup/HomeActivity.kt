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


        //하단의 아이콘을 누르면 pace 선택 메뉴 화면으로 넘어가기
        binding.runStartBtn.setOnClickListener {
           val intent = Intent(this, PaceActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}