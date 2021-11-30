package com.example.pindergarten_android

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Fragment_postDeclare : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var moveFragment : String ?=null
    var imm : InputMethodManager?=null


    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_post_declare,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var postId : Int
        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            postId = bundle.getInt("postId")
            moveFragment = bundle.getString("moveFragment")
            Log.i("bundle_postId",postId.toString())
        }
        else {
            postId  = -1
        }


        var type : Int = -1
        //spinner data
        val items = listOf("광고성 글","스팸 컨텐츠","욕설/비방/혐오","노골적인 폭력 묘사")
        var titleDeclare = view.findViewById<EditText>(R.id.textTitle)
        var declareText = view.findViewById<EditText>(R.id.declareText)
        val addDeclareBtn = view.findViewById<TextView>(R.id.addDeclareBtn)
        addDeclareBtn.isEnabled=true
        val spinner: Spinner = view.findViewById(R.id.spinner)

        var parentlayout : ConstraintLayout = view.findViewById(R.id.parentlayout)
        parentlayout.setOnClickListener{
            //keyboard control
            imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken,0)

        }


        titleDeclare.requestFocus()
        declareText.requestFocus()

        titleDeclare?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(type!=-1 && titleDeclare.text.isNotEmpty() && declareText.text.isNotEmpty()){
                    addDeclareBtn.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addDeclareBtn.setTextColor(Color.LTGRAY)
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(type!=-1 && titleDeclare.text.isNotEmpty() && declareText.text.isNotEmpty()){
                    addDeclareBtn.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addDeclareBtn.setTextColor(Color.LTGRAY)
                }
            }
        })

        declareText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(type!=-1 && titleDeclare.text.isNotEmpty() && declareText.text.isNotEmpty()){
                    addDeclareBtn.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addDeclareBtn.setTextColor(Color.LTGRAY)
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(type!=-1 && titleDeclare.text.isNotEmpty() && declareText.text.isNotEmpty()){
                    addDeclareBtn.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addDeclareBtn.setTextColor(Color.LTGRAY)
                }
            }
        })


        val adapter = object : ArrayAdapter<String>(requireContext(),R.layout.spinner_item){

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getView(position, convertView, parent)
                var text1 = v.findViewById<TextView>(R.id.text)

                if (position == count) {
                   text1.text = ""
                   text1.hint = getItem(count)
                }

                return v
            }

            override fun getCount(): Int {
                return super.getCount() -1
            }
        }

        adapter.addAll(items)
        adapter.add("신고 유형을 선택하세요")
        spinner.adapter = adapter
        spinner.setSelection(adapter.count)
        spinner.dropDownVerticalOffset = dipToPixels(45f).toInt()

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = position
                if(type!=-1 && titleDeclare.text.isNotEmpty() && declareText.text.isNotEmpty()){
                    addDeclareBtn.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    addDeclareBtn.setTextColor(Color.LTGRAY)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        addDeclareBtn.setOnClickListener{
            Log.i("신고 접수 버튼눌림","ok")
            addDeclareBtn.isEnabled=false

            //keyboard control
            imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken,0)

            if(type!=-1 && titleDeclare.text.isNotEmpty() && declareText.text.isNotEmpty()){

            var declare: HashMap<String, String> = HashMap()
            declare["title"] = titleDeclare?.text.toString()
            declare["content"] = declareText?.text.toString()

            Log.i("type",type.toString())
            if(type!=0){
                apiService.declarePostAPI(sharedPreferences.toString(),postId, declare,type)?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                        //신고 완료 메세지
                        val dialogBuilder = AlertDialog.Builder(requireActivity())
                        val view = inflater.inflate(R.layout.join_popup, null)
                        var text : TextView = view.findViewById(R.id.text)
                        var button : Button = view.findViewById(R.id.button)
                        text.text="신고접수 되었습니다."
                        button.text="확인"
                        addDeclareBtn.isEnabled=true
                        val alertDialog = dialogBuilder.create()
                        button.setOnClickListener{
                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_postdetail()
                            val bundle = Bundle()
                            bundle.putString("moveFragment",moveFragment)
                            bundle.putInt("postId", postId)
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                            alertDialog!!.dismiss()


                        }
                        alertDialog.setView(view)
                        alertDialog.show()
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("declare",t.stackTraceToString())
                    }

                })
            }
        }
        }

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_postdetail()
            val bundle = Bundle()
            bundle.putInt("postId", postId)
            bundle.putString("moveFragment",moveFragment)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)

    }


    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )}

}

