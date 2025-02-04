package home.runforlisten.runningup

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import home.runforlisten.runningup.databinding.GrantPageBinding



class GrantActivity : AppCompatActivity() {

    private lateinit var binding: GrantPageBinding
    private lateinit var permissionManager: PermissionManager
    private lateinit var firstRunCheckData: SharedPreferences
    private val REQUEST_PERMISSIONS = 1
    private var firstUserChecker = 0

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
        binding = GrantPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        permissionManager = PermissionManager(this)


        firstRunCheckData = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        firstUserChecker = firstRunCheckData.getInt("firstUserChecker", 0)

        binding.grantBtn.setOnClickListener {
            permissionRequester()
        }

    }

    // 권한 요청 함수
    private fun permissionRequester() {
        // 거부된 권한을 찾기
        val permissionsToRequest = permissionList.filter {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED
        }

        // 요청할 권한이 있으면 요청
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_PERMISSIONS)
        } else {
            // 모든 권한이 허용된 경우 바로 HomeActivity로 이동
            navigateToHomeActivity()
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSIONS) {
            // 모든 권한이 허용되었는지 확인
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

            if (allPermissionsGranted) {
                // 모든 권한이 허용되었고 앱 최초 실행자라면 튜토리얼 액티비티로 전환
                // 모든 권한이 허용되었고 앱 최초 실행자가 아니면 홈 액티비티로 전환

                if(firstUserChecker == 0){
                    firstUserChecker = 1
                    saveFirstUserChecker()
                    navigateTutorialActivity()

                }else{
                    navigateToHomeActivity()
                }

            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "권한을 허용해야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                binding.grantBtn.isEnabled = true
            }
        }
    }

    private fun navigateTutorialActivity(){
        val intent = Intent(this, TutorialActivity::class.java)
        startActivity(intent)
        finish()
    }

    // HomeActivity로 이동하는 함수
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // 현재 Activity 종료
    }

    // 첫 번째 사용자인지 여부를 SharedPreferences에 저장
    private fun saveFirstUserChecker() {
        val editor = firstRunCheckData.edit()
        editor.putInt("firstUserChecker", firstUserChecker)
        editor.apply()
    }



}