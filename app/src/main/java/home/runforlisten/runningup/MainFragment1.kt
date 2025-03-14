package home.runforlisten.runningup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import home.runforlisten.runningup.databinding.MainFragment1Binding

class MainFragment1 : Fragment(R.layout.main_fragment_1), TimeHandler.TimerCallback {

    private lateinit var binding: MainFragment1Binding
    private var timeHandler : TimeHandler? = null
    private var totalDistance = 0.0 // 총 이동 거리

    //생성자
    companion object {
        private const val TARGET_PACE_KEY = "maxPace"
        private const val TARGET_PACE_KEY2 = "minPace"
        private const val MAX_VOLUME_VALUE = "maxVolume"
        private const val MIN_VOLUME_VALUE = "minVolume"

        // Fragment를 생성하면서 targetPace를 arguments로 전달하는 메서드
        fun newInstance(targetPace: Int, targetPace2:Int, maxVolume: Int, minVolume: Int): MainFragment1 {
            val fragment = MainFragment1()
            val args = Bundle()
            args.putInt(TARGET_PACE_KEY, targetPace)
            args.putInt(TARGET_PACE_KEY2, targetPace2)
            args.putInt(MAX_VOLUME_VALUE, maxVolume)
            args.putInt(MIN_VOLUME_VALUE, minVolume)
            fragment.arguments = args
            return fragment
        }
    }

    //MainService와 상호작용 하면서, 서비스단에서 넘어오는 값으로 ui 수정
    private val distanceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            totalDistance = intent.getDoubleExtra("distance", 0.0)
            var pace = intent.getStringExtra("pace")
            var currentVolume = intent.getIntExtra("currentVolume",0)
            uiUpdater(pace,currentVolume)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragment1Binding.bind(view)

        //타이머 객체
        timeHandler = TimeHandler(this)

        // arguments에서 targetPace 값을 가져와서 텍스트로 출력
        val maxPace = arguments?.getInt(TARGET_PACE_KEY) ?: 0
        val minPace = arguments?.getInt(TARGET_PACE_KEY2) ?: 0
        val maxVolume = arguments?.getInt(MAX_VOLUME_VALUE) ?: 0
        val minVolume = arguments?.getInt(MIN_VOLUME_VALUE) ?: 0
        binding.targetPace.text = "$maxPace 분 ~ $minPace 분 /km"
        binding.maxVolume.text = "Max Volume : $maxVolume"
        
        //시작 버튼을 눌렀을 때 서비스 시작
        binding.startBtn.setOnClickListener {

            //서비스 시작
            startService(maxPace, minPace, maxVolume, minVolume)
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                distanceReceiver, IntentFilter("ACTION_DISTANCE_UPDATED")
            )
            timeHandler!!.startTimer()


        }

        binding.stopBtn.setOnClickListener {

            timeHandler!!.stopAllTimer()

            requireContext().stopService(Intent(requireContext(), MainService::class.java))
            binding.distance.text = "0.0"

            // 브로드캐스트 리시버 해제
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(distanceReceiver)

        }




    }

    //서비스단에서 넘어오는 값들을 화면에 뿌리는 메서드
    private fun uiUpdater(pace: String?, currentVolume: Int) {
        binding.distance.text = "${"%.2f".format(totalDistance / 1000)}"
        binding.paceStatusText.text = "$pace"
        binding.volumeStatus.text = "$currentVolume"
    }

    // 서비스에 페이스와 맥스볼륨 값을 전달
    private fun startService(targetPace: Int, targetPace2:Int, maxVolume: Int, minVolume: Int) {
        val serviceIntent = Intent(requireContext(), MainService::class.java)
        serviceIntent.putExtra("maxpace", targetPace)
        serviceIntent.putExtra("minpace", targetPace2) // 최소 페이스도 전달
        serviceIntent.putExtra("maxVolume", maxVolume)
        serviceIntent.putExtra("minVolume", minVolume) // 최소 볼륨도 전달
        requireContext().startService(serviceIntent)
    }

    override fun onTick(time: String) {
        binding.timer.text = time
    }

    override fun onTimerStop() {
        TODO("Not yet implemented")
    }


}
