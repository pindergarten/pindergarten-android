package com.example.pindergarten_android

import android.R.attr.button
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.pindergarten_android.databinding.ActivityFindpwdBinding
import com.example.pindergarten_android.databinding.ActivityJoinBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern


class FindPwdActivity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFindpwdBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_findpwd)
        binding.vm = PindergartenViewModel()
        binding.activity = this@FindPwdActivity

        //액션바 제거
        var actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

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

                if(phoneNum?.text.toString().length>1){
                    line?.visibility = View.VISIBLE
                    confirmBtn?.visibility = View.VISIBLE
                    info?.visibility = View.VISIBLE
                    vertifyNum?.visibility = View.VISIBLE
                    confirmBtn?.isClickable = false

                    //서버: 휴대폰 인증번호 전송
                    var vertifyNum: HashMap<String, String> = HashMap()
                    vertifyNum["phone"] = phoneNum?.text.toString()
                    apiService.smssendAPI(vertifyNum)?.enqueue(object : Callback<Post?> {
                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.d(ContentValues.TAG, "실패 : {${t}}")
                        }

                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            if (response.body()?.success == "success") {
                                Log.i("인증번호 전송: ", "success")
                                Log.i("인증번호 전송: ", response.body().toString())

                            } else {
                                Log.i("인증번호 전송 : ", "fail")
                                Log.i("인증번호 전송 : ", response.code().toString())
                            }
                        }
                    })

                }
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
                        if (response.body()?.success == "success") {
                            Log.i("휴대폰 인증번호 확인: ", "success")
                            Log.i("휴대폰 인증번호 확인: ", response.body().toString())

                            info?.visibility = View.INVISIBLE
                            nextBtn?.setImageResource(R.drawable.join_next2)
                            pass = true
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
                    startActivity(intent)
                } else {
                    //인증오류 메세지
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view2 = inflater.inflate(R.layout.join_popup, null)
                    var text : TextView = view2.findViewById(R.id.text)
                    var button : Button = view2.findViewById(R.id.button)
                    text.text="휴대폰을 인증 해주세요."
                    button.text="확인"
                    val alertDialog = AlertDialog.Builder(this).create()
                    button.setOnClickListener{ alertDialog.dismiss() }
                    alertDialog.setView(view2)
                    alertDialog.show()

                }
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


}