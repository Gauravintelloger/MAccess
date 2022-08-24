package technology.dubaileading.maccessemployee.ui.forgot_password

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityVerifyOtpactivityBinding
import technology.dubaileading.maccessemployee.rest.entity.ForgotPassword
import technology.dubaileading.maccessemployee.rest.entity.VerifyOTP
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.ui.personal_info.PersonalInfoActivity

class VerifyOTPActivity : BaseActivity<ActivityVerifyOtpactivityBinding, ForgotPasswordViewModel>(){
    private lateinit var otp1 : String
    private lateinit var otp2 : String
    private lateinit var otp3 : String
    private lateinit var otp4 : String
    private lateinit var otp5 : String
    private lateinit var otp6 : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        var email = intent.getStringExtra("email")
        binding.otpEdit1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    binding.otpEdit2.requestFocus()
                }
            }

        })

        binding.otpEdit2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    binding.otpEdit3.requestFocus()
                }
            }

        })

        binding.otpEdit3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    binding.otpEdit4.requestFocus()
                }
            }

        })

        binding.otpEdit4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    binding.otpEdit5.requestFocus()
                }
            }

        })

        binding.otpEdit5.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    binding.otpEdit6.requestFocus()
                }
            }

        })

        binding.otpEdit6.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    hideKeyboard()
                }
            }

        })




        binding.submit.setOnClickListener {
            otp1 =  binding.otpEdit1.text.toString().trim()
            otp2 =  binding.otpEdit2.text.toString().trim()
            otp3 =  binding.otpEdit3.text.toString().trim()
            otp4 =  binding.otpEdit4.text.toString().trim()
            otp5 =  binding.otpEdit5.text.toString().trim()
            otp6 =  binding.otpEdit6.text.toString().trim()
            if (isOtpValid()){
                var otp = otp1+otp2+otp3+otp4+otp5+otp6
                var verifyOTP = VerifyOTP(email,otp)
                viewModel.verifyOTP(this@VerifyOTPActivity,verifyOTP)

            }

        }

        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }


        viewModel.verifyOTPSuccess.observe(this){
            Toast.makeText(this@VerifyOTPActivity, it.message, Toast.LENGTH_LONG).show()
            var intent = Intent(this@VerifyOTPActivity, ResetPasswordActivity::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
        }
        viewModel.verifyOTPError.observe(this){
            Toast.makeText(this@VerifyOTPActivity, it.message, Toast.LENGTH_LONG).show()
        }



        binding.resend.setOnClickListener {
            if (email!!.isEmpty()){
                Toast.makeText(this@VerifyOTPActivity, "Please Re-Try", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var forgotPassword = ForgotPassword(email)
            viewModel.resendOTP(this@VerifyOTPActivity,forgotPassword)
        }

        viewModel.resendOTPSuccess.observe(this){
            Toast.makeText(this@VerifyOTPActivity, it.message, Toast.LENGTH_LONG).show()
        }
        viewModel.resendOTPError.observe(this){
            Toast.makeText(this@VerifyOTPActivity, it.message, Toast.LENGTH_LONG).show()
        }




    }

    private fun isOtpValid(): Boolean {
        if (binding.otpEdit1.text != null && TextUtils.isEmpty(binding.otpEdit1.text)) {
            binding.otpEdit1.requestFocus()
            return false
        }
        if (binding.otpEdit2.text != null && TextUtils.isEmpty(binding.otpEdit2.text)) {
            binding.otpEdit2.requestFocus()
            return false
        }
        if (binding.otpEdit3.text != null && TextUtils.isEmpty(binding.otpEdit3.text)) {
            binding.otpEdit3.requestFocus()
            return false
        }
        if (binding.otpEdit4.text != null && TextUtils.isEmpty(binding.otpEdit4.text)) {
            binding.otpEdit4.requestFocus()
            return false
        }

        if (binding.otpEdit5.text != null && TextUtils.isEmpty(binding.otpEdit5.text)) {
            binding.otpEdit5.requestFocus()
            return false
        }

        if (binding.otpEdit6.text != null && TextUtils.isEmpty(binding.otpEdit6.text)) {
            binding.otpEdit6.requestFocus()
            return false
        }
        return true
    }


    override fun createViewModel(): ForgotPasswordViewModel {
        return ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivityVerifyOtpactivityBinding {
        return ActivityVerifyOtpactivityBinding.inflate(layoutInflater!!)
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
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