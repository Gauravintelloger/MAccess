package technology.dubaileading.maccessemployee.ui.personal_info

import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface PersonalRepoCallback {
    fun profileResponse(profile: Profile?)
    fun profileFailure(error : ErrorResponse)

    fun updateProfileResponse(profile: Profile?)
    fun updateProfileFailure(error : ErrorResponse)
}