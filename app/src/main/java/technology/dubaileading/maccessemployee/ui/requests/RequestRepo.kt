package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.LeaveTypes
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback

class RequestRepo(var callback: RequestCallback) {

    fun getLeaveTypes(context : Context){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).leaveTypes

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<LeaveTypes?> {
                override fun onSuccess(leaveTypes: LeaveTypes?) {
                    callback.leaveTypesResponse(leaveTypes!!)
                }
            }) {
                callback.leaveTypesFailure(it)
            }.build()
        request.executeAsync()
    }
}