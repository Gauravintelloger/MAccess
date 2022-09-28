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

    var applyLeaveSuccess = MutableLiveData<ApiResponse2>()
    var applyLeaveError = MutableLiveData<ApiResponse2>()
    var applyLeaveFailure = MutableLiveData<ErrorResponse>()

    var documentRequestSuccess = MutableLiveData<ApiResponse>()
    var documentRequestError = MutableLiveData<ApiResponse>()
    var documentRequestFailure = MutableLiveData<ErrorResponse>()

    var getRequestSuccess = MutableLiveData<EmployeeRequests>()
    var getRequestError = MutableLiveData<EmployeeRequests>()
    var getRequestFailure = MutableLiveData<ErrorResponse>()

    var selectedFileLiveData: MutableLiveData<String?>? = MutableLiveData()


    var deleteDocRequestSuccess = MutableLiveData<ApiResponse>()
    var deleteDocRequestError = MutableLiveData<ApiResponse>()
    var deleteDocRequestFailure = MutableLiveData<ErrorResponse>()

    var deleteLeaveRequestSuccess = MutableLiveData<ApiResponse>()
    var deleteLeaveRequestError = MutableLiveData<ApiResponse>()
    var deleteLeaveRequestFailure = MutableLiveData<ErrorResponse>()




    fun getLeaveTypes(context : Context){
        requestRepo.getLeaveTypes(context)
    }

    fun applyLeave(context : Context, applyLeave: ApplyLeave){
        this.context = context
        requestRepo.applyLeave(context,applyLeave, selectedFileLiveData?.value)
    }

    fun updateLeave(context : Context, updateLeave: UpdateLeave){
        this.context = context
        requestRepo.updateLeave(context,updateLeave)
    }

    fun getRequestTypes(context : Context){
        requestRepo.getRequestTypes(context)
    }

    fun documentRequest(context: Context, documentRequest: DocumentRequest){
        this.context = context
        requestRepo.documentRequest(context,documentRequest, selectedFileLiveData?.value)
    }

    fun updateDocumentRequest(context: Context, updateDocumentRequest: UpdateDocumentRequest){
        this.context = context
        requestRepo.updateDocumentRequest(context,updateDocumentRequest, selectedFileLiveData?.value)
    }

    fun getEmployeeRequests(context : Context,getRequests: GetRequests){
        requestRepo.getEmployeeRequests(context,getRequests)
    }


    fun deleteDocRequest(context: Context, deleteReq: DeleteReq){
        requestRepo.deleteDocRequest(context,deleteReq)
    }

    fun deleteLeaveRequest(context: Context, deleteReq: DeleteReq){
        requestRepo.deleteLeaveRequest(context,deleteReq)
    }








    override fun leaveTypesResponse(leaveTypes: LeaveTypes) {
        leaveTypesData.value = leaveTypes!!

    }

    override fun leaveTypesFailure(error: ErrorResponse) {
        leaveTypesFailure.value = error
    }




    override fun applyLeaveSuccess(apiResponse: ApiResponse2) {
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

    override fun updateLeaveSuccess(apiResponse: ApiResponse2) {
        if (apiResponse?.status == "ok") {
            applyLeaveSuccess.value = apiResponse!!
        }
        else {
            applyLeaveError.value = apiResponse!!
        }
    }

    override fun updateLeaveFailure(error: ErrorResponse) {
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

    override fun updateDocumentRequestSuccess(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            documentRequestSuccess.value = apiResponse!!
        }
        else {
            documentRequestError.value = apiResponse!!
        }
    }

    override fun updateDocumentRequestFailure(error: ErrorResponse) {
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

    override fun deleteDocRequestSuccess(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            deleteDocRequestSuccess.value = apiResponse!!
        }
        else {
            deleteDocRequestError.value = apiResponse!!
        }

    }

    override fun deleteDocRequestFailure(error: ErrorResponse) {
        deleteDocRequestFailure.value = error
    }

    override fun deleteLeaveRequestSuccess(apiResponse: ApiResponse) {
        if (apiResponse?.status == "ok") {
            deleteLeaveRequestSuccess.value = apiResponse!!
        }
        else {
            deleteLeaveRequestError.value = apiResponse!!
        }
    }

    override fun deleteLeaveRequestFailure(error: ErrorResponse) {
        deleteLeaveRequestFailure.value = error
    }

}