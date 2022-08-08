package technology.dubaileading.maccessemployee.ui.notifications

import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.entity.Notifications
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface NotificationsCallback {
    fun notificationsResponse(notifications : Notifications?)
    fun notificationsFailure(error : ErrorResponse)
}