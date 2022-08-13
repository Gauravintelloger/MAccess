package technology.dubaileading.maccessemployee.ui.change_password


import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityChangePasswordBinding


class ChangePasswordActivity: BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun createViewModel(): ChangePasswordViewModel {
        return ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivityChangePasswordBinding {
        return ActivityChangePasswordBinding.inflate(layoutInflater!!)
    }
}