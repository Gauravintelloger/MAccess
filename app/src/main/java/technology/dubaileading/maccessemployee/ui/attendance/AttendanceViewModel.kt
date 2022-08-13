package technology.dubaileading.maccessemployee.ui.attendance

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.AttendenceReport
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

class AttendanceViewModel : BaseViewModel(),AttendanceCallback {
    val attendanceRepo = AttendanceRepo(this)
    var attendanceList = MutableLiveData<AttendenceReport>()
    var attendanceFailure = MutableLiveData<ErrorResponse>()
    var error = MutableLiveData<AttendenceReport>()

    fun getReport(context : Context,reportRequest: ReportRequest){
        attendanceRepo.getReport(context,reportRequest)
    }

    override fun attendanceResponse(attendenceReport: AttendenceReport?) {
        if (attendenceReport?.status == "ok") {
            attendanceList.value = attendenceReport!!
        }
        else {
            error.value = attendenceReport!!
        }

    }

    override fun attendanceFailure(error: ErrorResponse) {
        attendanceFailure.value = error
    }


}