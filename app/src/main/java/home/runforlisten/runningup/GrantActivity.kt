package home.runforlisten.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.GrantPageBinding

class GrantActivity : AppCompatActivity() {

    private lateinit var binding: GrantPageBinding
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GrantPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionManager = PermissionManager(this)


        binding.grantBtn.setOnClickListener {
            permissionManager.permissionRequester()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.handleRequestPermissionResult(requestCode, grantResults)
    }

}