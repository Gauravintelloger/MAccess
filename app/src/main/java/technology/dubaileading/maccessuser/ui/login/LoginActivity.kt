package technology.dubaileading.maccessuser.ui.login

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
//            startActivity(Intent(applicationContext,HomeActivity::class.java))

            var username = binding.usename.text.toString()
            var password = Utils.md5(binding.password.text.toString())
            var deviceToken = Utils.getUniqueID(this@LoginActivity)

            if(username.isEmpty()){
                Toast.makeText(this@LoginActivity,"All fields are mandatory to proceed",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(password.isEmpty()){
                Toast.makeText(this@LoginActivity,"All fields are mandatory to proceed",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var loginRequest = LoginRequest(
                device_token = deviceToken,
                password = password,
                username = username
            )

            login(loginRequest)
        }



    }

    override fun createViewModel(): LoginViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    private fun login(request: LoginRequest) {

        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .login(request)

        val request = requestFactory.newHttpRequest<Any>(this@LoginActivity)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<LoginResponse?> {
                override fun onSuccess(user: LoginResponse?) {
                    if (user?.data?.status_id == 1) {
                        AppShared(this@LoginActivity).saveToken(user.token)

                        AppShared(this@LoginActivity).saveUser(user)
                        finish()
                        startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                    }
                }
            }
            ) { Toast.makeText(this@LoginActivity, "error", Toast.LENGTH_LONG).show() }.build()
        request.executeAsync()
    }


}