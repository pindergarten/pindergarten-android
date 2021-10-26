package com.example.pindergarten_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pindergarten_android.databinding.ActivityLoginBinding
import com.example.pindergarten_android.databinding.ActivitySplashBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl( "http://13.125.184.176:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@LoginActivity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        PreferenceManager.setString(this, "accessToken", "jihyun")



    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.login->{
                //서버연결
            }
            R.id.join->{
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.findPwd->{
                val intent = Intent(this, FindPwdActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

}