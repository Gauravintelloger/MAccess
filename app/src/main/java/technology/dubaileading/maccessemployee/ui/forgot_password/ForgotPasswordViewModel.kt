package technology.dubaileading.maccessemployee.ui.forgot_password

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.ForgotPassword
import technology.dubaileading.maccessemployee.rest.entity.ResetPassword
import technology.dubaileading.maccessemployee.rest.entity.VerifyOTP
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

class ForgotPasswordViewModel: BaseViewModel(),ForgotPasswordCallback{
    var forgotPasswordRepo = ForgotPasswordRepo(this)

    var forgotPasswordSuccess = MutableLiveData<ApiResponse>()
    var forgotPasswordError = MutableLiveData<ApiResponse>()
    var forgotPasswordFailure = MutableLiveData<ErrorResponse>()

    var verifyOTPSuccess = MutableLiveData<ApiResponse>()
    var verifyOTPError = MutableLiveData<ApiResponse>()
    var verifyOTPFailure = MutableLiveData<ErrorResponse>()

    var resendOTPSuccess = MutableLiveData<ApiResponse>()
    var resendOTPError = MutableLiveData<ApiResponse>()
    var resendOTPFailure = MutableLiveData<ErrorResponse>()



    fun forgotPassword(context : Context, forgotPassword: ForgotPassword){
        forgotPasswordRepo.forgotPassword(context,forgotPassword)
    }

    fun resendOTP(context : Context, forgotPassword: ForgotPassword){
        forgotPasswordRepo.resendOTP(context,forgotPassword)
    }

    fun verifyOTP(context : Context,verifyOTP: VerifyOTP){
        forgotPasswordRepo.verifyOTP(context,verifyOTP)
    }

    fun resetPassword(context : Context,resetPassword: ResetPassword){
        forgotPasswordRepo.resetPassword(context,resetPassword)
    }



    override fun forgotPasswordResponse(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            forgotPasswordSuccess.value = apiResponse!!
        }
        else {
            forgotPasswordError.value = apiResponse!!
        }

    }

    override fun forgotPasswordFailure(error: ErrorResponse) {
        forgotPasswordFailure.value = error
    }

    override fun verifyOTPResponse(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            verifyOTPSuccess.value = apiResponse!!
        }
        else {
            verifyOTPError.value = apiResponse!!
        }
    }

    override fun verifyOTPFailure(error: ErrorResponse) {
        verifyOTPFailure.value = error
    }

    override fun resendOTPResponse(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            resendOTPSuccess.value = apiResponse!!
        }
        else {
            resendOTPError.value = apiResponse!!
        }
    }

    override fun resendOTPFailure(error: ErrorResponse) {
        resendOTPFailure.value = error
    }
}