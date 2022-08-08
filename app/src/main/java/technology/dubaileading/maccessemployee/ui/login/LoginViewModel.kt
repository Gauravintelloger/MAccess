package technology.dubaileading.maccessemployee.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse
import technology.dubaileading.maccessuser.ui.login.LoginRepoCallback


class LoginViewModel : BaseViewModel(), LoginRepoCallback {

    var invalidUser = MutableLiveData<LoginResponse>()
    var validUser = MutableLiveData<LoginResponse>()
    var loginFailure = MutableLiveData<ErrorResponse>()

    private var loginRepo = LoginRepo(this)

    //login api is called
    fun loginUser(context : Context, loginRequest: LoginRequest){
        loginRepo.login(context, loginRequest)
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

}