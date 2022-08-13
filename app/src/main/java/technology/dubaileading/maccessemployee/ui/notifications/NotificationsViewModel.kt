package technology.dubaileading.maccessemployee.ui.notifications

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.Notifications
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse


class NotificationsViewModel : BaseViewModel(),NotificationsCallback {
    private var notificationsRepo = NotificationsRepo(this)
    var notificationsList = MutableLiveData<Notifications>()
    var notificationsFailure = MutableLiveData<ErrorResponse>()
    var error = MutableLiveData<Notifications>()



    fun getNotifications(context : Context){
        notificationsRepo.getNotifications(context)
    }
    override fun notificationsResponse(notifications: Notifications?) {
        if (notifications?.status == "ok") {
            notificationsList.value = notifications!!
        }
        else {
            error.value = notifications!!
        }

    }

    override fun notificationsFailure(error: ErrorResponse) {
        notificationsFailure.value = error
    }
}
