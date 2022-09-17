package technology.dubaileading.maccessemployee.ui.login

import android.content.Context
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.entity.TokenRequest
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


    fun notificationToken(context : Context, tokenRequest: TokenRequest){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .notificationToken(tokenRequest)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(reponse: ApiResponse?) {
                    callback.notificationTokenSuccess(reponse)
                }
            }) {
                callback.notificationTokenFailure(it)
            }.build()
        request.executeAsync()
    }

}