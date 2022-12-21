package technology.dubaileading.maccessemployee.callbacks

interface OtpVerifyCallBack {
    fun createPinCallback(otp: String?)
    fun confirmPinCallback(otp: String?)
    fun onAllDigitNotCompleted()
}

