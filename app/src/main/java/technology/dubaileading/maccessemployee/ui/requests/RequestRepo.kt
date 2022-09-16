package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
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
                callback.requestTypesFailure(it)
            }.build()
        request.executeAsync()
    }

    fun applyLeave(context: Context, applyLeave: ApplyLeave) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).applyLeave(applyLeave)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse2?> {
                override fun onSuccess(apiResponse: ApiResponse2?) {
                    callback.applyLeaveSuccess(apiResponse!!)
                }
            }) {
                callback.applyLeaveFailure(it)
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
        var call : Call<ApiResponse>? = null
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
                callback.documentRequestFailure(it)
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
                callback.getRequestFailure(it)
            }.build()
        request.executeAsync()
    }
}