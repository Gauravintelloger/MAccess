package technology.dubaileading.maccessemployee.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivitySettingsBinding


class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsViewModel>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}