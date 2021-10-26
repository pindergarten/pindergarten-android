package com.example.pindergarten_android

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pindergarten_android.PreferenceManager.setString
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

        var accessToken = PreferenceManager.getString(this, "accessToken")

        if (accessToken != null) {
            Log.d("accessToken",accessToken)
        }
        else{
            Log.d("accessToken", "accessToken 없음")
        }


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


}
