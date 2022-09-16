package technology.dubaileading.maccessemployee.ui.settings

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivitySettingsBinding
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsViewModel>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        val versionName = getAppVersion(this)
        binding?.appVersion?.text = "App Version : $versionName"




       /* val startDate = parseDate("12-10-2013 13:00:00")
        val endDate = parseDate("13-10-2013 06:00:00")

        val differenceInMillis = endDate!!.time - startDate!!.time
        val formattedText = formatElapsedTime(differenceInMillis / 1000)*/






    }


    fun parseDate(strDate: String?): Date? {
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        var date1: Date? = null
        try {
            date1 = dateFormat.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date1
    }

    fun formatElapsedTime(seconds: Long): String? {
        var seconds = seconds
        val hours: Long = TimeUnit.SECONDS.toHours(seconds)
        seconds -= TimeUnit.HOURS.toSeconds(hours)
        val minutes: Long = TimeUnit.SECONDS.toMinutes(seconds)
        seconds -= TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%d:%d:%d", hours, minutes, seconds)
    }

    override fun createViewModel(): SettingsViewModel {
        return ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(layoutInflater!!)
    }



    fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
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