package technology.dubaileading.maccessemployee.ui.home_fragment

import android.content.Context
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
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
}