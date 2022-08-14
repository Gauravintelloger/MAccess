package technology.dubaileading.maccessemployee.ui.change_password


import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityChangePasswordBinding
import technology.dubaileading.maccessemployee.rest.entity.PasswordRequest
import technology.dubaileading.maccessemployee.utils.Utils


class ChangePasswordActivity: BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.submit?.setOnClickListener {
            if(binding?.oldPass?.text?.trim()!!?.isEmpty()){
                Toast.makeText(this@ChangePasswordActivity,"Enter Old Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(binding?.newPass?.text?.trim()!!?.isEmpty()){
                Toast.makeText(this@ChangePasswordActivity,"Enter New Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(binding?.rePass?.text?.trim()!!?.isEmpty()){
                Toast.makeText(this@ChangePasswordActivity,"Re-Enter New Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding?.rePass?.text?.trim() != binding?.newPass?.text?.trim()){
                Toast.makeText(this@ChangePasswordActivity,"New Password and Re-Enter Password is not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            var oldPass = Utils.md5(binding?.oldPass?.text?.toString()?.trim())
            var newPass = Utils.md5(binding?.newPass?.text?.toString()?.trim())
            var rePass = Utils.md5(binding?.rePass?.text?.toString()?.trim())
            var passwordRequest = PasswordRequest(oldPass,newPass,rePass)

            viewModel.changePassword(this,passwordRequest)

            viewModel.changePasswordSuccess.observe(this){
                Toast.makeText(this@ChangePasswordActivity,it.message,Toast.LENGTH_LONG).show()
            }

            viewModel.error.observe(this){
                Toast.makeText(this@ChangePasswordActivity,it.message,Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun createViewModel(): ChangePasswordViewModel {
        return ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivityChangePasswordBinding {
        return ActivityChangePasswordBinding.inflate(layoutInflater!!)
    }
}