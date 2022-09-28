package technology.dubaileading.maccessemployee.ui

import technology.dubaileading.maccessemployee.rest.entity.NotificationCount
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface HomePageCallback {
    fun notificationsCountSuccess(notificationCount: NotificationCount?)
    fun notificationsCountFailure(error : ErrorResponse)
}