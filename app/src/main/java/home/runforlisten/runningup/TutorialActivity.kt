package home.runforlisten.runningup


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import home.runforlisten.runningup.databinding.TutorialMasterPageBinding



class TutorialActivity: AppCompatActivity() {

    private lateinit var binding: TutorialMasterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TutorialMasterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tutorialViewPager: ViewPager2 = binding.tutorialViewPager

        val adapter = object:FragmentStateAdapter(this){
            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> TutorialFragment1()
                    1 -> TutorialFragment2()
                    2 -> TutorialFragment3()
                    3 -> TutorialFragment4()
                    4 -> TutorialFragment5()
                    5 -> TutorialFragment6()
                    else -> throw IllegalStateException("Invalid position: $position")
                }
            }

            override fun getItemCount(): Int {
                return 6
            }
        }

        tutorialViewPager.adapter = adapter
        tutorialViewPager.isUserInputEnabled = true
        tutorialViewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                circleChangeColor(position)
            }
        })

    }

    private fun circleChangeColor(position: Int){



        when (position) {
            0 -> {binding.firstCircle.setBackgroundResource(R.drawable.green_circle)
                binding.secondCircle.setBackgroundResource(R.drawable.white_circle)
                binding.thirdCircle.setBackgroundResource(R.drawable.white_circle)
                binding.fourthCircle.setBackgroundResource(R.drawable.white_circle)
                binding.fifthCircle.setBackgroundResource(R.drawable.white_circle)
                binding.sixthCircle.setBackgroundResource(R.drawable.white_circle)
            }

            1 ->{binding.firstCircle.setBackgroundResource(R.drawable.green_circle)
                binding.secondCircle.setBackgroundResource(R.drawable.green_circle)
                binding.thirdCircle.setBackgroundResource(R.drawable.white_circle)
                binding.fourthCircle.setBackgroundResource(R.drawable.white_circle)
                binding.fifthCircle.setBackgroundResource(R.drawable.white_circle)
                binding.sixthCircle.setBackgroundResource(R.drawable.white_circle)
            }

            2 ->{binding.firstCircle.setBackgroundResource(R.drawable.green_circle)
                binding.secondCircle.setBackgroundResource(R.drawable.green_circle)
                binding.thirdCircle.setBackgroundResource(R.drawable.green_circle)
                binding.fourthCircle.setBackgroundResource(R.drawable.white_circle)
                binding.fifthCircle.setBackgroundResource(R.drawable.white_circle)
                binding.sixthCircle.setBackgroundResource(R.drawable.white_circle)
            }

            3 -> {binding.firstCircle.setBackgroundResource(R.drawable.green_circle)
                binding.secondCircle.setBackgroundResource(R.drawable.green_circle)
                binding.thirdCircle.setBackgroundResource(R.drawable.green_circle)
                binding.fourthCircle.setBackgroundResource(R.drawable.green_circle)
                binding.fifthCircle.setBackgroundResource(R.drawable.white_circle)
                binding.sixthCircle.setBackgroundResource(R.drawable.white_circle)
                }
            4 -> {binding.firstCircle.setBackgroundResource(R.drawable.green_circle)
                binding.secondCircle.setBackgroundResource(R.drawable.green_circle)
                binding.thirdCircle.setBackgroundResource(R.drawable.green_circle)
                binding.fourthCircle.setBackgroundResource(R.drawable.green_circle)
                binding.fifthCircle.setBackgroundResource(R.drawable.green_circle)
                binding.sixthCircle.setBackgroundResource(R.drawable.white_circle)
            }

            5 -> {binding.firstCircle.setBackgroundResource(R.drawable.green_circle)
                binding.secondCircle.setBackgroundResource(R.drawable.green_circle)
                binding.thirdCircle.setBackgroundResource(R.drawable.green_circle)
                binding.fourthCircle.setBackgroundResource(R.drawable.green_circle)
                binding.fifthCircle.setBackgroundResource(R.drawable.green_circle)
                binding.sixthCircle.setBackgroundResource(R.drawable.green_circle)}

        }


    }

}