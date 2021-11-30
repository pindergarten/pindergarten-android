package com.pindergarten.pindergarten_android

import com.google.gson.annotations.SerializedName

class Post {


    @SerializedName("email")
    val email: String? = null
    @SerializedName("isSuccess")
    val success: Boolean? = null
    @SerializedName("id")
    val id: Int? = null
    @SerializedName("nickname")
    val nickname: String? = null
    @SerializedName("phone")
    val phone: String? = null
    @SerializedName("profile_img")
    val profile_img: String? = null
    @SerializedName("message")
    val message: String? = null



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
        //수정확인
        @SerializedName("userId")
        var userId: Int? = null

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
        @SerializedName("image")
        var image : String ?=null
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

    //all pindergartens
    @SerializedName("allpindergartens")
    var allPindergartenList : ArrayList<allPindergarten> = ArrayList()

    class allPindergarten{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("address")
        var address: String? = null
        @SerializedName("thumbnail")
        var thumbnail: String? = null
        @SerializedName("latitude")
        var latitude: String? = null
        @SerializedName("longitude")
        var longitude: String? = null
        @SerializedName("rating")
        var rating: Double? = null
        @SerializedName("distance")
        var distance: Double? = null
        @SerializedName("isLiked")
        var isLiked: Int? = null
    }

    //like pindergartens
    @SerializedName("likedPindergartens")
    var likePindergartenList : ArrayList<likePindergarten> = ArrayList()

    class likePindergarten{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("address")
        var address: String? = null
        @SerializedName("thumbnail")
        var thumbnail: String? = null
        @SerializedName("rating")
        var rating: Double? = null
        @SerializedName("distance")
        var distance: Double? = null
    }

    // search_query pindergartens
    @SerializedName("searchPindergartens")
    var searchpindergartenList : ArrayList<searchPindergarten> = ArrayList()

    class searchPindergarten {
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("address")
        var address: String? = null
        @SerializedName("thumbnail")
        var thumbnail: String? = null
        @SerializedName("latitude")
        var latitude: String? = null
        @SerializedName("longitude")
        var longitude: String? = null
        @SerializedName("rating")
        var rating: Double? = null
        @SerializedName("distance")
        var distance: Double? = null
        @SerializedName("opening_hours")
        var opening_hours: String? = null
        @SerializedName("access_guide")
        var access_guide: String? = null
        @SerializedName("phone")
        var phone: String? = null
        @SerializedName("website")
        var website: String? = null
        @SerializedName("social")
        var social: String? = null
    }

    //pindergation detail api
    @SerializedName("pindergarten")
    var pindergarten : Pindergarten = Pindergarten()

    class Pindergarten{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("address")
        var address: String? = null
        @SerializedName("thumbnail")
        var thumbnail: String? = null
        @SerializedName("latitude")
        var latitude: String? = null
        @SerializedName("longitude")
        var longitude: String? = null
        @SerializedName("rating")
        var rating: Double? = null
        @SerializedName("distance")
        var distance: Double? = null
        @SerializedName("opening_hours")
        var opening_hours: String? = null
        @SerializedName("access_guide")
        var access_guide: String? = null
        @SerializedName("phone")
        var phone: String? = null
        @SerializedName("website")
        var website: String? = null
        @SerializedName("social")
        var social: String? = null
        @SerializedName("isLiked")
        var isLiked: Int? = null
        @SerializedName("imgUrls")
        var postImage : ArrayList<pindergartenImage> = ArrayList()
    }

    class pindergartenImage{
        @SerializedName("image_url")
        var postImageUrl: String? = null
    }

    //marker event
    @SerializedName("nearPindergartens")
    var nearPindergartens :  ArrayList<NearPindergartens> = ArrayList()

    class NearPindergartens {
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("address")
        var address: String? = null
        @SerializedName("thumbnail")
        var thumbnail: String? = null
        @SerializedName("latitude")
        var latitude: String? = null
        @SerializedName("longitude")
        var longitude: String? = null
        @SerializedName("rating")
        var rating: Double? = null
        @SerializedName("isLiked")
        var isLiked: Int? = null
        @SerializedName("distance")
        var distance: Double? = null
    }

    //blog review
    @SerializedName("blogReviews")
    var blogReviews :  ArrayList<BlogReviews> = ArrayList()

    class BlogReviews {
        @SerializedName("title")
        var title: String? = null
        @SerializedName("content")
        var content: String? = null
        @SerializedName("date")
        var date: String? = null
        @SerializedName("link")
        var link: String? = null

    }

    //myPet Search
    @SerializedName("pets")
    val mypetList: ArrayList<myPet> = ArrayList()

    class myPet{

        @SerializedName("id")
        val id: Int? = null
        @SerializedName("name")
        val name: String? = null
        @SerializedName("profile_image")
        val profile_image: String? = null

    }

    //pet Search
    @SerializedName("pet")
    var pet : Pet = Pet()

    class Pet{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("profile_image")
        var profile_image: String? = null
        @SerializedName("gender")
        var gender: Int? = null
        @SerializedName("breed")
        var breed: String? = null
        @SerializedName("birth")
        var birth: String? = null
        @SerializedName("vaccination")
        var vaccination: Int? = null
        @SerializedName("neutering")
        var neutering: Int? = null

    }

    //my Page
    @SerializedName("user")
    var user : User = User()

    class User{
        @SerializedName("id")
        var id: Int? = null
        @SerializedName("nickname")
        var nickname: String? = null
        @SerializedName("phone")
        var phone: String? = null
        @SerializedName("profile_img")
        var profile_img: String? = null
    }

    @SerializedName("posts")
    val mypostsList: ArrayList<myPost> = ArrayList()

    class myPost{

        @SerializedName("id")
        val id: Int? = null
        @SerializedName("thumbnail")
        val thumbnail: String? = null

    }


}
