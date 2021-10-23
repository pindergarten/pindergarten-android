package com.example.pindergarten_android

import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.HashMap

interface RetrofitAPI {

    @FormUrlEncoded
    @POST("api/user/signup")
    fun singupAPI(@FieldMap param: HashMap<String, String>): Call<Post?>?

}