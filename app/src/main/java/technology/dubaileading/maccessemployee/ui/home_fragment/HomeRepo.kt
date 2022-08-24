package technology.dubaileading.maccessemployee.ui.home_fragment

import android.content.Context
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.LikePost
import technology.dubaileading.maccessemployee.rest.entity.Posts
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback

class HomeRepo (var callback: HomeCallback) {

    fun getPosts(context : Context){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).posts

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<Posts?> {
                override fun onSuccess(posts: Posts?) {
                    callback.postsResponse(posts)
                }
            }) {
                callback.postsFailure(it)
            }.build()
        request.executeAsync()
    }

    fun likePosts(context : Context, likePost: LikePost){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).likePost(likePost)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(apiResponse: ApiResponse?) {
                    callback.likePostSuccess(apiResponse!!)
                }
            }) {
                callback.likePostFailure(it)
            }.build()
        request.executeAsync()
    }
}