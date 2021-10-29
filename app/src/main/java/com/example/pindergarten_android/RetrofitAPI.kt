package com.example.pindergarten_android

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface RetrofitAPI {

    //아이디,비밀번호 확인 (로그인)
    @FormUrlEncoded
    @POST("api/users/sign-in")
    fun loginAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //회원가입
    @FormUrlEncoded
    @POST("api/users")
    fun joinAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //회원확인 (휴대폰번호로 확인)
    @FormUrlEncoded
    @POST("api/users/phone")
    fun userconfirmAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //인증문자 전송
    @FormUrlEncoded
    @POST("api/users/sms-send")
    fun smssendAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //인증문자 확인
    @FormUrlEncoded
    @POST("api/users/sms-verify")
    fun smsverifyAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //닉네임 중복확인
    @FormUrlEncoded
    @POST("api/users/nickname")
    fun nicknameAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //비밀번호 재설정
    @FormUrlEncoded
    @POST("api/users/find-pw")
    fun passwordAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

}