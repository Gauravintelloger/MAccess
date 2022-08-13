package technology.dubaileading.maccessemployee.ui.attendance

import android.content.Context

import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.AttendenceReport
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback


class AttendanceRepo(var callback: AttendanceCallback) {

    fun getReport(context: Context,reportRequest: ReportRequest) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .getAttendanceReport(reportRequest)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<AttendenceReport?> {
                override fun onSuccess(attendenceReport: AttendenceReport?) {
                    callback.attendanceResponse(attendenceReport)
                }
            }) {
                callback.attendanceFailure(it)
            }.build()
        request.executeAsync()
    }
}