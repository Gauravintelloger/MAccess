package technology.dubaileading.maccessemployee.ui.change_password


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityChangePasswordBinding
import technology.dubaileading.maccessemployee.rest.entity.PasswordRequest
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.utils.Utils


class ChangePasswordActivity: BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.submit?.setOnClickListener {
            var oldPassword = binding?.oldPass?.text?.trim()
            var newPassword = binding?.newPass?.text?.trim()
            var rePassword = binding?.rePass?.text?.trim()
            if(oldPassword!!.isEmpty()){
                Toast.makeText(this@ChangePasswordActivity,"Enter Old Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(newPassword!!.isEmpty()){
                Toast.makeText(this@ChangePasswordActivity,"Enter New Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(rePassword!!.isEmpty()){
                Toast.makeText(this@ChangePasswordActivity,"Re-Enter New Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (newPassword != rePassword){
                Toast.makeText(this@ChangePasswordActivity,"New Password and Re-Enter Password is not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            var oldPass = Utils.md5(binding?.oldPass?.text?.toString()?.trim())
            var newPass = Utils.md5(binding?.newPass?.text?.toString()?.trim())
            var rePass = Utils.md5(binding?.rePass?.text?.toString()?.trim())
            var passwordRequest = PasswordRequest(oldPass,newPass,rePass)

            viewModel.changePassword(this,passwordRequest)


        }

        viewModel.changePasswordSuccess.observe(this){
            Toast.makeText(this@ChangePasswordActivity,it.message,Toast.LENGTH_LONG).show()
            startActivity(Intent(this@ChangePasswordActivity, HomeActivity::class.java))
            finish()
        }

        viewModel.error.observe(this){
            Toast.makeText(this@ChangePasswordActivity,it.message,Toast.LENGTH_LONG).show()
        }

    }

    override fun createViewModel(): ChangePasswordViewModel {
        return ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityChangePasswordBinding {
        return ActivityChangePasswordBinding.inflate(layoutInflater!!)
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