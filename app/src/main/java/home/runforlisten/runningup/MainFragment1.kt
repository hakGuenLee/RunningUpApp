package home.runforlisten.runningup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import home.runforlisten.runningup.databinding.MainFragment1Binding


//메인화면 프래그먼트
class MainFragment1 : Fragment(R.layout.main_fragment_1) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment_1, container, false)
    }

    private lateinit var binding: MainFragment1Binding

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragment1Binding.bind(view)
    }

}