package com.example.pindergarten_android

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
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
        binding.activity =  this@SplashActivity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()
        //전체화면
        hideNavigationBar()


        var accessToken = PreferenceManager.getString(this, "accessToken")
        if (accessToken != null) {
            Log.d("accessToken",accessToken)
        }
        else{
            Log.d("accessToken", "accessToken 없음")
        }

        var joinBtn : ImageButton = findViewById(R.id.join)
        var loginBtn : ImageButton = findViewById(R.id.login)
        joinBtn.visibility= View.INVISIBLE
        loginBtn.visibility= View.INVISIBLE

        Handler().postDelayed({
            joinBtn.visibility= View.VISIBLE
            loginBtn.visibility= View.VISIBLE
        },3000L)

        if (accessToken != null) {
            //토큰 있을경우 -> 서버전달/정보 담아서 메인화면 (viewModel)
            Log.i("accessToken",accessToken)
            /*
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            finish()

             */
        }
        else{
            //토큰 없을경우 -> 로그인/회원가입
            /*
            val intent = Intent(this, LoginActivty::class.java)
            startActivity(intent)
            finish()

             */
        }

    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.login->{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.join->{
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

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
