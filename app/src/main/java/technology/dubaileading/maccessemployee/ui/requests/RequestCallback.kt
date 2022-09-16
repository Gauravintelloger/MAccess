package technology.dubaileading.maccessemployee.ui.requests

import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse

interface RequestCallback {
    fun leaveTypesResponse(leaveTypes: LeaveTypes)
    fun leaveTypesFailure(error : ErrorResponse)

    fun applyLeaveSuccess(apiResponse: ApiResponse2)
    fun applyLeaveFailure(error : ErrorResponse)

    fun requestTypesSuccess(requestType: RequestType)
    fun requestTypesFailure(error : ErrorResponse)

    fun documentRequestSuccess(apiResponse: ApiResponse)
    fun documentRequestFailure(error : ErrorResponse)

    fun getRequestSuccess(employeeRequests: EmployeeRequests)
    fun getRequestFailure(error : ErrorResponse)
}