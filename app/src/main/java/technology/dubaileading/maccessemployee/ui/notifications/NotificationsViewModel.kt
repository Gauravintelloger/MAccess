package technology.dubaileading.maccessemployee.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.dubaileading.maccessemployee.rest.entity.Notifications
import technology.dubaileading.maccessemployee.utility.DataState
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val notificationsRepository: NotificationsRepository) :
    ViewModel() {
    private val _notifications = MutableLiveData<DataState<Notifications>>()
    val notifications: LiveData<DataState<Notifications>> = _notifications

    fun notifications() {
        viewModelScope.launch {
            notificationsRepository.notifications()
                .onEach { dataState ->
                    dataState.let {
                        _notifications.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }
}
