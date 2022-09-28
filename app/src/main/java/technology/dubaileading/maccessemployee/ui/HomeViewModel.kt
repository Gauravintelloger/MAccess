package technology.dubaileading.maccessemployee.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.NotificationCount
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

class HomeViewModel : BaseViewModel(),HomePageCallback {
    var homePageRepo = HomePageRepo(this)

    var notificationCountSuccess = MutableLiveData<NotificationCount>()
    var notificationsCountFailure = MutableLiveData<ErrorResponse>()
    var error = MutableLiveData<NotificationCount>()



    fun getNotificationsCount(context : Context){
        homePageRepo.getNotificationsCount(context)
    }


    override fun notificationsCountSuccess(notificationCount: NotificationCount?) {
        if (notificationCount?.status == "ok") {
            notificationCountSuccess.value = notificationCount!!
        }
        else {
            error.value = notificationCount!!
        }
    }

    override fun notificationsCountFailure(error: ErrorResponse) {
        notificationsCountFailure.value = error
    }


}