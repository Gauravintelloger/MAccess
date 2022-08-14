package technology.dubaileading.maccessemployee.ui.change_password


import technology.dubaileading.maccessemployee.rest.entity.ChangePassword
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface ChangePasswordCallback {
    fun changePasswordResponse(changePassword: ChangePassword?)
    fun changePasswordFailure(error : ErrorResponse)
}