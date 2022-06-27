package technology.dubaileading.maccessuser.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider

import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivitySplashBinding
import technology.dubaileading.maccessuser.ui.HomeActivity
import technology.dubaileading.maccessuser.ui.login.LoginActivity
import technology.dubaileading.maccessuser.utils.AppShared

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        startActivity(Intent(applicationContext, LoginActivity::class.java))

//        Log.e("token",AppShared(applicationContext).getToken()!!)

        Handler(Looper.getMainLooper()).postDelayed({
            val token = AppShared(this@SplashActivity).getToken()

            if(token?.isEmpty() == false)
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            else startActivity(Intent(applicationContext, LoginActivity::class.java))

        },2000)

    }

    override fun createViewModel(): SplashViewModel {
        return ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}