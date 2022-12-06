package technology.dubaileading.maccessemployee.ui.check_in

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants.API_RESPONSE_CODE.*
import technology.dubaileading.maccessemployee.databinding.FragmentCheckInBinding
import technology.dubaileading.maccessemployee.locationUtils.GpsSettingsCheckCallback
import technology.dubaileading.maccessemployee.locationUtils.LocationHelper
import technology.dubaileading.maccessemployee.locationUtils.LocationUtils
import technology.dubaileading.maccessemployee.receivers.GpsStatusReceiver
import technology.dubaileading.maccessemployee.rest.entity.CheckInRequest
import technology.dubaileading.maccessemployee.rest.entity.CheckInResponse
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.CustomDialog
import technology.dubaileading.maccessemployee.utils.Utils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CheckInFragment :
    Fragment(),
    OnMapReadyCallback,
    GpsStatusReceiver.OnGpsStateListener, GpsSettingsCheckCallback {
    private val REQUEST_UPDATE_GPS_SETTINGS = 191
    private val viewModel by activityViewModels<ActionViewModel>()
    private var userPosMap: GoogleMap? = null
    private var lastLocation: Location? = null
    private var locationUpdatesReceived = 0
    private var locationHelper: LocationHelper? = null
    private val marker = MarkerOptions()
    private var latitude = 0.0
    private val locationPermissionList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private var isGPSTurnedOn = false
    private var broadcastTriggerCount = 0
    private var isLocationRefreshedOnPunch: Boolean = false
    private var longitude: Double = 0.0
    private var addressLine: String? = ""
    private lateinit var viewBinding: FragmentCheckInBinding
    private lateinit var gpsStatusReceiver: GpsStatusReceiver
    private var isPunchInOutDetailAvailable: Boolean = false

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (isRuntimePermissionGranted()) {

            viewBinding.googleMap.getMapAsync(this)
            locationHelper?.startLocationUpdates()
        } else createAlertForPermission()

    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->
            if (isGranted) {
                viewBinding.googleMap.getMapAsync(this)
                locationHelper?.startLocationUpdates()
            } else {
                createAlertForPermission()

            }
        }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (activity != null && isAdded) {
                val locationReceived = locationResult.lastLocation
                if (LocationUtils.isBetterLocation(locationReceived, lastLocation)) {
                    lastLocation = locationReceived
                }
                locationUpdatesReceived++
                if (locationHelper != null && lastLocation != null && ((lastLocation!!.hasAccuracy() && lastLocation!!.accuracy <= 100)) || locationUpdatesReceived > 3) {
                    stopLocationUpdates()
                    locationUpdatesReceived = 0;
                    latitude = lastLocation!!.latitude
                    longitude = lastLocation!!.longitude
                    addressLine = getStreetName(latitude, longitude)
                    addMarkerOnMap(latitude, longitude)
                    viewBinding.placeName.text = addressLine
                    if (isLocationRefreshedOnPunch) {
                        isLocationRefreshedOnPunch = false
                        punchInOutAttendance()
                    }

                }
            }


        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        SessionManager.init(requireContext())

        viewBinding = FragmentCheckInBinding.inflate(inflater, container, false)
        viewBinding.googleMap.onCreate(savedInstanceState);

        gpsStatusReceiver = GpsStatusReceiver(this)
        setListenerOnViews()
        checkPunchInOutStatus()


        return viewBinding.root
    }

    private fun setListenerOnViews() {

        viewBinding.shiftBt.setOnClickListener {
            if (System.currentTimeMillis() - lastLocation!!.time > 50000) {
                startOrStopLocationUpdates()
                isLocationRefreshedOnPunch = true
            } else {
                punchInOutAttendance()
                Log.d("arun", "punched")
            }
        }

        viewBinding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()

        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        userPosMap = googleMap
        MapsInitializer.initialize(requireActivity())

    }

    fun getStreetName(latitude: Double, longitude: Double): String? {

        var streetName: String? = null
        val geocoder = Geocoder(requireActivity())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            streetName =
                address.getAddressLine(0) + "\n(Accurate to ${lastLocation?.accuracy} meters)"
        }
        return if (!streetName.isNullOrEmpty()) streetName else "Unknown Address Found For Location\n(Accurate to ${lastLocation?.accuracy} meters)"
    }

    private fun isAutoTimeZoneEnabled() =
        Settings.Global.getInt(requireContext().contentResolver, Settings.Global.AUTO_TIME) != 0

    private fun punchInOutAttendance() {
        if (activity != null && isAdded) {
            if (lastLocation?.isFromMockProvider == true) {
                CustomDialog(requireActivity()).showWarningDialog("Some Fake GPS Spoof App is Running om your device.Please remove that from your device otherwise you will not be able to mark attendance\nThanks!")
            } else if (!isAutoTimeZoneEnabled()) {
                CustomDialog(requireActivity()).showWarningDialog("Please enable the Automatic time zone to be able to mark attendance")
            } else {
                val currentDate = Utils.getCurrentDate()
                val currentTime = Utils.getCurrentTime()
                if (latitude == 0.0 || longitude == 0.0) {
                    requireActivity().showToast("Unable to fetch location")
                } else if (currentDate == null) {
                    requireActivity().showToast("Invalid Date")
                } else if (currentTime == null) {
                    requireActivity().showToast("Invalid Time")
                } else {
                    val versionName = getAppVersion(requireContext())
                    val checkInRequest = CheckInRequest(
                        mode = 1,
                        date = currentDate,
                        time = currentTime,
                        lat_long = "$latitude,$longitude",
                        versionName
                    )

                    viewModel.checkIn(checkInRequest)
                    viewModel.checkIn.observe(viewLifecycleOwner, checkInObserver)

                }
            }
        }
    }

    private fun isRuntimePermissionGranted(): Boolean {
        var result: Boolean
        for (permission in locationPermissionList) {
            result = activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    permission
                )
            } == PackageManager.PERMISSION_GRANTED
            if (!result) return false
        }
        return true
    }

    private fun createAlertForPermission() {
        val alertDialog = AlertDialog.Builder(activity).setTitle("Info").setCancelable(false)
            .setMessage("Please allow permission to work app properly")
            .setNegativeButton("No") { dialog12: DialogInterface, which: Int ->
                dialog12.dismiss()
                findNavController().popBackStack()
            }
            .setPositiveButton("Go to Permissions") { dialog1: DialogInterface?, which: Int ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${activity?.packageName}")
                )
                activity?.let {
                    resultLauncher.launch(intent)
                }
            }.create()
        alertDialog.show()
    }


    private fun initLocationHelper() {

        locationHelper = LocationHelper(activity)
        locationHelper?.setRequiredGpsPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationHelper?.init()
        locationHelper?.setLocationCallback(locationCallback)
        locationHelper!!.checkForGpsSettings(this)


    }


    fun addMarkerOnMap(latitude: Double, longitude: Double) {
        userPosMap?.clear()
        marker.position(LatLng(latitude, longitude))
        userPosMap?.addMarker(
            marker
        )?.showInfoWindow()
        val cameraPosition = CameraPosition.Builder().target(
            LatLng(latitude, longitude)
        ).zoom(15f).build()
        userPosMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        viewBinding.googleMap?.onResume()

        requireActivity().registerReceiver(
            gpsStatusReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
        if (isPunchInOutDetailAvailable)
            initLocationHelper()


    }

    override fun onPause() {
        super.onPause()
        viewBinding.googleMap.onPause()
        requireActivity().unregisterReceiver(gpsStatusReceiver)
        locationHelper?.stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.googleMap.onDestroy()
        locationHelper?.stopLocationUpdates()
    }

    override fun onGPsTurnedOnOff(isGpsOn: Boolean) {
        broadcastTriggerCount++
        isGPSTurnedOn = isGpsOn
        if (!isGPSTurnedOn && broadcastTriggerCount > 2) {
            isLocationRefreshedOnPunch = false
            broadcastTriggerCount = 0
            viewBinding.shiftBt.visibility = View.GONE
            Toast.makeText(activity, "Please turn on GPS", Toast.LENGTH_SHORT).show()
            startOrStopLocationUpdates()

        } else if (isGpsOn && broadcastTriggerCount > 2) {
            broadcastTriggerCount = 0
            Toast.makeText(activity, "GPS is ON", Toast.LENGTH_SHORT).show()
            startOrStopLocationUpdates()

        }

    }

    private fun startOrStopLocationUpdates() {
        if (isGPSTurnedOn) {
            if (lastLocation == null || (System.currentTimeMillis() - lastLocation!!.time > 5000)) {
                locationHelper!!.startLocationUpdates()
                viewBinding.progressBar.visibility = View.VISIBLE
                viewBinding.shiftBt.visibility = View.GONE
            } else
                stopLocationUpdates()
        } else {
            locationHelper!!.checkForGpsSettings(this)
        }
    }

    private fun stopLocationUpdates() {
        locationHelper?.stopLocationUpdates()
        viewBinding.progressBar.visibility = View.GONE
        viewBinding.shiftBt.visibility = View.VISIBLE

        val gifFromAssets = GifDrawable(requireContext().assets, "start_shift.gif")
        viewBinding.gif.setImageDrawable(gifFromAssets)
    }

    private fun checkPunchInOutStatus() {
        if (SessionManager.timing?.isNotEmpty() == true) {
            isPunchInOutDetailAvailable = false
            viewBinding.progressBar.visibility = View.GONE
        } else {
            isPunchInOutDetailAvailable = true
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            if (isRuntimePermissionGranted()) {
                viewBinding.googleMap.getMapAsync(this)

            }
            initLocationHelper()
        }

    }


    override fun requiredGpsSettingAreUnAvailable(status: ResolvableApiException) {
        try {
            if (activity != null && isAdded) {
                startIntentSenderForResult(
                    status.resolution.intentSender, REQUEST_UPDATE_GPS_SETTINGS,
                    null,
                    0,
                    0,
                    0,
                    null
                )
            }
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun requiredGpsSettingAreAvailable() {
        isGPSTurnedOn = true
        startOrStopLocationUpdates()
        Log.d("points,", "GPS is turned on")
    }

    override fun gpsSettingsNotAvailable() {
        requireContext().showToast("GPS settings Not Available")
    }

    private fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }

    private var checkInObserver: Observer<DataState<CheckInResponse>> =
        androidx.lifecycle.Observer<DataState<CheckInResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
            }
        }

    private fun validateResponse(body: CheckInResponse) {

        if (activity != null && isAdded) {
            if (body.status == OK) {
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val nowTime = sdf.format(Date())

                SessionManager.timing = nowTime
                SessionManager.isTimerRunning = true

                val direction = CheckInFragmentDirections.checkInToCheckOut()
                findNavController().navigate(direction)


            } else if (body.status == NOT_OK && body.statuscode == TOKEN_EXPIRED) {
                CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                    R.string.tokenExpiredDesc
                ),
                    object : CustomDialog.OnClickListener {
                        override fun okButtonClicked() {
                            (activity as? HomeActivity?)?.logoutUser()
                        }
                    })
            } else {
                CustomDialog(requireContext()).showInformationDialog(body.message)
            }

        }
    }
}