package technology.dubaileading.maccessemployee.ui.change_password

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.ChangePassword
import technology.dubaileading.maccessemployee.rest.entity.PasswordRequest
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

class ChangePasswordViewModel : BaseViewModel(),ChangePasswordCallback {
    var changePasswordRepo = ChangePasswordRepo(this)
    var changePasswordSuccess = MutableLiveData<ChangePassword>()
    var changePasswordFailure = MutableLiveData<ErrorResponse>()
    var error = MutableLiveData<ChangePassword>()


    fun changePassword(context : Context, passwordRequest: PasswordRequest){
        changePasswordRepo.changePassword(context,passwordRequest)
    }

    override fun changePasswordResponse(changePassword: ChangePassword?) {
        if (changePassword?.status == "ok") {
            changePasswordSuccess.value = changePassword!!
        }
        else {
            error.value = changePassword!!
        }
    }

    override fun changePasswordFailure(error: ErrorResponse) {
        changePasswordFailure.value = error
    }
}