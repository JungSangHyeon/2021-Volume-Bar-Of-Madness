package com.example.volumebarofmadness

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.volumebarofmadness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frontVolum.drawable.level = 5000

//        var level = 0
//        var diff = 100
//        var maxAngle = -45
//        Thread{
//            while(true){
//                binding.frontVolum.drawable.level = level
//                binding.frontVolum.rotation = (maxAngle*level/10000).toFloat()
//                binding.backVolum.rotation = (maxAngle*level/10000).toFloat()
//                Log.d("ASD", ""+binding.frontVolum.rotation)
//                level+=diff
//                if(level==10000) diff=-100
//                else if(level==0) diff=100
//                Thread.sleep(10)
//            }
//        }.start()

        Thread{
            while(true){
                runOnUiThread {
                    ObjectAnimator.ofFloat(binding.ball, "translationX", 500f).apply {
                        duration = 2000
                        start()
                    }.addListener(XFinishedListener())
                    ObjectAnimator.ofFloat(binding.ball, "translationY", -100f).apply {
                        duration = 1000
                        start()
                    }.addListener(YFinishedListener())
                }
                Thread.sleep(4000)
            }
        }.start()

    }

    inner class YFinishedListener() : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            ObjectAnimator.ofFloat(binding.ball, "translationY", 0f).apply {
                duration = 1000
                start()
            }
        }
    }
    inner class XFinishedListener() : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            ObjectAnimator.ofFloat(binding.ball, "translationX", 0f).apply {
                duration = 2000
                start()
            }
        }
    }
}