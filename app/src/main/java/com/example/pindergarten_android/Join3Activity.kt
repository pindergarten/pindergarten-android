package com.example.pindergarten_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pindergarten_android.databinding.ActivityJoin3Binding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class Join3Activity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl( "http://13.125.184.176:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var info:TextView ?=null
    var id:EditText ?= null
    var confirmIdBtn:ImageButton ?=null
    var pass : Boolean =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityJoin3Binding = DataBindingUtil.setContentView(this, R.layout.activity_join3)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@Join3Activity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        info = findViewById(R.id.info)
        info?.visibility=View.INVISIBLE

        id = findViewById(R.id.id)
        confirmIdBtn = findViewById(R.id.confirmIdBtn)


    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.confirmIdBtn->{
                //서버연결
                if(true){
                    confirmIdBtn?.setImageResource(R.drawable.join3_confirm2)
                    pass = true
                }
                else{
                    info?.text="*이 계정 이름은 이미 다른 사람이 사용하고 있습니다."
                }

            }
            R.id.finishBtn->{
                if(pass){
                    //회원가입완료 메세지
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view2 = inflater.inflate(R.layout.join_popup,null)
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("회원가입이 완료되었습니다!\n로그인 화면으로 이동합니다.")
                        .setPositiveButton("확인"){
                                _, _ ->
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .create()
                    alertDialog.setView(view2)
                    alertDialog.show()
                }
            }
            }



        }

    }








