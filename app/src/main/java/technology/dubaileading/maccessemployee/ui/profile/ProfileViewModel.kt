package technology.dubaileading.maccessemployee.ui.profile


import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.GetLeave
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

class ProfileViewModel : BaseViewModel(),ProfileCallback {
    var profileRepo = ProfileRepo(this)

    var profileData = MutableLiveData<Profile>()
    var profileError = MutableLiveData<Profile>()

    var leaveData = MutableLiveData<GetLeave>()
    var leaveError = MutableLiveData<GetLeave>()

    var failure = MutableLiveData<ErrorResponse>()




    fun getProfile(context : Context){
        profileRepo.getProfile(context)
    }

    fun getLeaves(context : Context){
        profileRepo.getLeaves(context)
    }

    override fun profileResponse(profile: Profile?) {
        if (profile?.status == "ok") {
            profileData.value = profile!!
        } else {
            profileError.value = profile!!
        }
    }

    override fun profileFailure(error: ErrorResponse) {
        failure.value = error
    }

    override fun getLeaveResponse(getLeave: GetLeave?) {
        if (getLeave?.status == "ok") {
            leaveData.value = getLeave!!
        } else {
            leaveError.value = getLeave!!
        }

    }

    override fun getLeaveFailure(error: ErrorResponse) {
        failure.value = error
    }

}