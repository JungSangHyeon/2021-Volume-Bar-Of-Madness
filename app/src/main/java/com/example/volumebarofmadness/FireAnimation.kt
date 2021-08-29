package com.example.volumebarofmadness

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


class FireAnimation {

    companion object {
        fun start(target: View, duration: Long, newX: Float, height: Float) {
            val animationSet = AnimatorSet()

            val translationX = ObjectAnimator.ofFloat(target, "translationX", newX)
            translationX.duration = duration

            val translationY = ObjectAnimator.ofFloat(target, "translationY", -height)
            translationY.duration = duration/2
            translationY.addListener(YFinishedListener(target, duration))

            animationSet.playTogether(translationX, translationY)
            animationSet.start()
        }

        class YFinishedListener(private val target: View, private val duration: Long) : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                val translationY = ObjectAnimator.ofFloat(target, "translationY", 0f)
                translationY.duration = duration/2
                translationY.start()
            }
        }
    }
}