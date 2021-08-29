package com.example.volumebarofmadness

import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import com.example.volumebarofmadness.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.math.tan


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frontVolum.drawable.level = 0
        binding.clickArea.setOnTouchListener(FireListener())






    }

    var level = 0
    var maxAngle = -45
    var chargingThread: Thread? = null
    var reloadingThread: Thread? = null
    var charging = false

    inner class FireListener : View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event!!.action == MotionEvent.ACTION_DOWN){
                    reloadingThread?.interrupt()
                    startCharging()
                    charging = true
                    return true
                }else if (event!!.action == MotionEvent.ACTION_UP && charging){
                    chargingThread?.interrupt()
                    startReloading()
                    charging = false
                    return true
                }
            return false // no handle touch event
        }
    }

    private fun startCharging() {
        chargingThread = Thread{
            try {
                while(level!=10000){
                    binding.frontVolum.drawable.level = level
                    binding.frontVolum.rotation = (maxAngle*level/10000).toFloat()
                    binding.backVolum.rotation = (maxAngle*level/10000).toFloat()
                    level+=100
                    Thread.sleep(10)
                }
            }catch (e: InterruptedException){
            }
        }
        chargingThread?.start()
    }

    private fun startReloading() {
        reloadingThread = Thread{
            try {
                Log.d("ASD", "Fire LEVEL : $level")
                runOnUiThread {
                    binding.ball.translationX = 0f
                    val x = (binding.bar.width*level/10000).toFloat() + (binding.bar.x - binding.ball.x) - binding.ball.width/2
                    var rad = abs(maxAngle * level / 10000) * Math.PI/360;
                    val y = (tan(rad) * x).toFloat()
                    FireAnimation.start(binding.ball, 500, x, y)
                }

                val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * level / 10000,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                )
                Log.e("VOLUME", "vol: " + (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * level / 10000))
                Log.e("MAX VOLUME", "vol: " + (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)))

                while(level!=0){ // Reset Speaker & Wait Ball Fire Animation Finish
                    binding.frontVolum.drawable.level = level
                    binding.frontVolum.rotation = (maxAngle*level/10000).toFloat()
                    binding.backVolum.rotation = (maxAngle*level/10000).toFloat()
                    level-=100
                    Thread.sleep(10)
                }
            }catch (e: InterruptedException){
            }
        }
        reloadingThread?.start()
    }

    fun autoPlay(){
        Thread {
            while (true) {
                var level = 0
                var maxAngle = -45
                for (i in 0..100 step (10)) {
                    for (j in 0..i) {
                        binding.frontVolum.drawable.level = level
                        binding.frontVolum.rotation = (maxAngle * level / 10000).toFloat()
                        binding.backVolum.rotation = (maxAngle * level / 10000).toFloat()
                        level += 100
                        Thread.sleep(10)
                    }
                    // Fire
                    Log.d("ASD", "Fire LEVEL : $level")
                    runOnUiThread {
                        binding.ball.translationX = 0f
                        val x =
                            (binding.bar.width * level / 10000).toFloat() + (binding.bar.x - binding.ball.x) - binding.ball.width / 2
                        var rad = abs(maxAngle * level / 10000) * Math.PI / 360;
                        val y = (tan(rad) * x).toFloat()
                        FireAnimation.start(binding.ball, 500, x, y)
                    }

                    // Reset
                    for (j in 0..i) {
                        binding.frontVolum.drawable.level = level
                        binding.frontVolum.rotation = (maxAngle * level / 10000).toFloat()
                        binding.backVolum.rotation = (maxAngle * level / 10000).toFloat()
                        level -= 100
                        Thread.sleep(10)
                    }
                    // Wait Fire Finish
                    Thread.sleep(500)
                }
            }
        }.start()
    }
}
