package com.example.pindergarten_android

import com.google.gson.annotations.SerializedName
import java.util.*

class Post {


    @SerializedName("email")
    val email: String? = null
    @SerializedName("isSuccess")
    val success: Boolean? = null

    //login
    @SerializedName("result")
    private val resultList: Result = Result()
    fun getResultList(): Result? { return resultList }
    class Result {
        @SerializedName("jwt")
        val jwt: String? = null
        @SerializedName("userId")
        val userId: Int? = null

    }

}
