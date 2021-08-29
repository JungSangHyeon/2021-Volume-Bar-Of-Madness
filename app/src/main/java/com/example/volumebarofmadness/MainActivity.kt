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
    private var time:Long = 200

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

        val xArr = arrayListOf(200f, 300f, 400f, 500f, 700f)
        val yArr = arrayListOf(100f, 200f, 300f)
        var xIndex = 0
        var yIndex =0
        Thread{
            while(true){
                runOnUiThread {
                    var x = xArr[0]
                    try{ x = xArr[xIndex++] }catch (e:IndexOutOfBoundsException){ xIndex=1 }
                    var y = yArr[0]
                    try{ y = yArr[yIndex++] }catch (e:IndexOutOfBoundsException){ yIndex=1 }

                    binding.ball.translationX = 0f
                    FireAnimation.start(binding.ball, 500, x, y)
                }
                Thread.sleep(500)

            }
        }.start()

    }
}