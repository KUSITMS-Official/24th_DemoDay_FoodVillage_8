package com.example.foodvillage

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.foodvillage.databinding.ActivityCurrentAddress2Binding
import com.google.android.gms.location.*
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.SimpleDateFormat
import java.util.*

open class CurrentAddressActivity2 : AppCompatActivity(), MapView.CurrentLocationEventListener {
    private var mBinding: ActivityCurrentAddress2Binding? = null
    private val binding get() = mBinding!!
    private var mapView: MapView? = null

    var curr_lat = 37.5406564
    var curr_lon = 126.8809048
    var AddressData: String = ""
    var marker_distance: Int = 0

    var marker_dist: Int = 0

    // 위치 추적을 위한 변수들
    val TAG: String = "로그"

    private var mFusedLocationProviderClient: FusedLocationProviderClient? =
        null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    private lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_address2)

        // 바인딩
        mBinding = ActivityCurrentAddress2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvActivityCurrentAddressSettingMylocation.text=intent.getStringExtra("AddressData")

        binding.tvActivityCurrentAddressSettingMylocation2.text =
            intent.getStringExtra("etActivityDetailAddress")

        binding.btnActivityCurrentAddress2Cancel.setOnClickListener {
            //binding.tvMymapactivityMysavedlocation.setText("현재 위치: " + curr_lat + ", " + curr_lon)
//            intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            this.finish()
        }

        // 맵
        // 임포트 잘 해줘야함... mf들어간걸로!
        mapView = MapView(this)
        binding.clKakaoMapView.addView(mapView)
        mapView!!.setCurrentLocationEventListener(this);

        // 현재 위치
        // LocationRequest() deprecated 되서 아래 방식으로 LocationRequest 객체 생성
        // mLocationRequest = LocationRequest() is deprecated
        mLocationRequest = LocationRequest.create().apply {
            interval = 1000 // 업데이트 간격 단위(밀리초)
            //fastestInterval = 1000 // 가장 빠른 업데이트 간격 단위(밀리초)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확성
            //maxWaitTime= 2000 // 위치 갱신 요청 최대 대기 시간 (밀리초)
        }

        // 위치 추척 시작
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()

            // 현위치 트래킹 - 이건 주소 설정할 때 해서 최초로 받는거
            mapView!!.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            //TrackingModeOnWithMarkerHeadingWithoutMapMoving);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    protected fun startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates()")

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "startLocationUpdates() 두 위치 권한중 하나라도 없는 경우 ")
            return
        }
        Log.d(TAG, "startLocationUpdates() 위치 권한이 하나라도 존재하는 경우")
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청합니다.
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )

        // 여기서 curr_lat, lon 바뀌
        mFusedLocationProviderClient!!.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("위치", "updateCurrent")
                curr_lat = location!!.latitude
                curr_lon = location.longitude
                Log.d("위치", "updated: $curr_lat, $curr_lon")

                // 내 위치로 중심 이동
                val mapPoint = MapPoint.mapPointWithGeoCoord(curr_lat, curr_lon)
                mapView?.setMapCenterPoint(mapPoint, true)
                mapView?.setZoomLevel(2, true)

                // 다 보이게 레벨 조정
                //mapView!!.fitMapViewAreaToShowAllPOIItems()
            }
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "onLocationResult()")
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged()")
        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("hh:mm:ss a")
//        txtTime.text = "Updated at : " + simpleDateFormat.format(date) // 갱신된 날짜
//        txtLat.text = "LATITUDE : " + mLastLocation.latitude // 갱신 된 위도
//        txtLong.text = "LONGITUDE : " + mLastLocation.longitude // 갱신 된 경도
        //수원 화성의 위도, 경도
        val mapPoint = MapPoint.mapPointWithGeoCoord(
            mLastLocation.latitude,
            mLastLocation.longitude
        )
        Log.d(TAG, "위도: " + mLastLocation.latitude + ", 경도: " + mLastLocation.longitude)
        /*
            여기서 현 위치 로그가 나오는데, 지도찾기할 때 이거 사용하고
            버튼 클릭 - 현 위치로 설정? -> 예 선택 시 저장
            -> 아니오시 계속 트래킹!

         */
    }

    // 위치 권한이 있는지 확인하는 메서드
    fun checkPermissionForLocation(context: Context): Boolean {
        Log.d(TAG, "checkPermissionForLocation()")
        // Android 6.0 Marshmallow 이상에서는 지리 확보(위치) 권한에 추가 런타임 권한이 필요합니다.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "checkPermissionForLocation() 권한 상태 : O")
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                Log.d(TAG, "checkPermissionForLocation() 권한 상태 : X")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult()")
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 클릭")
                startLocationUpdates()

                // 현위치 트래킹
                mapView!!.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithMarkerHeadingWithoutMapMoving;

            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCurrentLocationUpdate(p0: MapView, p1: MapPoint, p2: Float) {
        val mapPointGeo: MapPoint.GeoCoordinate = p1.getMapPointGeoCoord()
        Log.i(
            "로그",
            java.lang.String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                mapPointGeo.latitude,
                mapPointGeo.longitude,
                p2
            )
        )
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
    }
}