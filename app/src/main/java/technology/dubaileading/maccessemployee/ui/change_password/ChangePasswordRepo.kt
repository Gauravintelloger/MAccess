package technology.dubaileading.maccessemployee.ui.change_password

import android.content.Context
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.ChangePassword
import technology.dubaileading.maccessemployee.rest.entity.PasswordRequest
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback

class ChangePasswordRepo(var callback: ChangePasswordCallback) {

    fun changePassword(context: Context, passwordRequest: PasswordRequest){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .changePassword(passwordRequest)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ChangePassword?> {
                override fun onSuccess(changePassword: ChangePassword?) {
                    callback.changePasswordResponse(changePassword)
                }
            }) {
                callback.changePasswordFailure(it)
            }.build()
        request.executeAsync()

    }
}