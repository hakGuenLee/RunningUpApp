package home.runforlisten.runningup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.runforlisten.runningup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionManager = PermissionManager(this)
        permissionManager.permissionGrantChecker()

    }
}