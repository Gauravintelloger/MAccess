package technology.dubaileading.maccessemployee.ui.personal_info

import android.content.Context
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.rest.entity.UpdateProfile
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import technology.dubaileading.maccessemployee.utility.showToast

class PersonalInfoRepo(var callback: PersonalRepoCallback) {

    fun getProfile(context : Context){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).profile

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<Profile?> {
                override fun onSuccess(profile: Profile?) {
                    callback.profileResponse(profile)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.profileFailure(it)
            }.build()
        request.executeAsync()
    }

    fun updateProfile(context : Context,updateProfile: UpdateProfile){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).updateProfile(updateProfile)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<Profile?> {
                override fun onSuccess(profile: Profile?) {
                    callback.updateProfileResponse(profile)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.updateProfileFailure(it)
            }.build()
        request.executeAsync()
    }
}