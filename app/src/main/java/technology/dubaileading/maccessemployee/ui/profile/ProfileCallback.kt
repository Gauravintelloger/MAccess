package technology.dubaileading.maccessemployee.ui.profile

import technology.dubaileading.maccessemployee.rest.entity.GetLeave
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface ProfileCallback {
    fun profileResponse(profile: Profile?)
    fun profileFailure(error : ErrorResponse)

    fun getLeaveResponse(getLeave: GetLeave?)
    fun getLeaveFailure(error : ErrorResponse)
}