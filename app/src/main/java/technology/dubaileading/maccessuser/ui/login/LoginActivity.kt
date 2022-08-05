package technology.dubaileading.maccessuser.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivityLoginBinding
import technology.dubaileading.maccessuser.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessuser.rest.entity.LoginRequest
import technology.dubaileading.maccessuser.rest.entity.LoginResponse
import technology.dubaileading.maccessuser.rest.request.ServerRequestFactory
import technology.dubaileading.maccessuser.rest.request.SuccessCallback
import technology.dubaileading.maccessuser.ui.HomeActivity
import technology.dubaileading.maccessuser.utils.AppShared
import technology.dubaileading.maccessuser.utils.Utils


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.submitBt.setOnClickListener{
            var username = binding.usename.text.toString()
            var password = Utils.md5(binding.password.text.toString())
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

        //observing the validUser livedata from ViewModel
        viewModel.validUser.observe(this){
            AppShared(this@LoginActivity).saveToken(it.token)

            AppShared(this@LoginActivity).saveUser(it)

            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
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

}