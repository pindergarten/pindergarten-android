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
import com.example.pindergarten_android.databinding.ActivityFindpwdBinding
import com.example.pindergarten_android.databinding.ActivityJoinBinding
import com.example.pindergarten_android.databinding.ActivitySplashBinding
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class FindPwdActivity : AppCompatActivity() {

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl( "http://13.125.184.176:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var line : ImageView? =null
    var vertifyNum :EditText ?=null
    var confirmBtn : ImageButton?=null
    var info :TextView?=null
    var phoneNum:EditText?=null
    var phone_info:TextView?=null
    var sendNum:ImageButton?=null
    var vertify_number :Int?=null
    var pass :Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_join)
        val binding: ActivityFindpwdBinding = DataBindingUtil.setContentView(this, R.layout.activity_findpwd)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@FindPwdActivity

        //액션바 제거
        var actionBar : ActionBar? = supportActionBar
        actionBar?.hide()

        phoneNum=findViewById(R.id.phoneNum)
        sendNum=findViewById(R.id.sendNum)

        //hide
        line = findViewById(R.id.imageView4)
        vertifyNum = findViewById(R.id.vertifyNum)
        confirmBtn = findViewById(R.id.confirmBtn)
        info = findViewById(R.id.info)
        phone_info = findViewById(R.id.phone_info)

        line?.visibility = View.INVISIBLE
        vertifyNum?.visibility = View.INVISIBLE
        confirmBtn?.visibility = View.INVISIBLE
        info?.visibility = View.INVISIBLE
        phone_info?.visibility = View.INVISIBLE

    }

    fun btnClick(view : View){

        when(view?.id){
            R.id.sendNum->{
                if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phoneNum?.text.toString())){
                    phone_info?.visibility = View.VISIBLE
                }
                else{
                    phone_info?.visibility = View.INVISIBLE
                    line?.visibility = View.VISIBLE
                    vertifyNum?.visibility = View.VISIBLE
                    confirmBtn?.visibility = View.VISIBLE
                    sendNum?.setImageResource(R.drawable.join_vertify2)
                    //서버: 인증번호 저장
                    vertify_number=0
                }

            }
            R.id.confirmBtn->{
                if(vertifyNum?.text.toString().toInt() != vertify_number){
                    info?.visibility=View.VISIBLE
                }
                else{
                    info?.visibility=View.INVISIBLE
                    pass = true
                    //인증확인 메세지
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view2 = inflater.inflate(R.layout.join_popup,null)
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("인증되었습니다.")
                        .setPositiveButton("확인"){
                                _, _ ->
                        }
                        .create()
                    alertDialog.setView(view2)
                    alertDialog.show()
                }
            }
            R.id.nextBtn->{
                if(pass){
                    //화면이동
                    val intent = Intent(this, FindPwd2Activity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    //인증오류 메세지
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view2 = inflater.inflate(R.layout.join_popup,null)
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("인증번호를 다시 입력해주세요.")
                        .setPositiveButton("확인"){
                                _, _ ->
                        }
                        .create()
                    alertDialog.setView(view2)
                    alertDialog.show()
                }
            }

        }

    }

}