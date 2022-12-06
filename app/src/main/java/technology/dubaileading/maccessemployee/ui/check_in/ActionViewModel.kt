package technology.dubaileading.maccessemployee.ui.check_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.utility.DataState
import javax.inject.Inject

@HiltViewModel
class ActionViewModel @Inject constructor(private val actionRepository: ActionRepository) : BaseViewModel() {
    private val _checkIn = MutableLiveData<DataState<CheckInResponse>>()
    val checkIn: LiveData<DataState<CheckInResponse>> = _checkIn

    private val _breakIn= MutableLiveData<DataState<BreakInResponse>>()
    val breakIn: LiveData<DataState<BreakInResponse>> = _breakIn

    private val _breakOut= MutableLiveData<DataState<BreakOutResponse>>()
    val breakOut: LiveData<DataState<BreakOutResponse>> = _breakOut

    private val _checkOut= MutableLiveData<DataState<CheckOutResponse>>()
    val checkOut: LiveData<DataState<CheckOutResponse>> = _checkOut

    fun checkIn(checkInRequest: CheckInRequest) {
        viewModelScope.launch {
            actionRepository.checkIn(checkInRequest).onEach { dataState ->
                _checkIn.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    fun breakIn(breakInRequest: BreakInRequest) {
        viewModelScope.launch {
            actionRepository.breakIn(breakInRequest).onEach { dataState ->
                _breakIn.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    fun breakOut(breakOutRequest: BreakOutRequest) {
        viewModelScope.launch {
            actionRepository.breakOut(breakOutRequest).onEach { dataState ->
                _breakOut.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    fun checkOut(checkOutRequest: CheckOutRequest) {
        viewModelScope.launch {
            actionRepository.checkOut(checkOutRequest).onEach { dataState ->
                _checkOut.value = dataState
            }.launchIn(viewModelScope)
        }
    }
}