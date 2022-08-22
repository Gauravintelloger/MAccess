package technology.dubaileading.maccessemployee.ui.forgot_password

import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface ForgotPasswordCallback {
    fun forgotPasswordResponse(apiResponse: ApiResponse)
    fun forgotPasswordFailure(error : ErrorResponse)

    fun verifyOTPResponse(apiResponse: ApiResponse)
    fun verifyOTPFailure(error : ErrorResponse)

    fun resendOTPResponse(apiResponse: ApiResponse)
    fun resendOTPFailure(error : ErrorResponse)
}