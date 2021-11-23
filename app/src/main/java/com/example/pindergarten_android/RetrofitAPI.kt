package com.example.pindergarten_android

import okhttp3.MultipartBody
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
    fun searchAllPostAPI(@Header("x-access-token") value : String): Call<Post?>?

    //특정 게시글 정보
    @GET("api/posts/{postId}")
    fun postDetailAPI(@Path("postId") postId: Int , @Header("x-access-token") value : String): Call<Post?>?

    //게시물 좋아요
    @FormUrlEncoded
    @POST("api/posts/{postId}/like")
    fun postLikeAPI(@Path("postId") postId: Int , @Header("x-access-token") value : String,@FieldMap param: HashMap<String, String>): Call<Post?>?

    //게시글 댓글 조회
    @GET("api/posts/{postId}/comments")
    fun postCommentAPI(@Path("postId") postId: Int,@Header("x-access-token") value : String): Call<Post?>?

    //게시글 댓글 등록
    @FormUrlEncoded
    @POST("api/posts/{postId}/comments")
    fun addPostCommentAPI(@Path("postId") postId: Int,@Header("x-access-token") value : String, @Field("content") content : String): Call<Post?>?

    //전체 이벤트 조회
    @GET("api/events")
    fun searchAllEventAPI(): Call<Post?>?

    //이벤트 상세 조회
    @GET("api/events/{eventId}")
    fun eventDetailAPI(@Path("eventId") eventId: Int , @Header("x-access-token") value : String): Call<Post?>?

    //이벤트 댓글 조회
    @GET("api/events/{eventId}/comments")
    fun eventCommentAPI(@Path("eventId") eventId: Int,@Header("x-access-token") value : String): Call<Post?>?

    //이벤트 좋아요
    @FormUrlEncoded
    @POST("api/events/{eventId}/like")
    fun eventLikeAPI(@Path("eventId") eventId: Int, @Header("x-access-token") value : String, @FieldMap param: HashMap<String, String>): Call<Post?>?

    //이벤트 댓글 등록
    @FormUrlEncoded
    @POST("api/events/{eventId}/comments")
    fun addEventCommentAPI(@Path("eventId") eventId: Int,@Header("x-access-token") value : String, @Field("content") content : String): Call<Post?>?

    //게시물 댓글 삭제
    @DELETE("api/posts/{postId}/comments/{commentId}")
    fun deletePostCommentAPI(@Header("x-access-token") value : String,@Path("postId") postId : Int,@Path("commentId") commentId : Int): Call<Post?>?

    //이벤트 댓글 삭제
    @DELETE("api/events/{eventId}/comments/{commentId}")
    fun deleteEventCommentAPI(@Header("x-access-token") value : String,@Path("eventId") eventId : Int,@Path("commentId") commentId : Int): Call<Post?>?

    //게시물 삭제
    @DELETE("api/posts/{postId}")
    fun deletePostAPI(@Header("x-access-token") value : String,@Path("postId") postId : Int): Call<Post?>?

    //게시물 신고
    @FormUrlEncoded
    @POST("api/posts/{postId}/declaration")
    fun declarePostAPI(@Header("x-access-token") value : String,@Path("postId") postId : Int, @FieldMap param: HashMap<String, String>, @Query("type") type : Int): Call<Post?>?

    //펫 유치원 전체 조회
    @GET("api/pindergartens")
    fun searchAllPindergartenAPI(@Header("x-access-token") value : String, @Query("latitude") latitude : Double,@Query("longitude") longitude : Double): Call<Post?>?

    //펫 유치원 상세 조회
    @GET("api/pindergartens/{pindergartenId}")
    fun PindergartenAPI(@Header("x-access-token") value : String,@Path("pindergartenId") pindergartenId : Int): Call<Post?>?

    //펫 유치원 좋아요
    @FormUrlEncoded
    @POST("api/pindergartens/{pindergartenId}/like")
    fun pindergartenLikeAPI(@Path("pindergartenId") pindergartenId: Int , @Header("x-access-token") value : String,@FieldMap param: HashMap<String, String>): Call<Post?>?

    //좋아요한 펫 유치원
    @GET("api/like/pindergartens")
    fun likedPindergartenAPI(@Header("x-access-token") value : String, @Query("latitude") latitude : Double,@Query("longitude") longitude : Double): Call<Post?>?

    //유치원 검색
    @GET("api/serch/pindergartens")
    fun searchPindergartenAPI(@Header("x-access-token") value : String,@Query("query") query: String, @Query("latitude") latitude : Double,@Query("longitude") longitude : Double): Call<Post?>?

    //유치원 블로그 검색
    @GET("api/pindergartens/{pindergartenId}/review")
    fun pindergartenBlogAPI(@Path("pindergartenId") pindergartenId: Int ): Call<Post?>?

    //마커클릭 이벤트
    @GET("api/near/pindergartens")
    fun markerAPI(@Header("x-access-token") value : String, @Query("latitude") latitude : Double,@Query("longitude") longitude : Double): Call<Post?>?

    //펫 등록
    @Multipart
    @FormUrlEncoded
    @POST("api/pets")
    fun addPetAPI(@Header("x-access-token") value : String,@FieldMap param: HashMap<String, Any>,@Part file: MultipartBody.Part?): Call<Post?>?

    //나의 펫 조회
    @GET("api/pets")
    fun myPetSearchAPI(@Header("x-access-token") value : String): Call<Post?>?

    //펫 상세조회
    @GET("api/pets/{petId}")
    fun petDetailAPI(@Header("x-access-token") value : String, @Query("petId") petId : Int ): Call<Post?>?
}