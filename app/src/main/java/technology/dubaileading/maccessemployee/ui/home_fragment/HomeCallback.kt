package technology.dubaileading.maccessemployee.ui.home_fragment

import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.Posts
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface HomeCallback {
    fun postsResponse(posts: Posts?)
    fun postsFailure(error : ErrorResponse)
    fun likePostFailure(apiResponse: ApiResponse)
}