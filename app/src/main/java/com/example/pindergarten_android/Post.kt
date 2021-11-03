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

        @SerializedName("isSet")
        val liked: Int? = null

    }

    /*
    //postComment
    @SerializedName("result")
    val commentList: ArrayList<postComment> = ArrayList()
    class postComment{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("nickname")
        var user_id: String? = null
        @SerializedName("profile_img")
        var user_image: String? = null
        @SerializedName("date")
        var date: String? = null
        @SerializedName("content")
        var content: String? = null
    }

     */




    //searchAllPost
    @SerializedName("allposts")
    var allPostList : ArrayList<allPost> = ArrayList()

    class allPost{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("nickname")
        var user_id: String? = null
        @SerializedName("profile_img")
        var user_image: String? = null
        @SerializedName("thumbnail")
        var thumbnail: String? = null
        @SerializedName("content")
        var content: String? = null
    }

    //post
    @SerializedName("post")
    var postList : Post = Post()

    class Post{

        @SerializedName("content")
        var postText : String ?=null
        @SerializedName("date")
        var date : String ?=null
        @SerializedName("nickname")
        var postUserId : String ?=null
        @SerializedName("profile_img")
        var postUserImage : String ?=null
        @SerializedName("likeCount")
        var likeCount : Int ?=null
        @SerializedName("commentCount")
        var commentCount : Int ?=null

        @SerializedName("imgUrls")
        var postImage : ArrayList<postImage> = ArrayList()
    }

    class postImage{
        @SerializedName("postImageUrl")
        var postImageUrl: String? = null
    }





}
