package com.example.pindergarten_android

import retrofit2.Call
import retrofit2.http.*


interface RetrofitAPI {

    //아이디,비밀번호 확인
    @FormUrlEncoded
    @POST("api/users/check")
    fun usercheckAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //회원가입
    @FormUrlEncoded
    @POST("api/users")
    fun joinAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //인증문자 전송
    @FormUrlEncoded
    @POST("api/users/sms-send")
    fun smssendAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //인증문자 확인
    @FormUrlEncoded
    @POST("api/users/sms-verify")
    fun smsverifyAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //로그아웃
    @FormUrlEncoded
    @PATCH("api/logout")
    fun logoutAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

    //회원탈퇴
    @FormUrlEncoded
    @PATCH("api/users/{userId}/status")
    fun secessionAPI(@Path("userId") userId: String?, @FieldMap param: HashMap<String?, Any?>?): Call<Post?>?

    //마이페이지 조회
    @GET("api/users/{userId}")
    fun mypageAPI(@Path("userId") userId: String?): Call<Post?>?


}