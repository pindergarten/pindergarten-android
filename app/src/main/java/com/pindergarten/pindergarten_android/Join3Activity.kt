package com.pindergarten.pindergarten_android

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
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
import com.pindergarten.pindergarten_android.databinding.ActivityJoin3Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Join3Activity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var info:TextView ?=null
    var id:EditText ?= null
    var confirmIdBtn:ImageButton ?=null
    var finishBtn:ImageButton ?=null
    var pass : Boolean =false

    var password : String?=null
    var phoneNum : String?=null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityJoin3Binding = DataBindingUtil.setContentView(this, R.layout.activity_join3)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@Join3Activity

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
        password = intent.getStringExtra("password")

        info = findViewById(R.id.info)
        info?.visibility=View.INVISIBLE

        id = findViewById(R.id.id)
        confirmIdBtn = findViewById(R.id.confirmIdBtn)
        finishBtn = findViewById(R.id.finishBtn)
        confirmIdBtn?.isClickable = false

        id?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                confirmIdBtn?.setImageResource(R.drawable.join3_confirm)
                confirmIdBtn?.isClickable = false
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(id?.text.toString().length>=2&& isValidNickname(id?.text.toString())){
                    confirmIdBtn?.setImageResource(R.drawable.join3_confirm2)
                    confirmIdBtn?.isClickable = true
                }else{
                    info?.text="*2자 이상 영문으로 입력해주세요."
                    info?.visibility=View.VISIBLE
                    confirmIdBtn?.setImageResource(R.drawable.join3_confirm)
                    confirmIdBtn?.isClickable = false
                }

            }
            override fun afterTextChanged(editable: Editable) {
                if(id?.text.toString().length>=2&& isValidNickname(id?.text.toString())){
                    confirmIdBtn?.setImageResource(R.drawable.join3_confirm2)
                    confirmIdBtn?.isClickable = true
                }else{
                    info?.text="*2자 이상 영문으로 입력해주세요."
                    info?.visibility=View.VISIBLE
                    confirmIdBtn?.setImageResource(R.drawable.join3_confirm)
                    confirmIdBtn?.isClickable = false
                }
            }
        })

    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.confirmIdBtn->{
                if(id?.text.toString().length>=2&& isValidNickname(id?.text.toString())){

                    //서버: 닉네임 중복확인
                    var nick: HashMap<String, String> = HashMap()
                    nick["nickname"] = id?.text.toString()

                    apiService.nicknameAPI(nick)?.enqueue(object : Callback<Post?> {
                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.d("닉네임 중복확인 실패 : ", t.toString())
                            info?.text="*이 계정 이름은 이미 다른 사람이 사용하고 있습니다."
                            info?.visibility=View.VISIBLE
                        }

                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            if (response.body()?.success==true) {
                                Log.i("닉네임 중복확인: ", "success")
                                Log.i("닉네임 중복확인: ", response.body().toString())
                                confirmIdBtn?.setImageResource(R.drawable.join3_confirmsame)
                                finishBtn?.setImageResource(R.drawable.join_finish2)
                                info?.visibility=View.INVISIBLE
                                pass = true
                                id?.setInputType(InputType.TYPE_NULL)

                            } else {
                                Log.i("닉네임 중복확인: ","fail")
                                Log.i("닉네임 중복확인: ", response.code().toString())
                                info?.text="*이 계정 이름은 이미 다른 사람이 사용하고 있습니다."
                                info?.visibility=View.VISIBLE
                            }
                        }
                    })

                }
            }

            R.id.finishBtn->{

                if(pass){

                //서버: 회원가입
                var join: HashMap<String, String> = HashMap()
                    join["nickname"] = id?.text.toString()
                    join["phone"] = phoneNum.toString()
                    join["password"] = password.toString()
                    join["password_check"] = password.toString()

                apiService.joinAPI(join)?.enqueue(object : Callback<Post?> {
                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.d("회원가입 실패: ", t.toString())
                    }

                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        if (response.body()?.success==true) {
                            Log.i("회원가입: ", "success")
                            Log.i("회원가입: ", response.body().toString())
                            popup()

                        } else {
                            Log.i("회원가입: ","fail")
                            Log.i("회원가입: ", response.code().toString())
                        }
                    }
                })
                }
                else{

                    //회원가입요청 메세지
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view2 = inflater.inflate(R.layout.join_popup, null)
                    var text : TextView = view2.findViewById(R.id.text)
                    var button : Button = view2.findViewById(R.id.button)
                    text.text="계정 이름을 중복 확인해주세요."
                    button.text="확인"
                    val alertDialog = AlertDialog.Builder(this).create()
                    button.setOnClickListener{ alertDialog.dismiss() }
                    alertDialog.setView(view2)
                    alertDialog.show()
                }
            }

            R.id.backBtn -> {
                val intent = Intent(this, Join2Activity::class.java)
                intent.putExtra("phone",phoneNum)
                startActivity(intent)
                finish()
            }
            }



        }

    private fun isValidNickname(nickname: String?): Boolean {
        val trimmedNickname = nickname?.trim().toString()
        val exp = Regex("^[-_a-zA-Z0-9]+$")
        return !trimmedNickname.isNullOrEmpty() && exp.matches(trimmedNickname)
    }

    fun popup(){

        //회원가입완료 메세지
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view2 = inflater.inflate(R.layout.join_popup, null)
        var text : TextView = view2.findViewById(R.id.text)
        var button : Button = view2.findViewById(R.id.button)
        text.text="회원가입이 완료되었습니다!\n로그인 화면으로 이동합니다."
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








