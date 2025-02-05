package home.runforlisten.runningup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class TutorialFragment6 : Fragment(R.layout.tutorial_fragment_6) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tutorial_fragment_6, container, false)
        val confirmBtn : ImageView = view.findViewById(R.id.confirm_btn)

        confirmBtn.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
        }

        return view
    }



}