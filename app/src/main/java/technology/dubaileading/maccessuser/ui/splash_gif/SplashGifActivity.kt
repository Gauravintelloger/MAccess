package technology.dubaileading.maccessuser.ui.splash_gif

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import pl.droidsonroids.gif.GifDrawable

import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivitySplashBinding
import technology.dubaileading.maccessuser.databinding.ActivitySplashGifBinding
import technology.dubaileading.maccessuser.ui.HomeActivity
import technology.dubaileading.maccessuser.ui.login.LoginActivity
import technology.dubaileading.maccessuser.utils.AppShared
import technology.dubaileading.maccessuser.utils.Utils

class SplashGifActivity : BaseActivity<ActivitySplashGifBinding, SplashGifViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("android ID", Utils.getUniqueID(applicationContext))

        Handler(Looper.getMainLooper()).postDelayed({
            val token = AppShared(this@SplashGifActivity).getToken()

            if(token?.isEmpty() == false){
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }

        },2000)

        val gifFromAssets = GifDrawable(assets, "splash_gif.gif")
        binding.splashGif.setImageDrawable(gifFromAssets)

    }

    override fun createViewModel(): SplashGifViewModel {
        return ViewModelProvider(this).get(SplashGifViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySplashGifBinding {
        return ActivitySplashGifBinding.inflate(layoutInflater)
    }
}