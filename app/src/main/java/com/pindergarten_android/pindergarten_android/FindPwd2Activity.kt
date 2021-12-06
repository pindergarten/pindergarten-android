package com.pindergarten_android.pindergarten_android

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pindergarten_android.pindergarten_android.databinding.ActivityFindpwd2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindPwd2Activity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var info:TextView ?=null
    var pwd1:EditText ?= null
    var pwd2:EditText ?= null
    var nextBtn:ImageButton ?=null
    var phoneNum : String ?=null



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFindpwd2Binding = DataBindingUtil.setContentView(this, R.layout.activity_findpwd2)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@FindPwd2Activity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        //상태바
        val window = window
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE


        var intent : Intent = intent
        phoneNum = intent.getStringExtra("phone")

        info = findViewById(R.id.info)
        info?.visibility=View.INVISIBLE
        pwd1 = findViewById(R.id.pwd1)
        pwd2 = findViewById(R.id.pwd2)
        nextBtn=findViewById(R.id.nextBtn)
        nextBtn?.isClickable = false


        pwd1?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                info?.visibility = View.INVISIBLE
                nextBtn?.setImageResource(R.drawable.join_next)
                nextBtn?.isClickable = false
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(pwd1?.text.toString().length <8 ||pwd1?.text.toString().length >16){
                    info?.text = "*8~16자 이내의  비밀번호를 입력해주세요."
                    info?.visibility = View.VISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
                else if(pwd1?.text.toString() == pwd2?.text.toString()){
                    nextBtn?.setImageResource(R.drawable.join_next2)
                    nextBtn?.isClickable = true
                    info?.visibility = View.INVISIBLE
                }
                else{
                    info?.text = "*비밀번호가 일치하지않습니다."
                    info?.visibility = View.INVISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(pwd1?.text.toString().length <8 ||pwd1?.text.toString().length >16){
                    info?.text = "*8~16자 이내의  비밀번호를 입력해주세요."
                    info?.visibility = View.VISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
                else if(pwd1?.text.toString() == pwd2?.text.toString()){
                    nextBtn?.setImageResource(R.drawable.join_next2)
                    nextBtn?.isClickable = true
                    info?.visibility = View.INVISIBLE
                }
                else{
                    info?.text = "*비밀번호가 일치하지않습니다."
                    info?.visibility = View.INVISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
            }
        })

        pwd2?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                nextBtn?.setImageResource(R.drawable.join_next)
                nextBtn?.isClickable = false
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(pwd1?.text.toString() != pwd2?.text.toString()){
                    info?.text = "*비밀번호가 일치하지않습니다."
                    info?.visibility = View.VISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
                else if(!(pwd1?.text.toString().length <8 ||pwd1?.text.toString().length >16)){
                    nextBtn?.setImageResource(R.drawable.join_next2)
                    nextBtn?.isClickable = true
                    info?.visibility = View.INVISIBLE
                }
                else{
                    info?.text = "*8~16자 이내의  비밀번호를 입력해주세요."
                    info?.visibility = View.VISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(pwd1?.text.toString() != pwd2?.text.toString()){
                    info?.text = "*비밀번호가 일치하지않습니다."
                    info?.visibility = View.VISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
                else if(!(pwd1?.text.toString().length <8 ||pwd1?.text.toString().length >16)){
                    nextBtn?.setImageResource(R.drawable.join_next2)
                    nextBtn?.isClickable = true
                    info?.visibility = View.INVISIBLE
                }
                else{
                    info?.text = "*8~16자 이내의  비밀번호를 입력해주세요."
                    info?.visibility = View.VISIBLE
                    nextBtn?.setImageResource(R.drawable.join_next)
                    nextBtn?.isClickable = false
                }
            }
        })
    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.nextBtn->{
                if(pwd1?.text.toString().length>7 && pwd2?.text.toString().length>7){
                    info?.visibility=View.INVISIBLE

                    //서버: 비밀번호 재설정
                    var password: HashMap<String, String> = HashMap()
                    password["password"] = pwd1?.text.toString()
                    password["password_check"] = pwd2?.text.toString()
                    password["phone"] = phoneNum.toString()

                    apiService.passwordAPI(password)?.enqueue(object : Callback<Post?> {
                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.d("비밀번호 재설정 실패: ", t.toString())
                        }

                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            if (response.body()?.success==true) {
                                Log.i("비밀번호 재설정: ", "success")
                                Log.i("비밀번호 재설정: ", response.body().toString())
                                popup()

                            } else {
                                Log.i("비밀번호 재설정: ","fail")
                                Log.i("비밀번호 재설정: ", response.code().toString())
                            }
                        }
                    })


                }
            }

            R.id.backBtn -> {
                val intent = Intent(this, FindPwdActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

    fun popup(){

        //변경완료 메세지
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view2 = inflater.inflate(R.layout.join_popup, null)
        var text : TextView = view2.findViewById(R.id.text)
        var button : Button = view2.findViewById(R.id.button)
        text.text="변경완료되었습니다!\n로그인 화면으로 이동합니다."
        button.text="확인"
        val alertDialog = AlertDialog.Builder(this).create()
        button.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            alertDialog.dismiss()
        }
        alertDialog.setView(view2)
        alertDialog.show()

    }


}