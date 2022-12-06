package technology.dubaileading.maccessemployee.ui.forgot_password


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
import technology.dubaileading.maccessemployee.databinding.ActivityForgotPasswordBinding
import technology.dubaileading.maccessemployee.rest.entity.ForgotPassword


class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel>(){
    private lateinit var email : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        binding.submit.setOnClickListener {
            email = binding.email.text?.toString()?.trim()!!
            if (email!!.isEmpty()) {
                Toast.makeText(this@ForgotPasswordActivity, "Enter email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var forgotPassword = ForgotPassword(email)

            viewModel.forgotPassword(this@ForgotPasswordActivity,forgotPassword)


        }

        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }


        viewModel.forgotPasswordSuccess.observe(this){
            Toast.makeText(this@ForgotPasswordActivity, it.message, Toast.LENGTH_LONG).show()
            var intent = Intent(this@ForgotPasswordActivity, VerifyOTPActivity::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
        }
        viewModel.forgotPasswordError.observe(this){
            Toast.makeText(this@ForgotPasswordActivity, it.message, Toast.LENGTH_LONG).show()
        }

        viewModel.forgotPasswordFailure.observe(this){
            Toast.makeText(this@ForgotPasswordActivity,it.message.toString(), Toast.LENGTH_LONG).show()
        }

    }

    override fun createViewModel(): ForgotPasswordViewModel {
        return ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityForgotPasswordBinding {
        return ActivityForgotPasswordBinding.inflate(layoutInflater!!)
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