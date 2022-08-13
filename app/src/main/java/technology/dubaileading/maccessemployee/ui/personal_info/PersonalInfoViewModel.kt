package technology.dubaileading.maccessemployee.ui.personal_info

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse


class PersonalInfoViewModel : BaseViewModel(),PersonalRepoCallback {
    private var personalInfoRepo = PersonalInfoRepo(this)
    var profileData = MutableLiveData<Profile>()
    var profileFailure = MutableLiveData<ErrorResponse>()
    var error = MutableLiveData<Profile>()


    fun getProfile(context : Context){
        personalInfoRepo.getProfile(context)
    }


    override fun profileResponse(profile: Profile?) {
        if (profile?.status == "ok") {
            profileData.value = profile!!
        }
        else {
            error.value = profile!!
        }

    }

    override fun profileFailure(error: ErrorResponse) {
        profileFailure.value = error
    }
}