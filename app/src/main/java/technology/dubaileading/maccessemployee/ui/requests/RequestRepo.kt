package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import technology.dubaileading.maccessemployee.utility.showToast
import java.io.File


class RequestRepo(var callback: RequestCallback) {

    fun getLeaveTypes(context: Context) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).leaveTypes

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<LeaveTypes?> {
                override fun onSuccess(leaveTypes: LeaveTypes?) {
                    callback.leaveTypesResponse(leaveTypes!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.leaveTypesFailure(it)
            }.build()
        request.executeAsync()
    }

    fun getRequestTypes(context: Context) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).requestTypes

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<RequestType?> {
                override fun onSuccess(requestType: RequestType?) {
                    callback.requestTypesSuccess(requestType!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.requestTypesFailure(it)
            }.build()
        request.executeAsync()
    }

    fun applyLeave(context: Context, applyLeave: ApplyLeave, filePath: String?) {


        val leave_type_id: RequestBody = applyLeave.leave_type_id.toString()
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val description: RequestBody = applyLeave.description
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val from_date: RequestBody = applyLeave.from_date
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val to_date: RequestBody = applyLeave.to_date
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val requestFactory = ServerRequestFactory()
        var call : Call<ApiResponse2?>? = null
        if (filePath != null) {
            val fileToUpload = File(filePath)

            val attachmentRequestBody =
                fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val attachmentMultiPart = MultipartBody.Part.createFormData(
                "document",
                fileToUpload.name,
                attachmentRequestBody
            )
            call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java).applyLeaveWithFile(leave_type_id,description,from_date,to_date,attachmentMultiPart)
        }else{
            call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java)
                .applyLeaveWithoutFile(leave_type_id,description,from_date,to_date)
        }

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse2?> {
                override fun onSuccess(apiResponse: ApiResponse2?) {
                    callback.applyLeaveSuccess(apiResponse!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.applyLeaveFailure(it)
            }.build()
        request.executeAsync()


    }

    fun updateLeave(context: Context, updateLeave: UpdateLeave, filePath: String?) {


        val id: RequestBody = updateLeave.id.toString()
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val leave_type_id: RequestBody = updateLeave.leave_type_id.toString()
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val description: RequestBody = updateLeave.description
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val from_date: RequestBody = updateLeave.from_date
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val to_date: RequestBody = updateLeave.to_date
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val requestFactory = ServerRequestFactory()
        var call : Call<ApiResponse2?>? = null
        if (filePath != null) {
            val fileToUpload = File(filePath)

            val attachmentRequestBody =
                fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val attachmentMultiPart = MultipartBody.Part.createFormData(
                "document",
                fileToUpload.name,
                attachmentRequestBody
            )
            call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java).updateLeaveWithFile(id,leave_type_id,description,from_date,to_date,attachmentMultiPart)
        }else{
            call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java)
                .updateLeaveWithoutFile(id,leave_type_id,description,from_date,to_date)
        }


        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse2?> {
                override fun onSuccess(apiResponse: ApiResponse2?) {
                    callback.updateLeaveSuccess(apiResponse!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.updateLeaveFailure(it)
            }.build()
        request.executeAsync()
    }

    fun documentRequest(context: Context, documentRequest: DocumentRequest, filePath: String?) {

        val subject: RequestBody = documentRequest.subject
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val description: RequestBody = documentRequest.description
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val request_type: RequestBody = documentRequest.request_type.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val required_by: RequestBody = documentRequest.required_by
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val email: RequestBody = documentRequest.email
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!
        val requestFactory = ServerRequestFactory()
        var call : Call<ApiResponse?>? = null
        if (filePath != null) {
            val fileToUpload = File(filePath)

            val attachmentRequestBody =
                fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val attachmentMultiPart = MultipartBody.Part.createFormData(
                "document",
                fileToUpload.name,
                attachmentRequestBody
            )
             call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java)
                .requestDocumentWithFile(
                    subject, description,
                    request_type, required_by, email, attachmentMultiPart
                )
        }else{
             call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java)
                .requestDocumentWithoutFile(
                    subject, description,
                    request_type, required_by, email
                )
        }



        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(apiResponse: ApiResponse?) {
                    callback.documentRequestSuccess(apiResponse!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.documentRequestFailure(it)
            }.build()
        request.executeAsync()
    }


    fun updateDocumentRequest(context: Context, updateDocumentRequest: UpdateDocumentRequest, filePath: String?) {

        val subject: RequestBody = updateDocumentRequest.subject
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val description: RequestBody = updateDocumentRequest.description
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val request_type: RequestBody = updateDocumentRequest.request_type.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val required_by: RequestBody = updateDocumentRequest.required_by
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

        val id: RequestBody = updateDocumentRequest.id.toString()
            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!
        val requestFactory = ServerRequestFactory()
        var call : Call<ApiResponse?>? = null
        if (filePath != null) {
            val fileToUpload = File(filePath)

            val attachmentRequestBody =
                fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val attachmentMultiPart = MultipartBody.Part.createFormData(
                "document",
                fileToUpload.name,
                attachmentRequestBody
            )
            call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java)
                .updateEmployeeDocRequestWithFile(
                    subject, description,
                    request_type, required_by, id, attachmentMultiPart
                )
        }else{
            call = requestFactory
                .obtainEndpointProxy(EmployeeEndpoint::class.java)
                .updateEmployeeDocRequestWithoutFile(
                    subject, description,
                    request_type, required_by, id
                )
        }



        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(apiResponse: ApiResponse?) {
                    callback.updateDocumentRequestSuccess(apiResponse!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.updateDocumentRequestFailure(it)
            }.build()
        request.executeAsync()
    }

    fun getEmployeeRequests(context: Context, getRequests: GetRequests) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).getEmployeeRequests(getRequests)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<EmployeeRequests?> {
                override fun onSuccess(employeeRequests: EmployeeRequests?) {
                    callback.getRequestSuccess(employeeRequests!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.getRequestFailure(it)
            }.build()
        request.executeAsync()
    }


    fun deleteDocRequest(context: Context, deleteReq: DeleteReq) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).deleteDocRequest(deleteReq)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(apiResponse: ApiResponse?) {
                    callback.deleteDocRequestSuccess(apiResponse!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.deleteDocRequestFailure(it)
            }.build()
        request.executeAsync()
    }

    fun deleteLeaveRequest(context: Context, deleteReq: DeleteReq) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).deleteLeaveRequest(deleteReq)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(apiResponse: ApiResponse?) {
                    callback.deleteLeaveRequestSuccess(apiResponse!!)
                }
            }) {
                context.showToast(
                    context.getString(R.string.apiEngineDown)
                )
                callback.deleteLeaveRequestFailure(it)
            }.build()
        request.executeAsync()
    }


}