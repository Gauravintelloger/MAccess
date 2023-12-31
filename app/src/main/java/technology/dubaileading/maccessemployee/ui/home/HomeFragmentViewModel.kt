package technology.dubaileading.maccessemployee.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.CheckOutResponse
import technology.dubaileading.maccessemployee.rest.entity.LikePost
import technology.dubaileading.maccessemployee.rest.entity.Posts
import technology.dubaileading.maccessemployee.utility.DataState
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _posts = MutableLiveData<DataState<Posts>>()
    val posts: LiveData<DataState<Posts>> = _posts

    private val _likePost = MutableLiveData<DataState<ApiResponse>>()
    val likePost: LiveData<DataState<ApiResponse>> = _likePost

    fun posts() {
        viewModelScope.launch {
            homeRepository.posts()
                .onEach { dataState ->
                    dataState.let {
                        _posts.value = it
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun likePost(request: LikePost) {
        viewModelScope.launch {
            homeRepository.likePost(request = request)
                .onEach { dataState ->
                    dataState.let {
                        _likePost.value = (it)
                    }
                }.launchIn(viewModelScope)
        }
    }

}