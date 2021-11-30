package com.example.pindergarten_android

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


class Fragment_addPost: Fragment() {

    private var myContext: FragmentActivity? = null
    var addPhoto : ImageButton ?=null
    private lateinit var callback: OnBackPressedCallback

    var list = ArrayList<Uri>()
    val adapter = MultiImageAdapter(list,this)

    var image_count : TextView ?= null
    var postText : EditText ?= null
    var addPostBtn : TextView ?=null
    var imm : InputMethodManager ?=null
    var fragment : String ?=null


    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)
    var mainAct : MainActivity ?=null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_addpost,container,false)

        //navigate hide
        mainAct = activity as MainActivity
        mainAct!!.HideBottomNavigation(true)

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            fragment = bundle.getString("fragment").toString()
        }

        var parentlayout : ConstraintLayout = view.findViewById(R.id.parentlayout)
        parentlayout.setOnClickListener{
            //keyboard control
            imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken,0)

        }

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        image_count = view.findViewById(R.id.image_count)

        addPhoto = view.findViewById(R.id.addPhoto_btn)
        addPhoto!!.setOnClickListener{
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.action= Intent.ACTION_GET_CONTENT

            startActivityForResult(intent,200)
        }

        addPostBtn = view.findViewById(R.id.addPostBtn)
        addPostBtn!!.isEnabled=true
        postText = view.findViewById(R.id.postText)
        postText!!.requestFocus()
        postText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(list.size!=0 && postText!!.text.isNotEmpty()){
                    addPostBtn!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addPostBtn!!.setTextColor(Color.LTGRAY)
                }
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(list.size!=0 && postText!!.text.isNotEmpty()){
                    addPostBtn!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addPostBtn!!.setTextColor(Color.LTGRAY)
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(list.size!=0 && postText!!.text.isNotEmpty()){
                    addPostBtn!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addPostBtn!!.setTextColor(Color.LTGRAY)
                }
            }
        })

        addPostBtn!!.setOnClickListener{
            Log.i("게시물 등록","clicked")
            addPostBtn!!.isEnabled=false
            if(list.size ==0){
                //1개이상 사진업로드 공고메세지
                Log.i("게시물 등록","fail (1개이상 업로드)")
                val view2 = View.inflate(context,R.layout.join_popup,null)
                var text : TextView = view2.findViewById(R.id.text)
                var button : Button = view2.findViewById(R.id.button)
                text.text="게시물 등록시 1개 이상의\n" + "사진이 필요합니다."
                button.text="확인"
                val alertDialog = AlertDialog.Builder(context).create()
                button.setOnClickListener{
                    alertDialog.dismiss()
                    addPostBtn!!.isEnabled=true
                }
                alertDialog.setView(view2)
                alertDialog.show()
            }
            else if(list.size>0 && postText!!.text.isNotEmpty()){
                //keyboard control
                Log.i("게시물 등록","api 연동")
                imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(view.windowToken,0)

                val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }

                //content
                var content = HashMap<String, RequestBody>()
                content["content"]=  RequestBody.create(MediaType.parse("text/plain"),postText?.text.toString())

                //image
                var images = ArrayList<MultipartBody.Part>()

                for ( index in 0 until list.size){


                    var file = File(list[index].path)
                    Log.i("file",list[index].toString())

                    var inputStream : InputStream?= null
                    try{
                        inputStream = context?.contentResolver?.openInputStream(list[index]!!)!!
                    }catch (e : IOException){
                        e.printStackTrace()
                    }

                    val bitmap = BitmapFactory.decodeStream(inputStream,null,BitmapFactory.Options())
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)

                    val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray())
                    val uploadFile = MultipartBody.Part.createFormData("images", file.name, requestBody)

                    Log.i("images",uploadFile.toString())
                    images.add(uploadFile)
                }

                if (content != null) {

                    //서버 : addPost
                    apiService.addPostAPI(sharedPreferences.toString(), content, images)
                        ?.enqueue(object : Callback<Post?> {
                            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                                Log.i("addPost", response.body()?.success.toString())

                                val view2 = View.inflate(myContext, R.layout.join_popup, null)
                                var text: TextView = view2.findViewById(R.id.text)
                                var button: Button = view2.findViewById(R.id.button)
                                text.text = "게시물이 정상적으로 등록 되었습니다."
                                button.text = "확인"
                                addPostBtn!!.isEnabled=true

                                val alertDialog = AlertDialog.Builder(context).create()
                                button.setOnClickListener {
                                    alertDialog.dismiss()
                                    if (fragment == "socialPet") {
                                        val transaction =
                                            myContext!!.supportFragmentManager.beginTransaction()
                                        val fragment: Fragment = Fragment_socialPet()
                                        val bundle = Bundle()
                                        fragment.arguments = bundle
                                        transaction.replace(R.id.container, fragment)
                                        transaction.commit()
                                        val fm: FragmentManager = requireActivity().supportFragmentManager
                                        fm.popBackStack()
                                        fm.popBackStack()

                                    } else if (fragment == "meAndPet") {
                                        val transaction =
                                            myContext!!.supportFragmentManager.beginTransaction()
                                        val fragment: Fragment = Fragment_meAndPet()
                                        val bundle = Bundle()
                                        fragment.arguments = bundle
                                        transaction.replace(R.id.container, fragment)
                                        transaction.commit()
                                        val fm: FragmentManager = requireActivity().supportFragmentManager
                                        fm.popBackStack()
                                        fm.popBackStack()
                                    }

                                }
                                alertDialog.setView(view2)
                                alertDialog.show()

                                Log.i("addPost: ", "성공")
                            }

                            override fun onFailure(call: Call<Post?>, t: Throwable) {
                                addPostBtn!!.isEnabled=true
                                Log.i("addPost 실패: ", t.toString())

                            }

                        })
                }

            }
        }

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            mainAct!!.onBackPressed()

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

        if(resultCode==RESULT_OK &&requestCode==200){
            list.clear()

            if(data?.clipData!=null){
                val count = data.clipData!!.itemCount
                Log.i("imageUriCount",count.toString())

                if(list.size!=0 && postText!!.text.isNotEmpty()){
                    addPostBtn!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addPostBtn!!.setTextColor(Color.LTGRAY)
                }

                if(count>10){
                    val view2 = View.inflate(context,R.layout.join_popup,null)
                    var text : TextView = view2.findViewById(R.id.text)
                    var button : Button = view2.findViewById(R.id.button)
                    text.text="사진은 10장까지 가능합니다."
                    button.text="확인"
                    val alertDialog = AlertDialog.Builder(context).create()
                    button.setOnClickListener{ alertDialog.dismiss() }
                    alertDialog.setView(view2)
                    alertDialog.show()
                    return
                }
                else{
                    for(i in 0 until count) {

                        val imageUri = data.clipData!!.getItemAt(i).uri
                        list.add(imageUri)
                        image_count!!.text = "${count}/10"
                    }
                }
            }
            else{
                data?.data?.let{
                        uri ->
                    val imageUri : Uri?= data?.data
                    if(imageUri!=null){
                        list.add(imageUri)
                        image_count!!.text = "1/10"
                        if(list.size!=0 && postText!!.text.isNotEmpty()){
                            addPostBtn!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                        }
                        else{
                            addPostBtn!!.setTextColor(Color.LTGRAY)
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }
    }


}