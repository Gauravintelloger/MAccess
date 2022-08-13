package technology.dubaileading.maccessemployee.ui.check_in

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pl.droidsonroids.gif.GifDrawable
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityCheckInBinding
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.CheckInRequest
import technology.dubaileading.maccessemployee.rest.entity.CheckInResponse
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.attendance.AttendanceActivity
import technology.dubaileading.maccessemployee.ui.check_out.CheckOutActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.GPSTracker
import technology.dubaileading.maccessemployee.utils.Utils
import java.text.SimpleDateFormat
import java.util.*


class CheckInActivity : BaseActivity<ActivityCheckInBinding,CheckInViewModel>(), OnMapReadyCallback, LocationListener {

    private lateinit var gpsTracker : GPSTracker
    private lateinit var mMap: GoogleMap
    var is_mock : Boolean = false
    private val MIN_TIME: Long = 400
    private val MIN_DISTANCE = 1000f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //if no timer is set
        val date = AppShared(applicationContext).getTiming()
        if (date!!.isNotEmpty()) {
            startActivity(Intent(applicationContext, CheckOutActivity::class.java))
            finish()
        }


        binding.timeSheet.setOnClickListener{
            startActivity(Intent(applicationContext,AttendanceActivity::class.java))
        }

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        val gifFromAssets = GifDrawable(assets, "start_shift.gif")
        binding.gif.setImageDrawable(gifFromAssets)

        MapsInitializer.initialize(this)

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        checkLocationPermission()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            this
        );


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsTracker = GPSTracker(this)
            gpsTracker.location
        }else {
            showAlert("You need give location access to use app")
        }

        binding.shiftBt.setOnClickListener{
            is_mock = isMockLocationOn(
                gpsTracker.location,
                applicationContext
            ) || isMockLocationOn(
                gpsTracker.location,
                applicationContext
            )
            if (is_mock){
                showAlert("Please disable fake location to be able to mark attendance")
            } else if (!isAutoTimeZoneEnabled()){
                showAlert("Please enable the Automatic time zone to be able to mark attendance")
            } else {
                startShift()
            }
        }
    }

    private fun startShift() {
        val builder = AlertDialog.Builder(this@CheckInActivity)
        builder.setTitle("Confirm Check In")
        builder.setMessage("Do you wish to check in to work now.?")
        builder.setNegativeButton("Not Now", null)
        builder.setPositiveButton(
            "Confirm"
        ) { _, _ -> processChecking() }
        builder.show()
    }

    private fun processChecking() {

        var checkInRequest = CheckInRequest(
            mode = 1,
            date = Utils.getCurrentDate(),
            time = Utils.getCurrentTime(),
            lat_long = gpsTracker.latitude.toString() + "," + gpsTracker.longitude,
        )

        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .checkIN(checkInRequest)

        val request = requestFactory.newHttpRequest<Any>(this@CheckInActivity)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<CheckInResponse?> {
                override fun onSuccess(user: CheckInResponse?) {
                    if (user?.status == "ok") {
                        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        val nowTime = sdf.format(Date())

                        AppShared(applicationContext).saveTiming(nowTime)
                        AppShared(applicationContext).savePlace(binding.placeName.text.toString())

                        startActivity(Intent(applicationContext, CheckOutActivity::class.java))
                        finish()
                    } else if (user?.status == "notok" && user?.statuscode == "500"){
                        Toast.makeText(this@CheckInActivity, "Token expired", Toast.LENGTH_LONG).show()
                        AppShared(this@CheckInActivity).saveToken("")

                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    }else{
                             AlertDialog.Builder(this@CheckInActivity)
                            .setTitle("Alert")
                            .setMessage(user?.message)
                            .setPositiveButton(
                                "Ok"
                            ) { dialog, which ->
                            }
                            .show()
                    }

                }
            }
            ) {
                Toast.makeText(this@CheckInActivity, "error"  , Toast.LENGTH_LONG).show()
            }.build()

        request.executeAsync()
    }



    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    private fun isAutoTimeZoneEnabled() =
        Settings.Global.getInt(applicationContext.contentResolver, Settings.Global.AUTO_TIME) != 0

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this@CheckInActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@CheckInActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@CheckInActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@CheckInActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }

    override fun createViewModel(): CheckInViewModel {
        return ViewModelProvider(this).get(CheckInViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityCheckInBinding {
        return ActivityCheckInBinding.inflate(layoutInflater)
    }


    private fun isMockLocationOn(location: Location, context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            location.isFromMockProvider
        } else {
            var mockLocation = "0"
            try {
                mockLocation = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ALLOW_MOCK_LOCATION
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            mockLocation != "0"
        }
    }


    private fun getAddress(currentLoc: LatLng): String? {
        try {
            val addresses: List<Address>
            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(
                currentLoc.latitude,
                currentLoc.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses[0].getAddressLine(0)
        } catch (e: Exception) {
//            e.toString()
        }
        return ""
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermission()

        val currentLoc = LatLng(gpsTracker.latitude, gpsTracker.longitude)
        val address : String? = getAddress(currentLoc)
        binding.placeName.text = address

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(gpsTracker.latitude, gpsTracker.longitude)).zoom(18f).build()
        mMap.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(cameraPosition)
        )
        val marker = MarkerOptions().position(
            LatLng(gpsTracker.latitude, gpsTracker.longitude)
        ).title("")
        mMap.addMarker(marker)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this@CheckInActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this@CheckInActivity, "Permission Granted", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(applicationContext,CheckInActivity::class.java))
                        finish()
                    }
                } else {
                    showAlert("You need give location access to use app")
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun showAlert(msg : String){
        android.app.AlertDialog.Builder(this@CheckInActivity)
            .setTitle("Alert")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(
                "Ok"
            ) { dialog, which ->
                startActivity(Intent(this@CheckInActivity, HomeActivity::class.java))
                finish()
            }
            .show()
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f)
        mMap.animateCamera(cameraUpdate)
        //locationManager!!.removeUpdates(this)
    }



}