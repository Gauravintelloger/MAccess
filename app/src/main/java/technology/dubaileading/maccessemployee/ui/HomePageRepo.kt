package technology.dubaileading.maccessemployee.ui

import android.content.Context
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.NotificationCount
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback

class HomePageRepo(var homePageCallback: HomePageCallback) {
    fun getNotificationsCount(context : Context){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).notificationCount

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<NotificationCount?> {
                override fun onSuccess(notificationCount: NotificationCount?) {
                    homePageCallback.notificationsCountSuccess(notificationCount)
                }
            }) {
                homePageCallback.notificationsCountFailure(it)
            }.build()
        request.executeAsync()
    }
}