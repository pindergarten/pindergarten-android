package com.example.pindergarten_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.pindergarten_android.databinding.ActivityFindpwd2Binding
import com.example.pindergarten_android.databinding.ActivityJoin2Binding
import com.example.pindergarten_android.databinding.ActivityJoinBinding
import com.example.pindergarten_android.databinding.ActivitySplashBinding
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class FindPwd2Activity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl( "http://13.125.184.176:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var info:TextView ?=null
    var pwd1:EditText ?= null
    var pwd2:EditText ?= null
    var nextBtn:ImageButton ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFindpwd2Binding = DataBindingUtil.setContentView(this, R.layout.activity_findpwd2)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@FindPwd2Activity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

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
                    //변경완료 메세지
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view2 = inflater.inflate(R.layout.join_popup,null)
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("변경완료되었습니다.")
                        .setPositiveButton("로그인화면으로 돌아가기"){
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