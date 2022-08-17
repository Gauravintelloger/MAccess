package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.LeaveTypes
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse


class RequestsViewModel : BaseViewModel(),RequestCallback {
    var requestRepo = RequestRepo(this)
    var leaveTypesData = MutableLiveData<LeaveTypes>()
    var leaveTypesFailure = MutableLiveData<ErrorResponse>()

    fun getLeaveTypes(context : Context){
        requestRepo.getLeaveTypes(context)
    }

    override fun leaveTypesResponse(leaveTypes: LeaveTypes) {
        leaveTypesData.value = leaveTypes!!

    }

    override fun leaveTypesFailure(error: ErrorResponse) {
        leaveTypesFailure.value = error
    }

}