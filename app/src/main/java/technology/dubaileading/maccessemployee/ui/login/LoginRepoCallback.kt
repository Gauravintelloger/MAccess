package technology.dubaileading.maccessuser.ui.login

import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse


interface LoginRepoCallback {

    fun loginResponse(loginResponse : LoginResponse?)
    fun loginFailure(error : ErrorResponse)
}