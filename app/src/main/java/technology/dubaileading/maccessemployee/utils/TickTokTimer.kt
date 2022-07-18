package technology.dubaileading.maccessemployee.utils

import android.app.Activity
import java.util.*

object TickTokTimer : Timer() {

    interface CallBack{
        fun timerCallback(string : String)
    }

    fun cancelTimer(){
        cancel()
    }

    fun schedule(activity : Activity, callback : CallBack){

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


                    callback.timerCallback(timerTime)

//                    binding.timer.text = timerTime
                })
            }
        }, 0, 1000)
    }



}