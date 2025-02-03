package home.runforlisten.runningup

import android.content.Context
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// 권한 처리 객체
class PermissionManager(private val context: Context) {

//    private val REQUEST_PERMISSIONS = 1
////    val permissionList = mutableMapOf<String, String>()
//
//
//    private val permissionList = listOf(
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.READ_EXTERNAL_STORAGE // 예시로 하나 더 추가
//    )
//
//    private var permissionStatus = mutableMapOf<String, Int>()
//
//    //MainActivity 실행 시(스플래쉬 화면) 항상 권한 체크
//    fun permissionGrantChecker(){
//
//        //각 권한들에 대한 status를 담은 맵
//        val permissionStatus = permissionList.map { permission ->
//            permission to ContextCompat.checkSelfPermission(context, permission)
//        }.toMap()
//
//        // 권한이 거부되었거나, '이번만 허용' 상태인 경우가 있는지 확인
//        val permissionDenyStatus = permissionStatus.filter {
//
//            it.value == PackageManager.PERMISSION_DENIED ||
//                    ActivityCompat.shouldShowRequestPermissionRationale(context as MainActivity, it.key)
//        }
//
//        // 거부된 권한 또는 '이번만 허용' 상태가 있으면 권한 요청 화면으로 이동
//        if (permissionDenyStatus.isNotEmpty()) {
//
//            Handler().postDelayed({
//                val intent = Intent(context, GrantActivity::class.java)
//                context.startActivity(intent)
//                if (context is MainActivity) {
//                    context.finish()
//                }
//            }, 980)
//        } else {
//            // 모든 권한이 허용된 경우
//            HomeActivityMover()
//        }
//    }
//
//
//    // 권한이 허용되었을 때 HomeActivity 전환
//    fun HomeActivityMover() {
//        Handler().postDelayed({
//            val intent = Intent(context, HomeActivity::class.java)
//            context.startActivity(intent)
//        }, 970) // 0.97초 지연
//    }
//
//
//    // 권한 요청 activity에서 호출하는 권한 허용 요청 메소드
//    fun permissionRequester(){
//
//        val permissionsToRequest = permissionList.filter {
//            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED ||
//                    ActivityCompat.shouldShowRequestPermissionRationale(context as MainActivity, it)
//        }
//
//        if (permissionsToRequest.isNotEmpty()) {
//            // 권한 요청
//            (context as? GrantActivity)?.requestPermissions(
//                permissionsToRequest.toTypedArray(),
//                REQUEST_PERMISSIONS
//            )
//        } else {
//            HomeActivityMover()
//        }
//
//
//    }




//    private val requestStatus = context.getSharedPreferences("permission_prefs", Context.MODE_PRIVATE)
//
//    init {
//        permissionList["location"] = Manifest.permission.ACCESS_COARSE_LOCATION
//        permissionList["fineLocation"] = Manifest.permission.ACCESS_FINE_LOCATION
//
//        // Android 13 이상에서는 READ_MEDIA_AUDIO 요청
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            permissionList["audio"] = Manifest.permission.READ_MEDIA_AUDIO
//        } else {
//            // Android 13 이하에서는 READ_EXTERNAL_STORAGE 요청
//            permissionList["audio"] = Manifest.permission.READ_EXTERNAL_STORAGE
//        }
//    }
//
//    // 권한 허용 여부를 체크하고, 거부된 권한만 다시 요청
//    fun permissionGrantChecker() {
//        val permissionDenied = permissionList.values.filter {
//            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
//        }
//
//        if (permissionDenied.isNotEmpty()) {
//
//            requestStatus.edit().putBoolean("isPermissionDenied", true).apply()
//
//            Handler().postDelayed({
//                val intent = Intent(context, GrantActivity::class.java)
//                context.startActivity(intent)
//                if (context is MainActivity) {
//                    context.finish()
//                }
//            }, 980)
//        } else {
//            navigateToFaceSelectActivity()
//        }
//    }
//
//    // 권한 요청 메소드: 거부된 권한만 다시 요청
//    fun permissionRequester() {
//        val permissionsToRequest = permissionList.values.filter {
//            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
//        }
//
//        if (permissionsToRequest.isNotEmpty()) {
//            (context as? GrantActivity)?.requestPermissions(
//                permissionsToRequest.toTypedArray(),
//                REQUEST_PERMISSIONS
//            )
//        } else {
////            Toast.makeText(context, "모든 권한이 이미 허용되었습니다.", Toast.LENGTH_SHORT).show()
//            navigateToFaceSelectActivity()
//        }
//    }
//
//    // 권한 요청 결과 처리
//    fun handleRequestPermissionResult(
//        requestCode: Int,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_PERMISSIONS) {
//            //모든 권한의 허용 여부를 담는 변수
//            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
//
//            if (allGranted) {
//                // 모든 권한이 허용된 경우
//                navigateToFaceSelectActivity()
//            } else {
//                // 거부된 권한이 있는 경우 처리
//                val shouldShowRationale = permissionList.values.any { permission ->
//                    androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
//                        context as GrantActivity,
//                        permission
//                    )
//                }
//
//                if (shouldShowRationale) {
//                    // 단순 거부: 다시 요청
//                    Toast.makeText(context, "권한이 거부되었습니다. 다시 요청합니다.", Toast.LENGTH_SHORT).show()
//                } else {
//                    // "다시 묻지 않음" 선택됨: 설정 화면으로 이동 안내
//                    showSettingsDialog()
//                }
//            }
//        }
//    }
//
//    // 설정 화면 이동 안내
//    private fun showSettingsDialog() {
//        val dialog = android.app.AlertDialog.Builder(context)
//            .setTitle("권한 필요")
//            .setMessage("앱을 사용하려면 권한 설정이 필요합니다. 설정 화면에서 권한을 허용해주세요.")
//            .setPositiveButton("설정으로 이동") { _, _ ->
//                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = android.net.Uri.fromParts("package", context.packageName, null)
//                context.startActivity(intent)
//            }
//            .setNegativeButton("취소") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .create()
//        dialog.show()
//    }


}
