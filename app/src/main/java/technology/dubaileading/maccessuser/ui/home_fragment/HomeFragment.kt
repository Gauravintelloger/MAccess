package technology.dubaileading.maccessuser.ui.home_fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import technology.dubaileading.maccessuser.R
import technology.dubaileading.maccessuser.base.BaseFragment
import technology.dubaileading.maccessuser.databinding.FragmentHomeBinding
import technology.dubaileading.maccessuser.ui.check_in.CheckInActivity
import technology.dubaileading.maccessuser.ui.check_out.CheckOutJiginActivity
import technology.dubaileading.maccessuser.ui.dialog.ComingSoonDialog
import technology.dubaileading.maccessuser.ui.login.LoginActivity
import technology.dubaileading.maccessuser.utils.AppShared
import technology.dubaileading.maccessuser.utils.TimerHelper
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding,HomeFragmentViewModel>() {

    var t : Timer = Timer()

    lateinit var timerText : TextView

    lateinit var activity: Context

    override fun createViewModel(): HomeFragmentViewModel {
        return ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.rv?.itemAnimator = DefaultItemAnimator()
        binding?.rv?.layoutManager = LinearLayoutManager(activity)
        binding?.rv?.adapter = HomeAdapter()

        timerText = binding?.timer!!

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

        binding?.timeClock?.setOnClickListener{
            startActivity(Intent(activity,CheckInActivity::class.java))
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
                    t.cancel()
                    startActivity(Intent(activity,LoginActivity::class.java))
                    (activity as Activity).finishAffinity()
                }
            }
            alertDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        performTimerLogic()
    }

    private fun performTimerLogic() {

        var timing = AppShared(activity as Context).getTiming()
        if(timing.equals("")){
            binding?.timeLayout?.visibility = View.GONE
            binding?.view?.visibility = View.GONE
            return
        }

        val isBreakOut: Boolean = AppShared(activity as Context).isBreakOut()
        if(isBreakOut){
            //when is break out - no updating in ui will happen
            binding?.timer?.text = AppShared(activity as Context).getHours()

            t.cancel()
        }
        else{
            runTimer()
        }

        binding?.started?.text = "Started at ${AppShared(activity as Context).getTiming()}"
        binding?.timer?.text = AppShared(activity as Context).getHours()
    }

    override fun onDestroy() {
        super.onDestroy()
        t.cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context
    }

    private fun runTimer() {
        t = Timer()

        binding?.timeLayout?.visibility = View.VISIBLE
        binding?.view?.visibility = View.VISIBLE

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

                    timerText?.text = timerTime
                })
            }
        }, 0, 1000)
    }

}