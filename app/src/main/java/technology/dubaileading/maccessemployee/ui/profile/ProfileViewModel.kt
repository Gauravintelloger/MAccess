package technology.dubaileading.maccessemployee.ui.profile


import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

class ProfileViewModel : BaseViewModel(),ProfileCallback {
    var profileRepo = ProfileRepo(this)
    var profileData = MutableLiveData<Profile>()
    var profileFailure = MutableLiveData<ErrorResponse>()
    var error = MutableLiveData<Profile>()


    fun getProfile(context : Context){
        profileRepo.getProfile(context)
    }

    override fun profileResponse(profile: Profile?) {
        if (profile?.status == "ok") {
            profileData.value = profile!!
        } else {
            error.value = profile!!
        }
    }

    override fun profileFailure(error: ErrorResponse) {
        profileFailure.value = error
    }

}