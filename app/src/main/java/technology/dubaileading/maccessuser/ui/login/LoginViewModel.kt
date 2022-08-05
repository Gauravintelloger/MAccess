package technology.dubaileading.maccessuser.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessuser.base.BaseViewModel
import technology.dubaileading.maccessuser.rest.entity.LoginRequest
import technology.dubaileading.maccessuser.rest.entity.LoginResponse
import technology.dubaileading.maccessuser.rest.request.ErrorResponse

class LoginViewModel : BaseViewModel(), LoginRepoCallback{

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
            validUser.value = user
        }
        else {
            invalidUser.value = user
        }
    }

    override fun loginFailure(error: ErrorResponse) {
        loginFailure.value = error
    }

}