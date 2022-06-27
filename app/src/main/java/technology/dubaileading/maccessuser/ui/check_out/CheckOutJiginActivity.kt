package technology.dubaileading.maccessuser.ui.check_out

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessuser.R
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivityCheckOutBinding
import technology.dubaileading.maccessuser.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessuser.rest.entity.*
import technology.dubaileading.maccessuser.rest.request.ServerRequestFactory
import technology.dubaileading.maccessuser.rest.request.SuccessCallback
import technology.dubaileading.maccessuser.ui.HomeActivity
import technology.dubaileading.maccessuser.ui.check_in.CheckInActivity
import technology.dubaileading.maccessuser.utils.AppShared
import technology.dubaileading.maccessuser.utils.GPSTracker
import technology.dubaileading.maccessuser.utils.TimerHelper
import technology.dubaileading.maccessuser.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class CheckOutJiginActivity : BaseActivity<ActivityCheckOutBinding,CheckOutJiginViewModel>() {

    var t : Timer = Timer()
    lateinit var gpsTracker : GPSTracker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //if no timer is set
        val date = AppShared(applicationContext).getTiming()
        if (date!!.isEmpty()) {
            startActivity(Intent(applicationContext, CheckInActivity::class.java))
            finish()
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
                checkOut()
            }
            builder.setNegativeButton("No", null)
            builder.show()
        }

        performTimerLogic()
    }

    private fun statusUpdate() {
        var breakStarted = AppShared(applicationContext).getBreakStarted()
        if(!breakStarted){
            binding.breakStatus.visibility = View.GONE
            binding.timeTxt.text = "check in at ${AppShared(applicationContext).getTiming()}"
        }else{
            if(AppShared(applicationContext).isBreakOut()){
                binding.breakStatus.visibility = View.VISIBLE

//                binding.imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_break_out))
                binding.imageView2.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_break_out,null))
                binding.statusName.setTextColor(resources.getColor(R.color.color_break_out))
                binding.statusName.text = "Break Out "

                binding.timeTxt.text = "at ${AppShared(applicationContext).getTiming()}"
            }
            else{
                binding.breakStatus.visibility = View.VISIBLE

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

                    binding.timer.text = timerTime
                })
            }
        }, 0, 1000)
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

                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    val tt = sdf.format(Date())

                    AppShared(applicationContext).saveTiming(tt)
                    AppShared(applicationContext).setBreakOut(false)

                    AppShared(applicationContext).setBreakStarted(true)

                    performTimerLogic()
                }
            }
            ) { Toast.makeText(this@CheckOutJiginActivity, "error", Toast.LENGTH_LONG).show() }.build()
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

                    t.cancel()

                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    val tt = sdf.format(Date())
                    AppShared(applicationContext).saveTiming(tt)

                    AppShared(applicationContext).setBreakOut(true)
                    AppShared(applicationContext).saveHours(binding.timer.text.toString())

                    AppShared(applicationContext).setBreakStarted(true)

                    performTimerLogic()
                }
            }
            ) { Toast.makeText(this@CheckOutJiginActivity, "error", Toast.LENGTH_LONG).show() }.build()
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

                    t.cancel()

                    AppShared(applicationContext).saveTiming("")
                    AppShared(applicationContext).saveHours("")
                    AppShared(applicationContext).setBreakOut(false)
                    AppShared(applicationContext).setBreakStarted(false)

                    val intent = Intent(this@CheckOutJiginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            ) { Toast.makeText(this@CheckOutJiginActivity, "error", Toast.LENGTH_LONG).show() }.build()
        request.executeAsync()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun createViewModel(): CheckOutJiginViewModel {
        return ViewModelProvider(this).get(CheckOutJiginViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityCheckOutBinding {
        return ActivityCheckOutBinding.inflate(layoutInflater)
    }

}