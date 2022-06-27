package technology.dubaileading.maccessuser.ui.check_in

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
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
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivityCheckInBinding
import technology.dubaileading.maccessuser.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessuser.rest.entity.CheckInRequest
import technology.dubaileading.maccessuser.rest.entity.CheckInResponse
import technology.dubaileading.maccessuser.rest.request.ServerRequestFactory
import technology.dubaileading.maccessuser.rest.request.SuccessCallback
import technology.dubaileading.maccessuser.ui.attendance.AttendanceActivity
import technology.dubaileading.maccessuser.ui.check_out.CheckOutJiginActivity
import technology.dubaileading.maccessuser.utils.AppShared
import technology.dubaileading.maccessuser.utils.GPSTracker
import technology.dubaileading.maccessuser.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class CheckInActivity : BaseActivity<ActivityCheckInBinding,CheckInViewModel>(), OnMapReadyCallback {

    private lateinit var gpsTracker : GPSTracker
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //if no timer is set
        val date = AppShared(applicationContext).getTiming()
        if (!date!!.isEmpty()) {
            startActivity(Intent(applicationContext, CheckOutJiginActivity::class.java))
            finish()
        }

//        binding.timeSheet.setOnClickListener{
//            startActivity(Intent(applicationContext,AttendanceActivity::class.java))
//        }

//        binding.timesheet.setOnClickListener{
//            startActivity(Intent(applicationContext,AttendanceActivity::class.java))
//        }

        binding.timeS.setOnClickListener{
            startActivity(Intent(applicationContext,AttendanceActivity::class.java))
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val gifFromAssets = GifDrawable(assets, "start_shift.gif")
        binding.gif.setImageDrawable(gifFromAssets)

        gpsTracker = GPSTracker(this)
        gpsTracker.location

        MapsInitializer.initialize(this)

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        checkLocationPermission()

        binding.shiftBt.setOnClickListener{
            val builder = AlertDialog.Builder(this@CheckInActivity)
            builder.setTitle("Confirm Check In")
            builder.setMessage("Do you wish to check in to work now.?")
            builder.setNegativeButton("Not Now", null)
            builder.setPositiveButton(
                "Confirm"
            ) { _, _ -> processChecking() }
            builder.show()
        }
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

                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    val nowTime = sdf.format(Date())

                    AppShared(applicationContext).saveTiming(nowTime)
                    AppShared(applicationContext).savePlace(binding.placeName.text.toString())

                    startActivity(Intent(applicationContext, CheckOutJiginActivity::class.java))
                    finish()

                }
            }
            ) { Toast.makeText(this@CheckInActivity, "error", Toast.LENGTH_LONG).show() }.build()
        request.executeAsync()
    }


    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this@CheckInActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@CheckInActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@CheckInActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@CheckInActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
    }

    override fun createViewModel(): CheckInViewModel {
        return ViewModelProvider(this).get(CheckInViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityCheckInBinding {
        return ActivityCheckInBinding.inflate(layoutInflater)
    }

    fun getAddress(currentLoc: LatLng): String? {
        return try {
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(
                currentLoc.latitude,
                currentLoc.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses[0].getAddressLine(0)
        } catch (e: Exception) {
            e.toString()
        }
//        return "";
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val currentLoc = LatLng(gpsTracker.latitude, gpsTracker.longitude)
        val address : String? = getAddress(currentLoc)
        binding.placeName.text = address

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).zoom(18f).build()
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
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this@CheckInActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(
                            this@CheckInActivity,
                            "Permission Granted",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(applicationContext,CheckInActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}