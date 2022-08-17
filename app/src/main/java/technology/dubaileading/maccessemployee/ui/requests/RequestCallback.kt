package technology.dubaileading.maccessemployee.ui.requests

import technology.dubaileading.maccessemployee.rest.entity.LeaveTypes
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface RequestCallback {
    fun leaveTypesResponse(leaveTypes: LeaveTypes)
    fun leaveTypesFailure(error : ErrorResponse)
}