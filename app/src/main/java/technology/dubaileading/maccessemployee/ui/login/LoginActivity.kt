package technology.dubaileading.maccessemployee.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityLoginBinding
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest
import technology.dubaileading.maccessemployee.rest.entity.TokenRequest
import technology.dubaileading.maccessemployee.ui.forgot_password.ForgotPasswordActivity
import technology.dubaileading.maccessemployee.ui.splash.SplashActivity
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.Utils


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(){
    private lateinit var token : String
    private lateinit var tokenRequest : TokenRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Splash", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            AppShared(this@LoginActivity).saveFirebaseToken(token)
            tokenRequest = TokenRequest(token,"android")
            // Log and toast
            Log.d("Splash", token)
            //Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })





        binding.submitBt.setOnClickListener{
            var username = binding.usename.text.toString()
            var password = Utils.md5(binding.password.text.toString().trim())
            var deviceToken = Utils.getUniqueID(this@LoginActivity)


            var loginRequest = LoginRequest(
                device_token = deviceToken,
                password = password,
                username = username
            )

            var isValidated = viewModel.validate(loginRequest)
            if(isValidated){
                viewModel.loginUser(this@LoginActivity, loginRequest)
            }

        }

        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }




        //observing the validUser livedata from ViewModel
        viewModel.validUser.observe(this){
            AppShared(this@LoginActivity).saveToken(it.token.toString())

            AppShared(this@LoginActivity).saveUser(it)
            AppShared(this@LoginActivity).saveImage(it.data?.photo)
            AppShared(this@LoginActivity).saveName(it.data?.name)

            viewModel.notificationToken(this@LoginActivity,tokenRequest)


        }

        viewModel.notificationTokenSuccess.observe(this){
            startActivity(Intent(this@LoginActivity, SplashActivity::class.java))
            finish()
        }

        //observing the invalidUser liveData from ViewModel
        viewModel.invalidUser.observe(this){
            AlertDialog.Builder(this@LoginActivity)
                .setTitle("Alert")
                .setMessage(it?.message)
                .setPositiveButton(
                "Ok"
                ) { _, _ ->
                }
            .show()
        }

    }

    override fun createViewModel(): LoginViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.login_bg)
        window.navigationBarColor = ContextCompat.getColor(this,  R.color.login_bg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.login_bg)
    }

}