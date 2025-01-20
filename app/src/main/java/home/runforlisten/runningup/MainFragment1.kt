package home.runforlisten.runningup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import home.runforlisten.runningup.databinding.MainFragment1Binding

class MainFragment1 : Fragment(R.layout.main_fragment_1) {

    private lateinit var binding: MainFragment1Binding

    companion object {
        private const val TARGET_PACE_KEY = "target_pace"
        private const val MAX_VOLUME_VALUE = "maxVolume"

        // Fragment를 생성하면서 targetPace를 arguments로 전달하는 메서드
        fun newInstance(targetPace: Double, maxVolume: Int): MainFragment1 {
            val fragment = MainFragment1()
            val args = Bundle()
            args.putDouble(TARGET_PACE_KEY, targetPace)
            args.putInt(MAX_VOLUME_VALUE, maxVolume)
            fragment.arguments = args
            return fragment
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

        // arguments에서 targetPace 값을 가져와서 텍스트로 출력
        val targetPace = arguments?.getDouble(TARGET_PACE_KEY) ?: 0.0
        val maxVolume = arguments?.getInt(MAX_VOLUME_VALUE) ?: 0
        binding.targetPace.text = "$targetPace /km"
        binding.maxVolume.text = "Max Volume : $maxVolume"
    }
}
