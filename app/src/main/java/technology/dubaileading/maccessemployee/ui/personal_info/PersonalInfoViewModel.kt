package technology.dubaileading.maccessemployee.ui.personal_info

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.rest.entity.UpdateProfile
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse


class PersonalInfoViewModel : BaseViewModel(),PersonalRepoCallback {
    private var personalInfoRepo = PersonalInfoRepo(this)

    var profileData = MutableLiveData<Profile>()
    var profileFailure = MutableLiveData<ErrorResponse>()
    var error = MutableLiveData<Profile>()

    var updateProfileSuccess = MutableLiveData<Profile>()
    var updateProfileFailure = MutableLiveData<ErrorResponse>()
    var updateProfileError = MutableLiveData<Profile>()


    fun getProfile(context : Context){
        personalInfoRepo.getProfile(context)
    }

    fun updateProfile(context : Context, updateProfile: UpdateProfile){
        personalInfoRepo.updateProfile(context,updateProfile)
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

    override fun updateProfileResponse(profile: Profile?) {
        if (profile?.status == "ok") {
            updateProfileSuccess.value = profile!!
        }
        else {
            updateProfileError.value = profile!!
        }

    }

    override fun updateProfileFailure(error: ErrorResponse) {
        updateProfileFailure.value = error
    }
}