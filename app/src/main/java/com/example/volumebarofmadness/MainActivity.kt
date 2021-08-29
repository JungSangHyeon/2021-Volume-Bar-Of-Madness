package com.example.volumebarofmadness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.volumebarofmadness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frontVolum.drawable.level = 5000

        var level = 0
        var diff = 100
        var maxAngle = -45
        Thread{
            while(true){
                binding.frontVolum.drawable.level = level
                binding.frontVolum.rotation = (maxAngle*level/10000).toFloat()
                binding.backVolum.rotation = (maxAngle*level/10000).toFloat()
                Log.d("ASD", ""+binding.frontVolum.rotation)
                level+=diff
                if(level==10000) diff=-100
                else if(level==0) diff=100
                Thread.sleep(10)
            }
        }.start()
    }
}