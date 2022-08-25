package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import androidx.lifecycle.MutableLiveData
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse


class RequestsViewModel() : BaseViewModel(),RequestCallback {
    var requestRepo = RequestRepo(this)
    lateinit var context: Context
    var leaveTypesData = MutableLiveData<LeaveTypes>()
    var leaveTypesFailure = MutableLiveData<ErrorResponse>()

    var requestTypesSuccess = MutableLiveData<RequestType>()
    var requestTypesFailure = MutableLiveData<ErrorResponse>()

    var applyLeaveSuccess = MutableLiveData<ApiResponse>()
    var applyLeaveError = MutableLiveData<ApiResponse>()
    var applyLeaveFailure = MutableLiveData<ErrorResponse>()

    var documentRequestSuccess = MutableLiveData<ApiResponse>()
    var documentRequestError = MutableLiveData<ApiResponse>()
    var documentRequestFailure = MutableLiveData<ErrorResponse>()

    var getRequestSuccess = MutableLiveData<EmployeeRequests>()
    var getRequestError = MutableLiveData<EmployeeRequests>()
    var getRequestFailure = MutableLiveData<ErrorResponse>()

    var selectedFileLiveData: MutableLiveData<String?>? = MutableLiveData()

    fun getLeaveTypes(context : Context){
        requestRepo.getLeaveTypes(context)
    }

    fun applyLeave(context : Context, applyLeave: ApplyLeave){
        this.context = context
        requestRepo.applyLeave(context,applyLeave)
    }

    fun getRequestTypes(context : Context){
        requestRepo.getRequestTypes(context)
    }

    fun documentRequest(context: Context, documentRequest: DocumentRequest){
        this.context = context
        requestRepo.documentRequest(context,documentRequest, selectedFileLiveData?.value)
    }

    fun getEmployeeRequests(context : Context,getRequests: GetRequests){
        requestRepo.getEmployeeRequests(context,getRequests)
    }


    override fun leaveTypesResponse(leaveTypes: LeaveTypes) {
        leaveTypesData.value = leaveTypes!!

    }

    override fun leaveTypesFailure(error: ErrorResponse) {
        leaveTypesFailure.value = error
    }




    override fun applyLeaveSuccess(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            applyLeaveSuccess.value = apiResponse!!
            var getRequests = GetRequests(20, 1)
            requestRepo.getEmployeeRequests(context,getRequests)

        }
        else {
            applyLeaveError.value = apiResponse!!
        }
    }

    override fun applyLeaveFailure(error: ErrorResponse) {
        applyLeaveFailure.value = error
    }




    override fun requestTypesSuccess(requestType: RequestType) {
        requestTypesSuccess.value = requestType!!
    }

    override fun requestTypesFailure(error: ErrorResponse) {
        requestTypesFailure.value = error
    }



    override fun documentRequestSuccess(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            documentRequestSuccess.value = apiResponse!!
            var getRequests = GetRequests(20, 1)
            requestRepo.getEmployeeRequests(context,getRequests)

        }
        else {
            documentRequestError.value = apiResponse!!
        }
    }

    override fun documentRequestFailure(error: ErrorResponse) {
        documentRequestFailure.value = error
    }



    override fun getRequestSuccess(employeeRequests: EmployeeRequests) {
        if (employeeRequests?.status == "ok") {
            getRequestSuccess.value = employeeRequests!!
        }
        else {
            getRequestError.value = employeeRequests!!
        }

    }

    override fun getRequestFailure(error: ErrorResponse) {
        getRequestFailure.value = error

    }

}