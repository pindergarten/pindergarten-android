package com.example.pindergarten_android

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.pindergarten_android.databinding.ActivityOnboardingBinding
import me.relex.circleindicator.CircleIndicator

class OnboardingActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        val binding: ActivityOnboardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding )
        binding.vm = PindergartenViewModel()
        binding.activity =  this@OnboardingActivity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        //전체화면
        hideNavigationBar()


        var pageradapter = OnboardingAdapter(this)
        var pager = findViewById<ViewPager>(R.id.view_pager)
        pager.adapter = pageradapter

        var indicator : CircleIndicator = findViewById(R.id.indicator)
        indicator.setViewPager(pager)


    }

    fun btnClick(view : View){

    }

    private fun hideNavigationBar() {
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
        if (isImmersiveModeEnabled) {
            Log.d(TAG, "Turning immersive mode mode off. ")
        } else {
            Log.d(TAG, "Turning immersive mode mode on.")
        }
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions
    }


}
