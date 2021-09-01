package com.example.elonmars.presentation.view

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.elonmars.R

/** Главный экран приложения */
class HomeFragment : Fragment() {

    companion object {
        private const val ENTER_FADE_TIME = 1000
        private const val EXIT_FADE_TIME = 3000
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout = view.findViewById<RelativeLayout>(R.id.layout)
        startBackgroundAnimation(layout)
    }

    private fun startBackgroundAnimation(view: View) {
        view.setBackgroundResource(R.drawable.gradient_list)

        val animationDrawable: AnimationDrawable = view.background as AnimationDrawable

        animationDrawable.apply {
            setEnterFadeDuration(ENTER_FADE_TIME)
            setExitFadeDuration(EXIT_FADE_TIME)
            start()
        }
    }

    //TODO that's just one more variant for animation
//    private fun startBackgroundAnimation(view: View) {
//        val animator = ValueAnimator.ofInt(Color.BLUE, Color.RED, Color.YELLOW, Color.MAGENTA)
//
//        animator.setEvaluator(ArgbEvaluator())
//        animator.repeatCount = ValueAnimator.INFINITE
//        animator.repeatMode = ValueAnimator.REVERSE
//        animator.duration = 10000
//        animator.interpolator = LinearInterpolator()
//
//        animator.addUpdateListener {
//            val value = it.animatedValue as Int
//
//            view.setBackgroundColor(value)
//        }
//        animator.start()
//    }
}
