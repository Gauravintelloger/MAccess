package technology.dubaileading.maccessemployee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.dubaileading.maccessemployee.rest.entity.NotificationCount
import technology.dubaileading.maccessemployee.ui.home.HomeRepository
import technology.dubaileading.maccessemployee.utility.DataState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _notificationCount = MutableLiveData<DataState<NotificationCount>>()
    val notificationCount: LiveData<DataState<NotificationCount>> = _notificationCount

    fun notificationCount() {
        viewModelScope.launch {
            homeRepository.notificationCount()
                .onEach { dataState ->
                    dataState.let {
                        _notificationCount.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }
}