package home.runforlisten.runningup



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import home.runforlisten.runningup.databinding.TutorialMasterPageBinding



class TutorialActivity: AppCompatActivity() {

    private lateinit var binding: TutorialMasterPageBinding
    private lateinit var tutorialViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TutorialMasterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tutorialViewPager = binding.tutorialViewPager

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

        tutorialViewPager.setPageTransformer(null)

        tutorialViewPager.setPageTransformer { page, position ->
            page.translationX = position * -page.width.toFloat()
            page.alpha = 1 - Math.abs(position)
        }

        tutorialViewPager.offscreenPageLimit = 1


        tutorialViewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                circleChangeColor(position)
            }
        })

    }

    //뒤로가기 버튼 누르면 이전 프래그먼트로 이동
    override fun onBackPressed() {
      
        if (tutorialViewPager.currentItem > 0) {
            tutorialViewPager.setCurrentItem(tutorialViewPager.currentItem - 1, true)
        } else {
            super.onBackPressed()
        }
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