package technology.dubaileading.maccessemployee.ui.login

import android.content.Context
import technology.dubaileading.maccessemployee.RetrofitResponse
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import technology.dubaileading.maccessuser.ui.login.LoginRepoCallback

class LoginRepo(var callback: LoginRepoCallback) {

    fun login(context : Context, request : LoginRequest){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .login(request)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<LoginResponse?> {
                override fun onSuccess(user: LoginResponse?) {
                    callback.loginResponse(user)
                }
            }) {
                callback.loginFailure(it)
            }.build()
        request.executeAsync()
    }

}