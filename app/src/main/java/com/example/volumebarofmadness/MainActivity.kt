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
import kotlin.math.abs
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frontVolum.drawable.level = 5000

        Thread{
            while(true){
                var level = 0
                var maxAngle = -45
                for(i in 0..100 step(10)) {
                    for(j in 0..i){
                        binding.frontVolum.drawable.level = level
                        binding.frontVolum.rotation = (maxAngle*level/10000).toFloat()
                        binding.backVolum.rotation = (maxAngle*level/10000).toFloat()
                        level+=100
                        Thread.sleep(10)
                    }
                    // Fire
                    Log.d("ASD", "Fire LEVEL : $level")
                    runOnUiThread {
                        binding.ball.translationX = 0f
                        val x = (binding.bar.width*level/10000).toFloat() + (binding.bar.x - binding.ball.x) - binding.ball.width/2
                        var rad = abs(maxAngle*level/10000) * Math.PI/360;
                        val y = (tan(rad) * x).toFloat()
                        FireAnimation.start(binding.ball, 500, x, y)
                    }

                    // Reset
                    for(j in 0..i){
                        binding.frontVolum.drawable.level = level
                        binding.frontVolum.rotation = (maxAngle*level/10000).toFloat()
                        binding.backVolum.rotation = (maxAngle*level/10000).toFloat()
                        level-=100
                        Thread.sleep(10)
                    }
                    // Wait Fire Finish
                    Thread.sleep(500)
                }
            }
        }.start()

    }
}