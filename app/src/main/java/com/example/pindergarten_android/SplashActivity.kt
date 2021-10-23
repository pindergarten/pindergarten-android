package com.example.pindergarten_android

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pindergarten_android.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash )
        binding.vm = PindergartenViewModel()

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()


    }
}
