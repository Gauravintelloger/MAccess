package technology.dubaileading.maccessemployee.ui.check_out

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import pl.droidsonroids.gif.GifDrawable
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityCheckOutBinding
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.attendance.AttendanceActivity
import technology.dubaileading.maccessemployee.ui.check_in.CheckInActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.GPSTracker
import technology.dubaileading.maccessemployee.utils.TimerHelper
import technology.dubaileading.maccessemployee.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class CheckOutJiginActivity : BaseActivity<ActivityCheckOutBinding,CheckOutJiginViewModel>() {

    var t : Timer = Timer()
    lateinit var gpsTracker : GPSTracker
    var IS_SHIFT_OVER = false;
    var is_mock : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backGroundColor()

        //if no timer is set
        val date = AppShared(applicationContext).getTiming()
        if (date!!.isEmpty()) {
            startActivity(Intent(applicationContext, CheckInActivity::class.java))
            finish()
        }

        binding.timesheetLayout.setOnClickListener{
            startActivity(Intent(applicationContext, AttendanceActivity::class.java))
        }

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        gpsTracker = GPSTracker(this@CheckOutJiginActivity)
        gpsTracker.location

        binding.placeName.text = AppShared(applicationContext).getPlace()

        binding.endShift.setOnClickListener{
            val builder = AlertDialog.Builder(this@CheckOutJiginActivity)
            builder.setTitle("End Shift?")
            builder.setMessage("Do you really want to End the Shift?")
            builder.setPositiveButton(
                "Yes"
            ) { _, _ ->
                if (is_mock){
                    showAlert("Please disable fake location to be able to mark attendance")
                } else if (!isAutoTimeZoneEnabled()){
                    showAlert("Please enable the Automatic time zone to be able to mark attendance")
                } else {
                    checkOut()
                }

            }
            builder.setNegativeButton("No", null)
            builder.show()
        }

        try {
            is_mock = isMockLocationOn(
                gpsTracker.location,
                applicationContext
            ) || isMockLocationOn(
                gpsTracker.location,
                applicationContext
            )
        }catch(e : Exception){
            e.printStackTrace()
        }




        performTimerLogic()



    }

    private fun showAlert(msg : String){
        android.app.AlertDialog.Builder(this@CheckOutJiginActivity)
            .setTitle("Alert")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(
                "Ok"
            ) { dialog, which ->

            }
            .show()
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

    private fun isAutoTimeZoneEnabled() =
        Settings.Global.getInt(applicationContext.contentResolver, Settings.Global.AUTO_TIME) != 0

    private fun statusUpdate() {
        var breakStarted = AppShared(applicationContext).getBreakStarted()
        if(!breakStarted){
            binding.breakStatus.visibility = View.GONE
            binding.checkIn.visibility = View.VISIBLE

            val gifFromAssets = GifDrawable(assets, "check_in_gif.gif")
            binding.checkInGif.setImageDrawable(gifFromAssets)

//            binding.timeTxt.text = "check in at ${AppShared(applicationContext).getTiming()}"
            binding.timeTxt.text = "at ${AppShared(applicationContext).getTiming()}"
        }else{
            binding.breakStatus.visibility = View.VISIBLE
            binding.checkIn.visibility = View.GONE

            if(AppShared(applicationContext).isBreakOut()){
//                binding.breakStatus.visibility = View.VISIBLE

//                binding.imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_break_out))
                binding.imageView2.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_break_out,null))
                binding.statusName.setTextColor(resources.getColor(R.color.color_break_out))
                binding.statusName.text = "Break Out "

                binding.timeTxt.text = "at ${AppShared(applicationContext).getTiming()}"
            }
            else{
//                binding.breakStatus.visibility = View.VISIBLE

//                binding.imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_break_in))
                binding.imageView2.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_break_in,null))
                binding.statusName.setTextColor(resources.getColor(R.color.color_break_in))
                binding.statusName.text = "Break In "

                binding.timeTxt.text = "at ${AppShared(applicationContext).getTiming()}"
            }
        }
    }

    private fun performTimerLogic() {
        val isBreakOut: Boolean = AppShared(applicationContext).isBreakOut()
        if(isBreakOut){
            //when is break out - no updating in ui will happen
            breakOutStatusChange(true)
            breakInStatusChange(false)

            binding.timer.text = AppShared(applicationContext).getHours()
        }
        else{
            breakInStatusChange(true)
            breakOutStatusChange(false)
            runTimer()
        }

        statusUpdate()
    }

    private fun breakInStatusChange(status : Boolean){
        if(status){
            binding.imgBreakIn.setColorFilter(ContextCompat.getColor(applicationContext,
                R.color.image_tint), android.graphics.PorterDuff.Mode.SRC_IN)

            binding.breakInTxt.setTextColor(resources.getColor(R.color.image_tint))

            binding.breakInLayout.setOnClickListener(null)
        }else{
            binding.imgBreakIn.colorFilter = null

            binding.breakInTxt.setTextColor(resources.getColor(R.color.color_break_in))

            binding.breakInLayout.setOnClickListener{
                breakIn()
            }
        }
    }

    private fun breakOutStatusChange(status : Boolean){
        if(status){
            binding.imgBreakOut.setColorFilter(ContextCompat.getColor(applicationContext,
                R.color.image_tint), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.breakOutTxt.setTextColor(resources.getColor(R.color.image_tint))

            binding.breakOutLayout.setOnClickListener(null)

        }else{
            binding.imgBreakOut.colorFilter = null
            binding.breakOutTxt.setTextColor(resources.getColor(R.color.color_break_out))

            binding.breakOutLayout.setOnClickListener{
                breakOut()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        t.cancel()
    }

    private fun runTimer() {
        t = Timer()
        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                runOnUiThread(Runnable {
                    val savedTime = AppShared(this@CheckOutJiginActivity).getTiming()!!
                    val hours: String = AppShared(this@CheckOutJiginActivity).getHours()!!

                    if(savedTime == "") {
                        t.cancel()
                        return@Runnable
                    }

                    var timerTime = TimerHelper().findTime(savedTime,hours);
                    val h1 = timerTime.split(":").toTypedArray()
                    val hour1 = h1[0].toInt()
                    //val minute1 = h1[1].toInt()
                    if (hour1 >= 18){
                        t.cancel()
                        IS_SHIFT_OVER = true;
                        navToHome()

                    }


                    binding.timer.text = timerTime
                })
            }
        }, 0, 1000)
    }

    private fun navToHome() {
        AppShared(applicationContext).saveTiming("")
        AppShared(applicationContext).saveHours("")
        AppShared(applicationContext).setBreakOut(false)
        AppShared(applicationContext).setBreakStarted(false)

        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }

    private fun breakIn() {

        val lat_lng: String = gpsTracker.getLatitude().toString() + "," + gpsTracker.getLongitude()

        var breakIn = BreakInRequest(
            date = Utils.getCurrentDate(),
            time = Utils.getCurrentTime(),
            lat_lng
        )

        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .breakIN(breakIn)

        val request = requestFactory.newHttpRequest<Any>(this@CheckOutJiginActivity)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<BreakInResponse?> {
                override fun onSuccess(user: BreakInResponse?) {
                    if (user?.status == "ok") {
                        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        val tt = sdf.format(Date())

                        AppShared(applicationContext).saveTiming(tt)
                        AppShared(applicationContext).setBreakOut(false)

                        AppShared(applicationContext).setBreakStarted(true)

                        performTimerLogic()
                    }else if (user?.status == "notok" && user?.statuscode == "500"){
                        Toast.makeText(this@CheckOutJiginActivity, "Token expired", Toast.LENGTH_LONG).show()
                        AppShared(this@CheckOutJiginActivity).saveToken("")

                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    } else {
                        android.app.AlertDialog.Builder(this@CheckOutJiginActivity)
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
               // Toast.makeText(this@CheckOutJiginActivity, "error", Toast.LENGTH_LONG).show()
            }.build()
        request.executeAsync()
    }

    private fun breakOut() {

        val lat_lng: String = gpsTracker.getLatitude().toString() + "," + gpsTracker.getLongitude()

        var breakOut = BreakOutRequest(
            date = Utils.getCurrentDate(),
            time = Utils.getCurrentTime(),
            lat_lng
        )

        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .breakOut(breakOut)

        val request = requestFactory.newHttpRequest<Any>(this@CheckOutJiginActivity)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<BreakOutResponse?> {
                override fun onSuccess(user: BreakOutResponse?) {

                    if (user?.status == "ok") {
                        t.cancel()
                        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        val tt = sdf.format(Date())
                        AppShared(applicationContext).saveTiming(tt)

                        AppShared(applicationContext).setBreakOut(true)
                        AppShared(applicationContext).saveHours(binding.timer.text.toString())

                        AppShared(applicationContext).setBreakStarted(true)

                        performTimerLogic()
                    }else if (user?.status == "notok" && user?.statuscode == "500"){
                        Toast.makeText(this@CheckOutJiginActivity, "Token expired", Toast.LENGTH_LONG).show()
                        AppShared(this@CheckOutJiginActivity).saveToken("")

                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    }else{

                        android.app.AlertDialog.Builder(this@CheckOutJiginActivity)
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
                //Toast.makeText(this@CheckOutJiginActivity, "error", Toast.LENGTH_LONG).show()
            }.build()
        request.executeAsync()
    }

    private fun checkOut() {

        val lat_lng: String = gpsTracker.getLatitude().toString() + "," + gpsTracker.getLongitude()

        var checkOutRequest = CheckOutRequest(
            date = Utils.getCurrentDate(),
            time = Utils.getCurrentTime(),
            lat_lng,
            2
        )

        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .checkOut(checkOutRequest)

        val request = requestFactory.newHttpRequest<Any>(this@CheckOutJiginActivity)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<CheckOutResponse?> {
                override fun onSuccess(user: CheckOutResponse?) {
                    if (user?.status == "ok") {
                        t.cancel()
                        AppShared(applicationContext).saveTiming("")
                        AppShared(applicationContext).saveHours("")
                        AppShared(applicationContext).setBreakOut(false)
                        AppShared(applicationContext).setBreakStarted(false)

                        val intent = Intent(this@CheckOutJiginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (user?.status == "notok" && user?.statuscode == "500"){
                        Toast.makeText(this@CheckOutJiginActivity, "Token expired", Toast.LENGTH_LONG).show()
                        AppShared(this@CheckOutJiginActivity).saveToken("")

                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    } else {
                             AlertDialog.Builder(this@CheckOutJiginActivity)
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
                //Toast.makeText(this@CheckOutJiginActivity, "error", Toast.LENGTH_LONG).show()
            }.build()
        request.executeAsync()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
        if (IS_SHIFT_OVER){
            navToHome()
        }
    }

    override fun createViewModel(): CheckOutJiginViewModel {
        return ViewModelProvider(this).get(CheckOutJiginViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityCheckOutBinding {
        return ActivityCheckOutBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.statusbar_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
    }

}