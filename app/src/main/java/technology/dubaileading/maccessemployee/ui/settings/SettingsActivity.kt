package technology.dubaileading.maccessemployee.ui.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivitySettingsBinding


class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsViewModel>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun createViewModel(): SettingsViewModel {
        return ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(layoutInflater!!)
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