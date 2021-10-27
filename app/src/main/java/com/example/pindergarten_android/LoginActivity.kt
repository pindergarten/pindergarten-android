package com.example.pindergarten_android

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
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

    var loginText :EditText?=null
    var pwdText :EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@LoginActivity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        PreferenceManager.setString(this, "accessToken", "jihyun")
        loginText = findViewById(R.id.editText2)
        pwdText = findViewById(R.id.editText)
        var login : ImageButton = findViewById(R.id.login)

        loginText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                login?.setImageResource(R.drawable.login_loginbtn)
                login?.isClickable = false
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(loginText?.text.toString().length>9 && pwdText?.text.toString().length>7){
                    login?.setImageResource(R.drawable.login_loginbtn2)
                    login?.isClickable = true
                }
                else{
                    login?.setImageResource(R.drawable.login_loginbtn)
                    login?.isClickable = false
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(loginText?.text.toString().length>9 && pwdText?.text.toString().length>7){
                    login?.setImageResource(R.drawable.login_loginbtn2)
                    login?.isClickable = true
                }
                else{
                    login?.setImageResource(R.drawable.login_loginbtn)
                    login?.isClickable = false
                }
            }
        })

        pwdText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                login?.setImageResource(R.drawable.login_loginbtn)
                login?.isClickable = false
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(loginText?.text.toString().length>9 && pwdText?.text.toString().length>7){
                    login?.setImageResource(R.drawable.login_loginbtn2)
                    login?.isClickable = true
                }
                else{
                    login?.setImageResource(R.drawable.login_loginbtn)
                    login?.isClickable = false
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(loginText?.text.toString().length>9 && pwdText?.text.toString().length>7){
                    login?.setImageResource(R.drawable.login_loginbtn2)
                    login?.isClickable = true
                }
                else{
                    login?.setImageResource(R.drawable.login_loginbtn)
                    login?.isClickable = false
                }
            }
        })



    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.login->{
                if(loginText?.text?.length!!> 9 && pwdText?.text?.length!! >7){
                    Log.i("login: ","login")
                    //서버연결
                }
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