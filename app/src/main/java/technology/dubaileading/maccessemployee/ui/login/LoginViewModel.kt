package technology.dubaileading.maccessemployee.ui.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.entity.TokenRequest
import technology.dubaileading.maccessemployee.utility.DataState
import technology.dubaileading.maccessemployee.utility.Event
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    private val _userDetails = MutableLiveData<DataState<LoginResponse>>()
    val userDetails: LiveData<DataState<LoginResponse>> = _userDetails

    private val _saveToken = MutableLiveData<DataState<ApiResponse>>()
    val saveToken: LiveData<DataState<ApiResponse>> = _saveToken

    fun userLogin(request: LoginRequest) {
        when {
            TextUtils.isEmpty(request.username) -> {
                _statusMessage.value = Event("Enter Valid Username")
            }
            TextUtils.isEmpty(request.password) -> {
                _statusMessage.value = Event("Enter Valid Password")
            }
            TextUtils.isEmpty(request.device_token) -> {
                _statusMessage.value = Event("Invalid Android ID")
            }
            else -> {
                _statusMessage.value = Event("")
                viewModelScope.launch {
                    loginRepository.login(request = request).onEach { dataState ->
                        _userDetails.value = dataState
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }


    fun saveFcmToken(request: TokenRequest) {
        viewModelScope.launch {
            loginRepository.saveFcmToken(request = request).onEach { dataState ->
                _saveToken.value = dataState
            }
                .launchIn(viewModelScope)
        }
    }

}