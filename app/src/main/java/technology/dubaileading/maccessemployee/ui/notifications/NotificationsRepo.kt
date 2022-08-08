package technology.dubaileading.maccessemployee.ui.notifications

import android.content.Context
import android.util.Log
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.Notifications
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback


class NotificationsRepo(var callback: NotificationsCallback) {

    fun getNotifications(context : Context){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).notifications

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<Notifications?> {
                override fun onSuccess(notifications: Notifications?) {
                    callback.notificationsResponse(notifications)
                    Log.d("test","success------------------------")
                }
            }) {
                callback.notificationsFailure(it)
            }.build()
        request.executeAsync()
    }
}