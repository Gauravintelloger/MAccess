package technology.dubaileading.maccessuser.ui.login

import android.content.Context

import technology.dubaileading.maccessuser.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessuser.rest.entity.LoginRequest
import technology.dubaileading.maccessuser.rest.entity.LoginResponse
import technology.dubaileading.maccessuser.rest.request.ServerRequestFactory
import technology.dubaileading.maccessuser.rest.request.SuccessCallback

class LoginRepo(var callback: LoginRepoCallback) {

    fun login(context : Context,request : LoginRequest){
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