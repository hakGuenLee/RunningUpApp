package home.test.runningup

import android.content.Context
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat

// 권한 처리 객체
class PermissionManager(private val context: Context) {

    private val REQUEST_PERMISSIONS = 1
    val permissionList = mutableMapOf<String, String>()

    init {
        permissionList["location"] = Manifest.permission.ACCESS_COARSE_LOCATION
        permissionList["fineLocation"] = Manifest.permission.ACCESS_FINE_LOCATION

        // Android 13 이상에서는 READ_MEDIA_AUDIO 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList["audio"] = Manifest.permission.READ_MEDIA_AUDIO
        } else {
            // Android 13 이하에서는 READ_EXTERNAL_STORAGE 요청
            permissionList["audio"] = Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    // 권한 허용 여부를 체크하고, 거부된 권한만 다시 요청
    fun permissionGrantChecker() {
        val permissionDenied = permissionList.values.filter {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
        }

        if (permissionDenied.isNotEmpty()) {
            Handler().postDelayed({
                val intent = Intent(context, GrantActivity::class.java)
                context.startActivity(intent)
                if (context is MainActivity) {
                    context.finish()
                }
            }, 980)
        } else {
            navigateToFaceSelectActivity()
        }
    }

    // 권한 요청 메소드: 거부된 권한만 다시 요청
    fun permissionRequester() {
        val permissionsToRequest = permissionList.values.filter {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
        }

        if (permissionsToRequest.isNotEmpty()) {
            (context as? GrantActivity)?.requestPermissions(
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS
            )
        } else {
//            Toast.makeText(context, "모든 권한이 이미 허용되었습니다.", Toast.LENGTH_SHORT).show()
            navigateToFaceSelectActivity()
        }
    }

    // 권한 요청 결과 처리
    fun handleRequestPermissionResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS) {
            //모든 권한의 허용 여부를 담는 변수
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

            if (allGranted) {
                // 모든 권한이 허용된 경우
                navigateToFaceSelectActivity()
            } else {
                // 거부된 권한이 있는 경우 처리
                val shouldShowRationale = permissionList.values.any { permission ->
                    androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                        context as GrantActivity,
                        permission
                    )
                }

                if (shouldShowRationale) {
                    // 단순 거부: 다시 요청
                    Toast.makeText(context, "권한이 거부되었습니다. 다시 요청합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // "다시 묻지 않음" 선택됨: 설정 화면으로 이동 안내
                    showSettingsDialog()
                }
            }
        }
    }

    // 설정 화면 이동 안내
    private fun showSettingsDialog() {
        val dialog = android.app.AlertDialog.Builder(context)
            .setTitle("권한 필요")
            .setMessage("앱을 사용하려면 권한 설정이 필요합니다. 설정 화면에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = android.net.Uri.fromParts("package", context.packageName, null)
                context.startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    // 권한이 허용되었을 때 HomeActivity 전환
    fun navigateToFaceSelectActivity() {
        Handler().postDelayed({
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }, 970) // 0.97초 지연
    }
}
