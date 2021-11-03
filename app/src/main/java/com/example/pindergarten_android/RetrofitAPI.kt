package com.example.pindergarten_android

import retrofit2.Call
import retrofit2.http.*


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

    //전체 게시글 조회
    @GET("api/posts")
    fun searchAllPostAPI(): Call<Post?>?

    //특정 게시글 정보
    @GET("api/posts/{postId}")
    fun postDetailAPI(@Path("postId") postId: Int): Call<Post?>?

    //게시물 좋아요
    @FormUrlEncoded
    @POST("api/posts/{postId}/like")
    fun postLikeAPI(@Path("postId") postId: Int ,@FieldMap param: HashMap<String, String>): Call<Post?>?

    //게시글 댓글 확인
    @GET("api/posts/{postId}/comments")
    fun postCommentAPI(@Path("postId") postId: Int): Call<Post?>?


}