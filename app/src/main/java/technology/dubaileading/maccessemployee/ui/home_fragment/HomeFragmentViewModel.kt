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
    var likePost = MutableLiveData<ApiResponse>()
    var postsFailure = MutableLiveData<ErrorResponse>()
    var invalidUser = MutableLiveData<Posts>()



    fun getPosts(context : Context){
        homeRepo.getPosts(context)
    }

    fun likePost(context : Context, likePost: LikePost){
        homeRepo.likePosts(context,likePost)
    }

    override fun postsResponse(posts: Posts?) {
        postsList.value = posts!!
        /*if (posts?.status == "ok") {
            postsList.value = posts!!
        }
        else {
            invalidUser.value = posts!!
        }*/
    }

    override fun postsFailure(error: ErrorResponse) {
    }

    override fun likePostFailure(apiResponse: ApiResponse) {
        likePost.value = apiResponse
    }


}