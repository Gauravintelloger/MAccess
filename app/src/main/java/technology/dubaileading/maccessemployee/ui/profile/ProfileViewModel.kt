package technology.dubaileading.maccessemployee.ui.profile


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.dubaileading.maccessemployee.rest.entity.GetLeave
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.utility.DataState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profile = MutableLiveData<DataState<Profile>>()
    val profile: LiveData<DataState<Profile>> = _profile

    private val _leaves = MutableLiveData<DataState<GetLeave>>()
    val leaves: LiveData<DataState<GetLeave>> = _leaves

    fun profile() {
        viewModelScope.launch {
            profileRepository.getProfile()
                .onEach { dataState ->
                    dataState.let {
                        _profile.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun leaves() {
        viewModelScope.launch {
            profileRepository.getLeaves()
                .onEach { dataState ->
                    dataState.let {
                        _leaves.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

}