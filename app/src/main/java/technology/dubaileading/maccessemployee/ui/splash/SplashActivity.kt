package technology.dubaileading.maccessemployee.ui.splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import coil.load

import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivitySplashBinding
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utils.AppShared



class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var user = AppShared(this@SplashActivity).getUser()
        binding.image.load(user.data?.organisationLogo){
            crossfade(true)
            crossfade(800)
        }
        val token = AppShared(this@SplashActivity).getToken()
        Handler(Looper.getMainLooper()).postDelayed({
            if (token.equals(null) || token!!.isEmpty()){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }

        },2000)

    }

    override fun createViewModel(): SplashViewModel {
        return ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}