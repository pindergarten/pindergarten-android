package com.pindergarten.pindergarten_android

import android.content.ContentValues
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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pindergarten.pindergarten_android.databinding.ActivityFindpwdBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern


class FindPwdActivity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var line: ImageView? = null
    var vertifyNum: EditText? = null
    var confirmBtn: ImageButton? = null
    var info: TextView? = null
    var phoneNum: EditText? = null
    var phone_info: TextView? = null
    var sendNum: ImageButton? = null
    var nextBtn: ImageButton? = null
    var pass: Boolean = false
    var imm : InputMethodManager ?= null


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFindpwdBinding = DataBindingUtil.setContentView(this, R.layout.activity_findpwd)
        binding.vm = PindergartenViewModel()
        binding.activity = this@FindPwdActivity

        //액션바 제거
        var actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        //상태바
        val window = window
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE

        phoneNum = findViewById(R.id.phoneNum)
        sendNum = findViewById(R.id.sendNum)
        sendNum?.isClickable = false

        //hide
        line = findViewById(R.id.imageView4)
        vertifyNum = findViewById(R.id.vertifyNum)
        confirmBtn = findViewById(R.id.confirmBtn)
        info = findViewById(R.id.info)
        phone_info = findViewById(R.id.phone_info)
        nextBtn=findViewById(R.id.nextBtn)

        line?.visibility = View.INVISIBLE
        vertifyNum?.visibility = View.INVISIBLE
        confirmBtn?.visibility = View.INVISIBLE
        info?.visibility = View.INVISIBLE
        phone_info?.visibility = View.VISIBLE

        phoneNum?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                sendNum?.setImageResource(R.drawable.join_vertify)
                sendNum?.isClickable = false
                phone_info?.visibility = View.INVISIBLE
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phoneNum?.text.toString())){
                    sendNum?.setImageResource(R.drawable.join_vertify)
                    sendNum?.isClickable = false
                    phone_info?.visibility = View.VISIBLE
                }
                else{
                    sendNum?.setImageResource(R.drawable.join_vertify2)
                    sendNum?.isClickable = true
                    phone_info?.visibility = View.INVISIBLE
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phoneNum?.text.toString())){
                    sendNum?.setImageResource(R.drawable.join_vertify)
                    sendNum?.isClickable = false
                    phone_info?.visibility = View.VISIBLE
                }
                else{
                    sendNum?.setImageResource(R.drawable.join_vertify2)
                    sendNum?.isClickable = true
                    phone_info?.visibility = View.INVISIBLE
                }
                sendNum?.isClickable = true
                line?.visibility = View.INVISIBLE
                confirmBtn?.visibility = View.INVISIBLE
                info?.visibility = View.INVISIBLE
                vertifyNum?.visibility = View.INVISIBLE
            }

        })

        vertifyNum?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                confirmBtn?.setImageResource(R.drawable.join_vertifyconfirm)
                confirmBtn?.isClickable = false
                info?.visibility = View.INVISIBLE
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(vertifyNum?.text.toString().length==4){
                    confirmBtn?.setImageResource(R.drawable.join_vertifyconfirm2)
                    confirmBtn?.isClickable = true
                    info?.visibility = View.INVISIBLE
                }
                else{
                    confirmBtn?.setImageResource(R.drawable.join_vertifyconfirm)
                    confirmBtn?.isClickable = false
                    info?.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(vertifyNum?.text.toString().length==4){
                    confirmBtn?.setImageResource(R.drawable.join_vertifyconfirm2)
                    confirmBtn?.isClickable = true
                    info?.visibility = View.INVISIBLE
                }
                else{
                    confirmBtn?.setImageResource(R.drawable.join_vertifyconfirm)
                    confirmBtn?.isClickable = false
                    info?.visibility = View.VISIBLE
                }
            }
        })


    }

    fun btnClick(view: View) {

        when (view?.id) {
            R.id.sendNum -> {

                //서버: 휴대폰번호로 회원확인
                var vertifyNum1: HashMap<String, String> = HashMap()
                vertifyNum1["phone"] = phoneNum?.text.toString()

                apiService.userconfirmAPI(vertifyNum1)?.enqueue(object : Callback<Post?> {
                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.d(ContentValues.TAG, "실패 : {${t}}")
                    }

                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("userCheck: ", "success")
                        if (response.body()?.success == true) {
                            //비회원
                            popup_notuser()
                        } else {
                            //회원
                            if (phoneNum?.text.toString().length > 1) {
                                line?.visibility = View.VISIBLE
                                confirmBtn?.visibility = View.VISIBLE
                                info?.visibility = View.VISIBLE
                                vertifyNum?.visibility = View.VISIBLE
                                confirmBtn?.isClickable = false


                                //서버: 휴대폰 인증번호 전송
                                var vertifyNum: HashMap<String, String> = HashMap()
                                vertifyNum["phone"] = phoneNum?.text.toString()
                                apiService.smssendAPI(vertifyNum)
                                    ?.enqueue(object : Callback<Post?> {
                                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                                            Log.d(ContentValues.TAG, "실패 : {${t}}")
                                        }

                                        override fun onResponse(
                                            call: Call<Post?>,
                                            response: Response<Post?>
                                        ) {
                                            Log.i("인증번호 전송: ", "success")
                                            Log.i("인증번호 전송: ", response.body().toString())

                                            sendNum?.isClickable = false
                                        }
                                    })

                            }
                        }
                    }
                })

            }
            R.id.confirmBtn -> {

                //서버: 휴대폰 인증번호 확인
                var vertifyNum1: HashMap<String, String> = HashMap()
                vertifyNum1["phone"] = phoneNum?.text.toString()
                vertifyNum1["verifyCode"] = vertifyNum?.text.toString()

                apiService.smsverifyAPI(vertifyNum1)?.enqueue(object : Callback<Post?> {
                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.d(ContentValues.TAG, "실패 : {${t}}")
                        info?.visibility = View.VISIBLE
                    }

                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("휴대폰 인증번호 확인: ", response.body().toString())
                        if (response.body()?.success == true) {
                            Log.i("휴대폰 인증번호 확인: ", "success")
                            Log.i("휴대폰 인증번호 확인: ", response.body().toString())

                            info?.visibility = View.INVISIBLE
                            nextBtn?.setImageResource(R.drawable.join_next2)
                            pass = true

                            //키보드 내리기
                            imm = applicationContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                            imm?.hideSoftInputFromWindow(view.windowToken, 0)

                            popup()

                        } else {
                            Log.i("휴대폰 인증번호 확인 : ", "fail")
                            Log.i("휴대폰 인증번호 확인 : ", response.code().toString())
                            info?.visibility = View.VISIBLE
                            popup_fail()
                        }
                    }
                })


            }

            R.id.nextBtn -> {
                if (pass) {
                    //화면이동
                    val intent = Intent(this, FindPwd2Activity::class.java)
                    intent.putExtra("phone", phoneNum?.text.toString())
                    startActivity(intent)

                } else {
                    //인증오류 메세지
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view2 = inflater.inflate(R.layout.join_popup, null)
                    var text: TextView = view2.findViewById(R.id.text)
                    var button: Button = view2.findViewById(R.id.button)
                    text.text = "휴대폰을 인증 해주세요."
                    button.text = "확인"
                    val alertDialog = AlertDialog.Builder(this).create()
                    button.setOnClickListener { alertDialog.dismiss() }
                    alertDialog.setView(view2)
                    alertDialog.show()
                }
            }


            R.id.backBtn -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun popup(){

        vertifyNum?.inputType = InputType.TYPE_NULL
        phoneNum?.inputType = InputType.TYPE_NULL

        //인증확인 메세지
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view2 = inflater.inflate(R.layout.join_popup, null)
        var text : TextView = view2.findViewById(R.id.text)
        var button : Button = view2.findViewById(R.id.button)
        text.text="인증되었습니다."
        button.text="확인"
        val alertDialog = AlertDialog.Builder(this).create()
        button.setOnClickListener{ alertDialog.dismiss() }
        alertDialog.setView(view2)
        alertDialog.show()
    }

    fun popup_fail(){

        //인증확인 메세지
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view2 = inflater.inflate(R.layout.join_popup, null)
        var text : TextView = view2.findViewById(R.id.text)
        var button : Button = view2.findViewById(R.id.button)
        text.text="인증번호를 다시 입력해주세요."
        button.text="확인"
        val alertDialog = AlertDialog.Builder(this).create()
        button.setOnClickListener{ alertDialog.dismiss() }
        alertDialog.setView(view2)
        alertDialog.show()

    }

    fun popup_notuser(){

        //비회원
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view2 = inflater.inflate(R.layout.join_popup, null)
        var text : TextView = view2.findViewById(R.id.text)
        var button : Button = view2.findViewById(R.id.button)
        text.text="가입되어있지 않은 회원입니다.\n로그인 화면으로 이동합니다."
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