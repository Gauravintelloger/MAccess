package technology.dubaileading.maccessemployee.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.entity.TokenRequest
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse
import technology.dubaileading.maccessuser.ui.login.LoginRepoCallback


class LoginViewModel : BaseViewModel(), LoginRepoCallback {

    var invalidUser = MutableLiveData<LoginResponse>()
    var validUser = MutableLiveData<LoginResponse>()
    var loginFailure = MutableLiveData<ErrorResponse>()

    var notificationTokenSuccess = MutableLiveData<ApiResponse>()
    var notificationTokenError = MutableLiveData<ApiResponse>()
    var notificationTokenFailure = MutableLiveData<ErrorResponse>()

    private var loginRepo = LoginRepo(this)

    //login api is called
    fun loginUser(context : Context, loginRequest: LoginRequest){
        loginRepo.login(context, loginRequest)
    }


    fun notificationToken(context : Context,tokenRequest: TokenRequest){
        loginRepo.notificationToken(context, tokenRequest)
    }

    //validating the input data
    fun validate(loginRequest : LoginRequest) : Boolean{
        if(loginRequest.username.isEmpty()){
            return false
        }
        if(loginRequest.password.isEmpty()){
            return false
        }

        return true
    }

    //login response from the server
    override fun loginResponse(user : LoginResponse?) {
        if (user?.status == "ok") {
            validUser.value = user!!
        }
        else {
            invalidUser.value = user!!
        }
    }

    override fun loginFailure(error: ErrorResponse) {
        loginFailure.value = error
    }

    override fun notificationTokenSuccess(apiResponse: ApiResponse?) {
        if (apiResponse?.status == "ok") {
            notificationTokenSuccess.value = apiResponse!!
        }
        else {
            notificationTokenError.value = apiResponse!!
        }

    }

    override fun notificationTokenFailure(error: ErrorResponse) {
        notificationTokenFailure.value = error

    }

}