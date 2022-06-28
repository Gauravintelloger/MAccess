package technology.dubaileading.maccessuser.utils

import android.app.Activity
import java.util.*

object TickTokTimer : Timer() {

    fun schedule(activity : Activity){
        scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                activity.runOnUiThread(Runnable {
                    val savedTime = AppShared(activity).getTiming()!!
                    val hours: String = AppShared(activity).getHours()!!

                    if(savedTime == "") {
                        cancel()
                        return@Runnable
                    }

                    var timerTime = TimerHelper().findTime(savedTime,hours);

//                    binding.timer.text = timerTime
                })
            }
        }, 0, 1000)
    }



}