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


    //postComment & eventComment
    @SerializedName("comments")
    val commentList: ArrayList<postComment> = ArrayList()
    class postComment{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("nickname")
        var nickname: String? = null
        @SerializedName("profile_img")
        var user_image: String? = null
        @SerializedName("date")
        var date: String? = null
        @SerializedName("content")
        var content: String? = null
        @SerializedName("userId")
        var userId: Int? = null
    }



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
        @SerializedName("isLiked")
        var isLiked: Int? = null

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
        @SerializedName("isLiked")
        var isLiked: Int? = null

        @SerializedName("imgUrls")
        var postImage : ArrayList<postImage> = ArrayList()
    }

    class postImage{
        @SerializedName("postImageUrl")
        var postImageUrl: String? = null
    }


    //allEvent
    @SerializedName("allevents")
    var allEventList : ArrayList<allEvent> = ArrayList()

    class allEvent{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("title")
        var title: String? = null
        @SerializedName("thumbnail")
        var thumbnail: String? = null
        @SerializedName("expired_at")
        var expired_at: String? = null
        @SerializedName("created_at")
        var created_at: String? = null

    }

    //event
    @SerializedName("event")
    var eventList : EventList = EventList()

    class EventList{
        @SerializedName("id")
        var id : Int ?=null
        @SerializedName("title")
        var title : String ?=null
        @SerializedName("thumbnail")
        var thumbnail : String ?=null
        @SerializedName("expired_at")
        var expired_at : String ?=null
        @SerializedName("likeCount")
        var likeCount : Int ?=null
        @SerializedName("commentCount")
        var commentCount : Int ?=null
        @SerializedName("created_at")
        var created_at: String? = null
        @SerializedName("isLiked")
        var isLiked: Int? = null


    }




}
