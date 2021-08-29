package com.example.volumebarofmadness

import android.media.AudioManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import com.example.volumebarofmadness.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.math.tan


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val maxAngle = -45
    private val maxLevel = 10000
    private val diffLevel = 100
    private val levelChargingInterval = 10L // ms
    private val ballMovingTime = 500L // ms

    private var level = 0
    private var chargingThread: Thread? = null
    private var reloadingThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frontVolum.drawable.level = 0
        binding.clickArea.setOnTouchListener(FireListener())
        binding.bar.viewTreeObserver.addOnGlobalLayoutListener(VolumeBarInitializeListener())
    }

    inner class VolumeBarInitializeListener : OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            binding.bar.viewTreeObserver.removeOnGlobalLayoutListener(this)
            val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
            val nowVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

            val x = (binding.bar.width*nowVolume/audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toFloat() + (binding.bar.x - binding.ball.x) - binding.ball.width/2
            FireAnimation.start(binding.ball, 0, x, 0f)
        }
    }

    inner class FireListener : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event!!.action == MotionEvent.ACTION_DOWN) {
                reloadingThread?.interrupt()
                startCharging()
                return true
            } else if (event.action == MotionEvent.ACTION_UP) {
                chargingThread?.interrupt()
                fireAndReload()
                return true
            }
            return false // no handle touch event
        }
    }

    private fun startCharging() {
        chargingThread = Thread{
            try {
                while(level!=maxLevel){
                    binding.frontVolum.drawable.level = level
                    binding.frontVolum.rotation = (maxAngle*level/maxLevel).toFloat()
                    binding.backVolum.rotation = (maxAngle*level/maxLevel).toFloat()
                    level+=diffLevel
                    Thread.sleep(levelChargingInterval)
                }
            }catch (e: InterruptedException){}
        }
        chargingThread?.start()
    }

    private fun fireAndReload() {
        reloadingThread = Thread{
            try {
                runOnUiThread {
                    binding.ball.translationX = 0f
                    val x = (binding.bar.width*level/maxLevel).toFloat() + (binding.bar.x - binding.ball.x) - binding.ball.width/2
                    val rad = abs(maxAngle * level / maxLevel) * Math.PI/360;
                    val y = (tan(rad) * x).toFloat()
                    FireAnimation.start(binding.ball, ballMovingTime, x, y)
                }

                val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * level / maxLevel,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                )

                while(level!=0){ // Reset Speaker & Wait Ball Fire Animation Finish
                    binding.frontVolum.drawable.level = level
                    binding.frontVolum.rotation = (maxAngle*level/maxLevel).toFloat()
                    binding.backVolum.rotation = (maxAngle*level/maxLevel).toFloat()
                    level-=diffLevel
                    Thread.sleep(levelChargingInterval)
                }
            }catch (e: InterruptedException){ }
        }
        reloadingThread?.start()
    }
}
