package technology.dubaileading.maccessemployee.ui.forgot_password

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityResetPasswordBinding
import technology.dubaileading.maccessemployee.databinding.ActivityVerifyOtpactivityBinding
import technology.dubaileading.maccessemployee.rest.entity.ResetPassword
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utils.Utils

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding, ForgotPasswordViewModel>(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        var email = intent.getStringExtra("email")


        binding.submit.setOnClickListener {
            if(binding?.newPass?.text?.trim()!!?.isEmpty()){
                Toast.makeText(this@ResetPasswordActivity,"Enter New Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(binding?.rePass?.text?.trim()!!?.isEmpty()){
                Toast.makeText(this@ResetPasswordActivity,"Re-Enter New Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding?.rePass?.text?.trim() != binding?.newPass?.text?.trim()){
                Toast.makeText(this@ResetPasswordActivity,"New Password and Re-Enter Password is not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var newPass = Utils.md5(binding?.newPass?.text?.toString()?.trim())
            var rePass = Utils.md5(binding?.rePass?.text?.toString()?.trim())
            var resetPassword = ResetPassword(email,newPass,rePass)

            viewModel.resetPassword(this@ResetPasswordActivity,resetPassword)


        }

        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }


        viewModel.forgotPasswordSuccess.observe(this){
            Toast.makeText(this@ResetPasswordActivity, it.message, Toast.LENGTH_LONG).show()
            startActivity(Intent(this@ResetPasswordActivity, LoginActivity    ::class.java))
            finish()
        }
        viewModel.forgotPasswordError.observe(this){
            Toast.makeText(this@ResetPasswordActivity, it.message, Toast.LENGTH_LONG).show()
        }


    }

    override fun createViewModel(): ForgotPasswordViewModel {
        return ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivityResetPasswordBinding {
        return ActivityResetPasswordBinding.inflate(layoutInflater!!)
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