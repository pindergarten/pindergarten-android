package com.example.pindergarten_android

import android.content.ContentValues
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplashActivity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

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


        var jwt = PreferenceManager.getString(this, "jwt")

        var joinBtn : ImageButton = findViewById(R.id.join)
        var loginBtn : ImageButton = findViewById(R.id.login)
        joinBtn.visibility= View.INVISIBLE
        loginBtn.visibility= View.INVISIBLE


        if (jwt != null) {

            //토큰 유효성 검사
            apiService.autoLoginAPI(jwt)?.enqueue(object : Callback<Post?> {
                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.d(ContentValues.TAG, "실패 : {${t}}")
                    Log.i("autoLogin","fail")

                    Handler().postDelayed({
                        joinBtn.visibility= View.VISIBLE
                        loginBtn.visibility= View.VISIBLE
                    },3000L)
                }

                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    if(response.body()?.success==true){

                        Log.i("jwt: ",jwt)

                        Handler().postDelayed({
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        },2000L)


                    }
                    else{

                        Handler().postDelayed({
                            joinBtn.visibility= View.VISIBLE
                            loginBtn.visibility= View.VISIBLE
                        },3000L)

                        Log.i("autoLogin: ","fail")
                    }
                }
            })

        }
        else{
            Handler().postDelayed({
                joinBtn.visibility= View.VISIBLE
                loginBtn.visibility= View.VISIBLE
            },3000L)
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
