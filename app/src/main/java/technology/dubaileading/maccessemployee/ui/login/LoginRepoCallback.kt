package technology.dubaileading.maccessuser.ui.login

import technology.dubaileading.maccessuser.rest.entity.LoginResponse
import technology.dubaileading.maccessuser.rest.request.ErrorResponse

interface LoginRepoCallback {

    fun loginResponse(loginResponse : LoginResponse?)
    fun loginFailure(error : ErrorResponse)
}