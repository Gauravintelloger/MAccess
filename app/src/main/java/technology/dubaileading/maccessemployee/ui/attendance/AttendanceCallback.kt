package technology.dubaileading.maccessemployee.ui.attendance

import technology.dubaileading.maccessemployee.rest.entity.AttendenceReport
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface AttendanceCallback {
    fun attendanceResponse(attendenceReport: AttendenceReport?)
    fun attendanceFailure(error : ErrorResponse)
}