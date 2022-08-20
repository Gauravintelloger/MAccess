package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import java.io.File


class RequestRepo(var callback: RequestCallback) {

    fun getLeaveTypes(context : Context){
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

    fun getRequestTypes(context : Context){
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

    fun applyLeave(context : Context, applyLeave: ApplyLeave){
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).applyLeave(applyLeave)

        val request = requestFactory.newHttpRequest<Any>(context)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<ApiResponse?> {
                override fun onSuccess(apiResponse: ApiResponse?) {
                    callback.applyLeaveSuccess(apiResponse!!)
                }
            }) {
                callback.applyLeaveFailure(it)
            }.build()
        request.executeAsync()
    }

    fun documentRequest(context: Context, documentRequest: DocumentRequest, uri: Uri){


        var fileToUpload :File = File(uri.path)
        val requestFile: RequestBody = fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body :MultipartBody.Part = MultipartBody.Part.createFormData("document",fileToUpload.name,requestFile)

        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java).requestDocument(body,documentRequest.subject,documentRequest.description,
                documentRequest.request_type,documentRequest.required_by,documentRequest.email)

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

    fun getEmployeeRequests(context : Context,getRequests: GetRequests){
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