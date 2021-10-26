package com.example.pindergarten_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pindergarten_android.databinding.ActivityJoin2Binding
import com.example.pindergarten_android.databinding.ActivityJoinBinding
import com.example.pindergarten_android.databinding.ActivitySplashBinding
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class Join2Activity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl( "http://13.125.184.176:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var info:TextView ?=null
    var pwd1:EditText ?= null
    var pwd2:EditText ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityJoin2Binding = DataBindingUtil.setContentView(this, R.layout.activity_join2)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@Join2Activity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        info = findViewById(R.id.info)
        info?.visibility=View.INVISIBLE
        pwd1 = findViewById(R.id.pwd1)
        pwd2 = findViewById(R.id.pwd2)
    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.nextBtn->{
                if(pwd1?.text.toString() == pwd2?.text.toString()){
                    info?.visibility=View.INVISIBLE
                    val intent = Intent(this, Join3Activity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    info?.visibility=View.VISIBLE
                }
            }


        }

    }

}