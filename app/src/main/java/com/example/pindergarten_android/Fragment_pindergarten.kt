package com.example.pindergarten_android

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.toRadians
import kotlin.math.pow


class Fragment_pindergarten : Fragment(),OnMapReadyCallback{

    private var myContext: FragmentActivity? = null

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    var mapView: MapView ?=null
    var current_latitude :Double ?=null
    var current_longitude :Double ?=null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var moved :String ?= null
    var moved_latitude : Double ?=null
    var moved_longitude : Double ?=null
    var pindergartenTargetId : Int ?=null

    var panel : SlidingUpPanelLayout ?=null
    var currentMarker : Marker ?=null

    var pindergartenId = ArrayList<Int>()
    var pindergartenName = ArrayList<String>()
    var pindergartenAddress = ArrayList<String>()
    var pindergartenThumbnail = ArrayList<Uri>()
    var pindergartenRating = ArrayList<Double>()
    var pindergartenIsLiked = ArrayList<Int>()
    var pindergartenLocation = ArrayList<LatLng>()
    var pindergartenDistance = ArrayList<String>()
    var locationManager : LocationManager?=null

    var recyclerView : RecyclerView ?=null
    val adapter = pindergartenAdapter(pindergartenThumbnail, pindergartenName, pindergartenAddress, pindergartenRating, pindergartenIsLiked, pindergartenDistance,this)

    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_pindergarten,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(false)

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            if(bundle.getString("moved")!=null){
                Log.i("detailPindergarten","back")
                moved = bundle.getString("moved")
                pindergartenTargetId = bundle.getInt("pindergartenId")
                moved_latitude = bundle.getDouble("latitude")
                moved_longitude = bundle.getDouble("longitude")
            }
        }
        else {
            Log.i("pindergartenId", null.toString())
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = adapter

        mapView =view.findViewById(R.id.map_view)
        mapView!!.onCreate(savedInstanceState)
        //맵 가져오기
        mapView!!.getMapAsync(this@Fragment_pindergarten)


        adapter.setItemClickListener( object : pindergartenAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("clicked: ", "${pindergartenName[position]}이 클릭되었습니다. ")
                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_detailPindergarten()
                val bundle = Bundle()
                bundle.putString("moved","map")
                current_latitude?.let { it1 -> bundle.putDouble("latitude", it1) }
                current_longitude?.let { it1 -> bundle.putDouble("longitude", it1) }
                bundle.putInt("pindergartenId",pindergartenId[position])
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })

        adapter.setLikedClickListener(object:pindergartenAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                if(pindergartenIsLiked[position]==0){
                    //좋아요 누름
                    pindergartenIsLiked[position]=1
                }
                else{
                    //좋아요 취소
                    pindergartenIsLiked[position]=0
                }

                //일정 부분의 adapter update
                adapter.notifyItemChanged(position)
                //좋아요 변경 API
                val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
                Log.i("jwt : ",sharedPreferences.toString())
                var temp: HashMap<String, String> = HashMap()
                apiService.pindergartenLikeAPI(pindergartenId[position],sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("pindergarten Liked: ","수정 성공")
                    }
                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("pindergarten Detail: ","fail")
                    }

                })


            }

        })

        var likePindergarten = view.findViewById<ImageButton>(R.id.like_Pindergarten)
        var searchPindergarten = view.findViewById<ImageButton>(R.id.search_Pindergarten)

        likePindergarten.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_likePindergarten()
            val bundle = Bundle()
            current_latitude?.let { it1 -> bundle.putDouble("latitude", it1) }
            current_longitude?.let { it1 -> bundle.putDouble("longitude", it1) }
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        searchPindergarten.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_searchPindergarten()
            val bundle = Bundle()
            current_latitude?.let { it1 -> bundle.putDouble("latitude", it1) }
            current_longitude?.let { it1 -> bundle.putDouble("longitude", it1) }
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        panel = view.findViewById(R.id.main_panel)
        panel!!.panelHeight = changeDP(20)


        return view
    }

    fun distance(myLocation:LatLng,pindergartenLocation:LatLng): String {

        var lat1 = myLocation.latitude
        var lat2 = pindergartenLocation.latitude
        var lon1 = myLocation.longitude
        var lon2 = myLocation.longitude

        val dLat = toRadians(lat2 - lat1)
        val dLon = toRadians(lon2 - lon1)
        val a = kotlin.math.sin(dLat / 2).pow(2.0) + kotlin.math.sin(dLon / 2).pow(2.0) * kotlin.math.cos(toRadians(lat1)) * kotlin.math.cos(toRadians(lat2))
        val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
        return String.format("%.1f",6372.8 * 1000 * c)
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }


    override fun onMapReady(map: NaverMap) {
        Log.i("Naver map","ready")
        naverMap = map

        // 현위치 버튼 기능
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true
        uiSetting.logoGravity= Gravity.TOP
        uiSetting.isCompassEnabled = false
        uiSetting.setLogoMargin(10,10,0,0)

       naverMap.setOnMapClickListener{point,coord ->
           panel!!.panelHeight = changeDP(20)
           if(currentMarker!=null){
               currentMarker!!.icon = OverlayImage.fromResource(R.drawable.marker)
               currentMarker!!.width = 70
               currentMarker!!.height = 70
           }
       }

        //focus
        panel!!.panelHeight = changeDP(20)
        panel!!.setOnClickListener{
            if(panel!!.panelHeight==changeDP(20)){
                recyclerView?.smoothScrollToPosition(0)
            }
            else if(panel!!.panelHeight<changeDP(250)){
                panel!!.requestFocus()
            }
            else{
                recyclerView!!.requestFocus()
            }
        }
        recyclerView!!.setOnClickListener{
            if(panel!!.panelHeight<changeDP(250)){
                panel!!.requestFocus()
            }
            else{
                recyclerView!!.requestFocus()
            }
        }

        // -> onRequestPermissionsResult // 위치 권한 요청
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode=LocationTrackingMode.Follow

        //현재위치
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var userLocation: Location = getLatLng()

        current_latitude = userLocation.latitude
        current_longitude = userLocation.longitude

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())
        Log.i("현재위치", "위도: ${ current_latitude} | 경도: ${ current_longitude}")


            //서버: 전체 펫유치원 조회
            apiService.searchAllPindergartenAPI(sharedPreferences.toString(), current_latitude!!,current_longitude!!)?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                    Log.i("pindergarten Search", response.body()?.success.toString())

                    pindergartenId.clear()
                    pindergartenName.clear()
                    pindergartenAddress.clear()
                    pindergartenThumbnail.clear()
                    pindergartenRating.clear()
                    pindergartenIsLiked.clear()
                    pindergartenLocation.clear()
                    pindergartenDistance.clear()

                    for (i in 0 until response.body()?.allPindergartenList?.size!!) {
                        pindergartenId.add(Integer.parseInt(response.body()?.allPindergartenList!![i].id.toString()))
                        pindergartenName.add(response.body()?.allPindergartenList!![i].name.toString())
                        pindergartenAddress.add(response.body()?.allPindergartenList!![i].address.toString())
                        pindergartenThumbnail.add(Uri.parse(response.body()?.allPindergartenList!![i].thumbnail.toString()))
                        pindergartenRating.add(
                            response.body()?.allPindergartenList!![i].rating.toString().toDouble()
                        )
                        pindergartenIsLiked.add(Integer.parseInt(response.body()?.allPindergartenList!![i].isLiked.toString()))
                        pindergartenLocation.add(
                            LatLng(
                                response.body()?.allPindergartenList!![i].latitude!!.toDouble(),
                                response.body()?.allPindergartenList!![i].longitude!!.toDouble()
                            )
                        )
                        pindergartenDistance.add(String.format("%.1f", response.body()?.allPindergartenList!![i].distance!!.toDouble()) + "Km")
                        Log.i("${i + 1}번째 pindergarten 조회", pindergartenName[i])

                    }

                    //펫유치원 표시
                    for (i in 0 until pindergartenLocation.size) {
                        val marker = Marker()
                        marker.position = pindergartenLocation[i]
                        marker.map = naverMap
                        marker.tag = pindergartenId[i]
                        marker.icon = OverlayImage.fromResource(R.drawable.marker)
                        marker.width = 80
                        marker.height = 80
                        Log.i("${i + 1}번째 pindergarten", pindergartenName[i])

                        marker.onClickListener = Overlay.OnClickListener {

                            //marker image 변경
                            if(currentMarker!=null){
                                currentMarker!!.icon = OverlayImage.fromResource(R.drawable.marker)
                                currentMarker!!.width = 80
                                currentMarker!!.height = 80
                            }
                            currentMarker = marker
                            currentMarker!!.icon = OverlayImage.fromResource(R.drawable.marker2)
                            marker.width = 100
                            marker.height = 100

                            apiService.markerAPI(sharedPreferences.toString(), marker.position.latitude!!,marker.position.longitude!!)?.enqueue(object : Callback<Post?> {
                                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                                    Log.i("marker event","success")
                                    val cameraUpdate = CameraUpdate.scrollTo(LatLng(marker.position.latitude!!,marker.position.longitude!!))
                                    naverMap.moveCamera(cameraUpdate)
                                    panel!!.panelHeight = changeDP(250)

                                    pindergartenId.clear()
                                    pindergartenName.clear()
                                    pindergartenAddress.clear()
                                    pindergartenThumbnail.clear()
                                    pindergartenRating.clear()
                                    pindergartenIsLiked.clear()
                                    pindergartenLocation.clear()
                                    pindergartenDistance.clear()

                                    for (i in 0 until response.body()?.nearPindergartens?.size!!) {
                                        pindergartenId.add(Integer.parseInt(response.body()?.nearPindergartens!![i].id.toString()))
                                        pindergartenName.add(response.body()?.nearPindergartens!![i].name.toString())
                                        pindergartenAddress.add(response.body()?.nearPindergartens!![i].address.toString())
                                        pindergartenThumbnail.add(Uri.parse(response.body()?.nearPindergartens!![i].thumbnail.toString()))
                                        pindergartenRating.add(
                                            response.body()?.nearPindergartens!![i].rating.toString().toDouble()
                                        )
                                        pindergartenIsLiked.add(Integer.parseInt(response.body()?.nearPindergartens!![i].isLiked.toString()))
                                        pindergartenLocation.add(
                                            LatLng(
                                                response.body()?.nearPindergartens!![i].latitude!!.toDouble(),
                                                response.body()?.nearPindergartens!![i].longitude!!.toDouble()
                                            )
                                        )
                                        try{
                                            pindergartenDistance.add(String.format("%.1f", response.body()?.nearPindergartens!![i].distance!!.toDouble()) + "Km")
                                        }catch(e :Exception){
                                            Log.i("error",e.toString())
                                        }
                                        //pindergartenDistance.add(String.format("%.1f", response.body()?.nearPindergartens!![i].distance!!.toDouble()) + "Km")
                                        Log.i("${i + 1}번째 pindergarten 조회", pindergartenName[i])
                                    }

                                    //최상단으로 이동
                                    recyclerView?.smoothScrollToPosition(0)
                                    adapter.notifyDataSetChanged()
                                }

                                override fun onFailure(call: Call<Post?>, t: Throwable) {
                                    Log.i("marker event fail",t.stackTraceToString())
                                }

                            })
                            true
                        }

                    }


                    if(moved=="map"){
                        apiService.markerAPI(sharedPreferences.toString(), moved_latitude!!,moved_longitude!!)?.enqueue(object : Callback<Post?> {
                            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                                Log.i("moved event","success")

                                pindergartenId.clear()
                                pindergartenName.clear()
                                pindergartenAddress.clear()
                                pindergartenThumbnail.clear()
                                pindergartenRating.clear()
                                pindergartenIsLiked.clear()
                                pindergartenLocation.clear()
                                pindergartenDistance.clear()

                                for (i in 0 until response.body()?.nearPindergartens?.size!!) {
                                    pindergartenId.add(Integer.parseInt(response.body()?.nearPindergartens!![i].id.toString()))
                                    pindergartenName.add(response.body()?.nearPindergartens!![i].name.toString())
                                    pindergartenAddress.add(response.body()?.nearPindergartens!![i].address.toString())
                                    pindergartenThumbnail.add(Uri.parse(response.body()?.nearPindergartens!![i].thumbnail.toString()))
                                    pindergartenRating.add(
                                        response.body()?.nearPindergartens!![i].rating.toString().toDouble()
                                    )
                                    pindergartenIsLiked.add(Integer.parseInt(response.body()?.nearPindergartens!![i].isLiked.toString()))
                                    pindergartenLocation.add(LatLng(response.body()?.nearPindergartens!![i].latitude!!.toDouble(), response.body()?.nearPindergartens!![i].longitude!!.toDouble()))


                                    if(response.body()?.nearPindergartens!![i].distance!!.toDouble()!=null){
                                        pindergartenDistance.add(String.format("%.1f", response.body()?.nearPindergartens!![i].distance!!.toDouble()) + "Km")
                                    }
                                    else{
                                        Log.i("nearPindergarten","Null error")
                                    }

                                    Log.i("${i + 1}번째 pindergarten 조회", pindergartenName[i])
                                }

                                adapter.notifyDataSetChanged()
                                val cameraUpdate = CameraUpdate.scrollTo(LatLng(moved_latitude!!,moved_longitude!!))
                                naverMap.moveCamera(cameraUpdate)
                                panel!!.panelHeight = changeDP(250)
                                //최상단으로 이동
                                recyclerView?.smoothScrollToPosition(0)
                            }

                            override fun onFailure(call: Call<Post?>, t: Throwable) {
                                Log.i("moved event fail",t.stackTraceToString())
                            }

                        })
                    }

                    panel!!.panelHeight = changeDP(20)
                    //최상단으로 이동
                    recyclerView?.smoothScrollToPosition(0)
                    adapter.notifyDataSetChanged()
                    Log.i("pindergarten 조회", "성공")
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("pindergarten 조회", t.toString())
                }

            })
        }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE)
            return

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                // 권한 설정 거부시 위치 추적을 사용하지 않음
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }

            return
        }
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i("callback","뒤로가기")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun getLatLng(): Location{
        var currentLatLng: Location? = null
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION)
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            val locatioNProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locationManager?.getLastKnownLocation(locatioNProvider)
        }else{
            currentLatLng = getLatLng()
        }
        return currentLatLng!!
    }

    private fun changeDP(value: Int): Int {
        var displayMetrics = requireContext().resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
