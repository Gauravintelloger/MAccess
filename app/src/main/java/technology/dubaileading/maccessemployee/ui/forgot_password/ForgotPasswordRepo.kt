package technology.dubaileading.maccessemployee.ui.forgot_password

import android.content.Context
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.ForgotPassword
import technology.dubaileading.maccessemployee.rest.entity.ResetPassword
import technology.dubaileading.maccessemployee.rest.entity.VerifyOTP
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback

class ForgotPasswordRepo(val forgotPasswordCallback: ForgotPasswordCallback) {

    fun forgotPassword(context : Context, forgotPassword: ForgotPassword){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).forgotPassword(forgotPassword)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(entity: ApiResponse?) {
                    forgotPasswordCallback.forgotPasswordResponse(entity!!)
                }
            }) {
                forgotPasswordCallback.forgotPasswordFailure(it)
            }.build()
        request.executeAsync()
    }

    fun resendOTP(context : Context, forgotPassword: ForgotPassword){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).resendOTP(forgotPassword)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(entity: ApiResponse?) {
                    forgotPasswordCallback.resendOTPResponse(entity!!)
                }
            }) {
                forgotPasswordCallback.resendOTPFailure(it)
            }.build()
        request.executeAsync()
    }

    fun verifyOTP(context : Context, verifyOTP: VerifyOTP){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).verifyOTP(verifyOTP)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(entity: ApiResponse?) {
                    forgotPasswordCallback.verifyOTPResponse(entity!!)
                }
            }) {
                forgotPasswordCallback.verifyOTPFailure(it)
            }.build()
        request.executeAsync()
    }

    fun resetPassword(context : Context, resetPassword: ResetPassword){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).resetPassword(resetPassword)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(entity: ApiResponse?) {
                    forgotPasswordCallback.forgotPasswordResponse(entity!!)
                }
            }) {
                forgotPasswordCallback.forgotPasswordFailure(it)
            }.build()
        request.executeAsync()
    }


}