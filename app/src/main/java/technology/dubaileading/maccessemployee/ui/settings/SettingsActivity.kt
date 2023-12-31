package technology.dubaileading.maccessemployee.ui.settings

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.databinding.ActivitySettingsBinding
import technology.dubaileading.maccessemployee.utility.SessionManager
import technology.dubaileading.maccessemployee.utility.setStatusBarTranslucent

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent(true)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        SessionManager.init(this)
        setUpListeners()
        setUpData()

    }

    private fun setUpListeners() {
        viewBinding.backImageView.setOnClickListener {
            finish()
        }

        viewBinding.notificationsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            SessionManager.isNotification = isChecked
        }

        viewBinding.notificationsSwitch.isChecked = SessionManager.isNotification == true

    }

    private fun setUpData() {
        viewBinding.appVersion.text = "App Version : ${getAppVersion(this)}"
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

}