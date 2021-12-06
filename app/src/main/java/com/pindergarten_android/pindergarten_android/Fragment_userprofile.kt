package com.pindergarten_android.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

class Fragment_userprofile : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var userImageUri : Uri?=null
    var userImageFile: File? = null
    var userImg : ImageView ?=null
    var adjustBtn : TextView ?=null
    var userId : Int ?= null

    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_userprofile,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            userId = bundle.getInt("userId")
        }

        userImg = view.findViewById(R.id.userImg)
        var userImgBtn : ImageButton = view.findViewById(R.id.userImgBtn)
        var userName : TextView =view.findViewById(R.id.userName)
        var userPhone : TextView = view.findViewById(R.id.userPhone)
        var welcomeText : TextView =view.findViewById(R.id.welcomeText)


        apiService.userProfileAPI(userId!!)?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                context?.let {
                    Glide.with(it)
                        .load(Uri.parse(response.body()?.profile_img))
                        .centerCrop()
                        .circleCrop()
                        .into(userImg!!)
                }

                userName.text = response.body()?.nickname
                userPhone.text =response.body()?.phone
                welcomeText.text = "${response.body()?.nickname}님,\n안녕하세요!"

                Log.i("userProfile","성공")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("userProfile",t.toString())
            }

        })



        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{

            mainAct.onBackPressed()

        }

        userImgBtn.setOnClickListener{
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.action= Intent.ACTION_GET_CONTENT

            startActivityForResult(intent,200)
        }

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())


        adjustBtn = view.findViewById(R.id.adjustBtn)
        adjustBtn!!.setOnClickListener{
            if(userImageUri!=null){

                var file = File(userImageUri!!.path)
                var inputStream : InputStream?= null
                try{
                    inputStream = context?.contentResolver?.openInputStream(userImageUri!!)!!
                }catch (e : IOException){
                    e.printStackTrace()
                }

                val bitmap = BitmapFactory.decodeStream(inputStream)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())
                val uploadFile = MultipartBody.Part.createFormData("profile_image", file.name, requestBody)

                apiService.userProfileUpdateAPI(sharedPreferences.toString(),uploadFile)?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                        if(response.body()?.success==true){
                            Log.i("updateProfile","success")
                        }
                        else{
                            Log.i("updateProfile","fail")
                        }

                        val dialogBuilder = AlertDialog.Builder(requireActivity())
                        val view = inflater.inflate(R.layout.join_popup, null)
                        var text : TextView = view.findViewById(R.id.text)
                        var button : Button = view.findViewById(R.id.button)
                        text.text="프로필이 수정되었습니다."
                        button.text="확인"
                        val alertDialog = dialogBuilder.create()
                        button.setOnClickListener{
                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_meAndPet()
                            val bundle = Bundle()
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }

                        Log.i("updateProfile","성공")
                        alertDialog.setView(view)
                        alertDialog.show()
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("updateProfile",t.toString())
                    }

                })
            }
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            data?.data?.let { uri ->
                val imageUri: Uri? = data?.data
                userImageUri = data?.data
                if (imageUri != null) {
                    context?.let {
                        Glide.with(it)
                            .load(imageUri)
                            .centerCrop()
                            .circleCrop()
                            .into(userImg!!)
                    }

                    adjustBtn!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                }

            }

        }

    }

}