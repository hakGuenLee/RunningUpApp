
package home.runforlisten.runningup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import home.runforlisten.runningup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 권한 목록
    private val permissionList = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,

        // Android 13 이상에서는 READ_MEDIA_AUDIO 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            // Android 13 이하에서는 READ_EXTERNAL_STORAGE 요청
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 권한 상태 확인
        checkPermissions()
    }

    private fun checkPermissions() {
        // 각 권한에 대해 checkSelfPermission을 통해 권한 상태 확인
        val permissionDenied = permissionList.any {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }

        // 권한에 따라 이동할 액티비티 결정
        val targetActivity = if (permissionDenied) {
            GrantActivity::class.java
        } else {
            HomeActivity::class.java
        }

        // 액티비티 이동
        moveToActivity(targetActivity)
    }

    private fun moveToActivity(activityClass: Class<*>) {
        Handler().postDelayed({
            val intent = Intent(this, activityClass)
            startActivity(intent)
            finish() // 현재 MainActivity 종료
        }, 980)
    }

}