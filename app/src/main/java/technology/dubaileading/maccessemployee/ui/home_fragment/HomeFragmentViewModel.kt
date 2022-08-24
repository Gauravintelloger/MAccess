package technology.dubaileading.maccessemployee.ui.home_fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.LikePost
import technology.dubaileading.maccessemployee.rest.entity.Posts
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse


class HomeFragmentViewModel : BaseViewModel(),HomeCallback {
    private var homeRepo = HomeRepo(this)
    var postsList = MutableLiveData<Posts>()
    var postsFailure = MutableLiveData<ErrorResponse>()
    var invalidUser = MutableLiveData<Posts>()

    var likePost = MutableLiveData<ApiResponse>()
    var likeError = MutableLiveData<ApiResponse>()
    var likeFailure = MutableLiveData<ErrorResponse>()



    fun getPosts(context : Context){
        homeRepo.getPosts(context)
    }

    fun likePost(context : Context, likePost: LikePost){
        homeRepo.likePosts(context,likePost)
    }

    override fun postsResponse(posts: Posts?) {
        if (posts?.status == "ok") {
            postsList.value = posts!!
        }
        else {
            invalidUser.value = posts!!
        }
    }

    override fun postsFailure(error: ErrorResponse) {
        postsFailure.value = error
    }

    override fun likePostSuccess(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            likePost.value = apiResponse!!
        }
        else {
            likeError.value = apiResponse!!
        }
    }

    override fun likePostFailure(error: ErrorResponse) {
        likeFailure.value = error
    }




}