package technology.dubaileading.maccessemployee.ui.home_fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentHomeBinding
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.check_in.CheckInActivity
import technology.dubaileading.maccessemployee.ui.check_out.CheckOutJiginActivity
import technology.dubaileading.maccessemployee.ui.dialog.ComingSoonDialog
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.TimerHelper
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding,HomeFragmentViewModel>() {

    var t : Timer = Timer()

    lateinit var timerText : TextView

    lateinit var activity: Context

    override fun createViewModel(): HomeFragmentViewModel {
        return ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }
    private val REQ_CODE_LOCATION = 100

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.rv?.itemAnimator = DefaultItemAnimator()
        binding?.rv?.layoutManager = LinearLayoutManager(activity)
        binding?.rv?.adapter = HomeAdapter()

        timerText = binding?.timer!!

        val token = AppShared(activity).getToken()

        var user = AppShared(activity as Context).getUser()
        binding?.username?.text = user.data.username

        binding?.welcomText?.text = buildSpannedString {
            inSpans(
                ForegroundColorSpan(ContextCompat.getColor(binding?.welcomText?.context!!, R.color.wlcolor))
            ){
                append("Welcome")
            }
            append(" \uD83D\uDD90")
        }

        binding?.timeLayout?.setOnClickListener{
            startActivity(Intent(activity, CheckOutJiginActivity::class.java))
        }

        val locationManager = activity.getSystemService(BaseActivity.LOCATION_SERVICE) as LocationManager

        binding?.timeClock?.setOnClickListener{
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(Intent(activity,CheckInActivity::class.java))
            } else {
                showAlert()
            }

        }

        binding?.service?.setOnClickListener{
            ComingSoonDialog(activity as Context).show()
        }

        binding?.request?.setOnClickListener{
            ComingSoonDialog(activity as Context).show()
        }

        binding?.lock?.setOnClickListener{
            var alertDialog = AlertDialog.Builder(activity as Context)
            alertDialog.setTitle("Logout?")
            alertDialog.setMessage("Do you want to logout from mAccess?")
            alertDialog.setPositiveButton("Yes") { _, _ ->
                run {
                    AppShared(activity as Context).clearAll()

//                    TickTokTimer.cancelTimer()

                    t.cancel()
                    startActivity(Intent(activity,LoginActivity::class.java))
                    (activity as Activity).finishAffinity()
                }
            }
            alertDialog.show()
        }
    }

    private fun showAlert(){
        android.app.AlertDialog.Builder(activity)
            .setTitle("Alert")
            .setMessage("You need give location access to use app")
            .setPositiveButton(
                "Ok"
            ) { dialog, which ->
            dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()

        t.cancel()

        performTimerLogic()
    }

    private fun performTimerLogic() {

        var timing = AppShared(activity as Context).getTiming()
        if(timing.equals("")){
            binding?.timeLayout?.visibility = View.GONE
            binding?.view?.visibility = View.GONE
            return
        }
        else{

        }

        val isBreakOut: Boolean = AppShared(activity as Context).isBreakOut()
        if(isBreakOut){
            binding?.timeLayout?.visibility = View.VISIBLE
            binding?.view?.visibility = View.VISIBLE

            //when is break out - no updating in ui will happen
            binding?.timer?.text = AppShared(activity as Context).getHours()

//            TickTokTimer.cancelTimer()
            t.cancel()
        }
        else{
            binding?.timeLayout?.visibility = View.VISIBLE
            binding?.view?.visibility = View.VISIBLE

            runTimer()
        }

        binding?.started?.text = "Started at ${AppShared(activity as Context).getTiming()}"
        binding?.timer?.text = AppShared(activity as Context).getHours()
    }

    override fun onDestroy() {
        super.onDestroy()

//        TickTokTimer.cancelTimer()
        t.cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context
    }

    private fun runTimer() {

//        TickTokTimer.cancelTimer()

        t = Timer()

        binding?.timeLayout?.visibility = View.VISIBLE
        binding?.view?.visibility = View.VISIBLE

//        TickTokTimer.schedule(activity = activity as Activity,object : TickTokTimer.CallBack{
//            override fun timerCallback(string: String) {
//                (activity as Activity).runOnUiThread(Runnable {
//                    timerText.text = string
//                })
//            }
//
//        })

        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                (activity as Activity)?.runOnUiThread(Runnable {
                    val savedTime = AppShared(activity as Context).getTiming()!!
                    val hours: String = AppShared(activity as Context).getHours()!!

                    if(savedTime == "") {
                        t.cancel()
                        return@Runnable
                    }

                    var timerTime = TimerHelper().findTime(savedTime,hours)

                    val h1 = timerTime.split(":").toTypedArray()
                    val hour1 = h1[0].toInt()
                    //val minute1 = h1[1].toInt()


                    if (hour1 >= 18){
                        t.cancel()
                        AppShared(activity).saveTiming("")
                        AppShared(activity).saveHours("")
                        AppShared(activity).setBreakOut(false)
                        AppShared(activity).setBreakStarted(false)
                        binding?.timeLayout?.visibility = View.GONE
                        binding?.view?.visibility = View.GONE
                    }

                    timerText?.text = timerTime
                })
            }
        }, 0, 1000)
    }

}