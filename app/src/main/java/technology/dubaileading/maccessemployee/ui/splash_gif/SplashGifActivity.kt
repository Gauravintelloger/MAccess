package technology.dubaileading.maccessemployee.ui.splash_gif

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import pl.droidsonroids.gif.GifDrawable

import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivitySplashGifBinding
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.ui.splash.SplashActivity
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.Utils


private const val UPDATE_REQUEST_CODE = 123

class SplashGifActivity : BaseActivity<ActivitySplashGifBinding, SplashGifViewModel>() {
    private lateinit var appUpdateManager : AppUpdateManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(this)

        Log.e("android ID", Utils.getUniqueID(applicationContext))
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE)
            }else if (it.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE){
                navtoHome()
            } else{
                navtoHome()
            }
        }.addOnFailureListener {
            navtoHome()
            Log.e("ImmediateUpdateActivity", "Failed to check for update: $it")
        }



        val gifFromAssets = GifDrawable(assets, "splash_gif.gif")
        binding.splashGif.setImageDrawable(gifFromAssets)


    }

    @SuppressLint("SuspiciousIndentation")
    private fun navtoHome() {
        Handler(Looper.getMainLooper()).postDelayed({
        val token = AppShared(this@SplashGifActivity).getToken()
            if (token.equals(null) || token!!.isEmpty()){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, SplashActivity::class.java))
                finish()
            }

       },2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            navtoHome()
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE)
            }
        }
    }



    override fun createViewModel(): SplashGifViewModel {
        return ViewModelProvider(this).get(SplashGifViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySplashGifBinding {
        return ActivitySplashGifBinding.inflate(layoutInflater)
    }
}