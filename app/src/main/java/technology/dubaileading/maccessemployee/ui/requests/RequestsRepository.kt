package technology.dubaileading.maccessemployee.ui.requests


import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.di.ErrorHandler
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.utility.DataState
import technology.dubaileading.maccessemployee.utility.NetworkHelper
import java.io.File
import javax.inject.Inject

class RequestsRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
) {
    suspend fun leaveTypes(): Flow<DataState<LeaveTypes>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.leaveTypes()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun applyLeave(request: ApplyLeave, filePath: String?): Flow<DataState<ApiResponse2>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val leaveTypeId: RequestBody = request.leave_type_id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val description: RequestBody = request.description
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val fromDate: RequestBody = request.from_date
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val toDate: RequestBody = request.to_date
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                if (filePath != null) {
                    val fileToUpload = File(filePath)

                    val attachmentRequestBody =
                        fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val attachmentMultiPart = MultipartBody.Part.createFormData(
                        "document",
                        fileToUpload.name,
                        attachmentRequestBody
                    )
                    val response =  retrofit.applyLeaveWithFile(leaveTypeId, description, fromDate, toDate, attachmentMultiPart)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            emit(DataState.Error(response.message()))
                        }
                    }
                }else{
                   val response = retrofit.applyLeaveWithoutFile(leaveTypeId, description, fromDate, toDate)
                    println("apply leave = ${leaveTypeId}--${description}--${fromDate}--${toDate}")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            println("response message = ${response.code()}")
                            emit(DataState.Error(response.message()))
                        }
                    }
                }


            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun requestDocument(request: DocumentRequest, filePath: String?): Flow<DataState<ApiResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val subject: RequestBody = request.subject.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val description: RequestBody = request.description
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val requestType: RequestBody = request.request_type.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val requiredBy: RequestBody = request.required_by
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val email: RequestBody = request.email
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                if (filePath != null) {
                    val fileToUpload = File(filePath)

                    val attachmentRequestBody =
                        fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val attachmentMultiPart = MultipartBody.Part.createFormData(
                        "document",
                        fileToUpload.name,
                        attachmentRequestBody
                    )
                    val response =  retrofit.requestDocumentWithFile(
                        subject, description,
                        requestType, requiredBy, email, attachmentMultiPart
                    )
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            emit(DataState.Error(response.message()))
                        }
                    }
                }else{
                    val response = retrofit.requestDocumentWithoutFile(
                        subject, description,
                        requestType, requiredBy, email
                    )
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            println("response message = ${response.code()}")
                            emit(DataState.Error(response.message()))
                        }
                    }
                }


            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
                emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun requestTypes(): Flow<DataState<RequestType>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.requestTypes()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun employeeRequests(request: GetRequests): Flow<DataState<EmployeeRequests>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.getEmployeeRequests(request)
                if (response.isSuccessful) {
                    val response = response.body()
                    println("employeeRequests = ${Gson().toJson(response)}")
                    emit(DataState.Success(response!!))
                } else {
                    println("employeeRequests error = ${Gson().toJson(response)}")

                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                        emit(DataState.TokenExpired)
                    } else {
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }


    suspend fun updateLeave(request: UpdateLeave, filePath: String?): Flow<DataState<ApiResponse2>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val id: RequestBody = request.id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val leaveTypeId: RequestBody = request.leave_type_id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val description: RequestBody = request.description
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val fromDate: RequestBody = request.from_date
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val toDate: RequestBody = request.to_date
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                if (filePath != null) {
                    val fileToUpload = File(filePath)

                    val attachmentRequestBody =
                        fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val attachmentMultiPart = MultipartBody.Part.createFormData(
                        "document",
                        fileToUpload.name,
                        attachmentRequestBody
                    )
                    val response =  retrofit.updateLeaveWithFile(id, leaveTypeId, description, fromDate, toDate, attachmentMultiPart)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            emit(DataState.Error(response.message()))
                        }
                    }
                }else{
                    val response = retrofit.updateLeaveWithoutFile(id, leaveTypeId, description, fromDate, toDate)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            println("response message = ${response.code()}")
                            emit(DataState.Error(response.message()))
                        }
                    }
                }


            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun deleteLeaveRequest(request: DeleteReq): Flow<DataState<ApiResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.deleteLeaveRequest(request)
                if (response.isSuccessful) {
                    val response = response.body()
                    emit(DataState.Success(response!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                        emit(DataState.TokenExpired)
                    } else {
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun updateDocument(request: UpdateDocumentRequest, filePath: String?): Flow<DataState<ApiResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val subject: RequestBody = request.subject
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val description: RequestBody = request.description
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val requestType: RequestBody = request.request_type.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val requiredBy: RequestBody = request.required_by
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())!!

                val id: RequestBody = request.id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                if (filePath != null) {
                    val fileToUpload = File(filePath)

                    val attachmentRequestBody =
                        fileToUpload.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val attachmentMultiPart = MultipartBody.Part.createFormData(
                        "document",
                        fileToUpload.name,
                        attachmentRequestBody
                    )
                    val response =  retrofit.updateEmployeeDocRequestWithFile(
                        subject, description,
                        requestType, requiredBy, id, attachmentMultiPart
                    )
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            emit(DataState.Error(response.message()))
                        }
                    }
                }else{
                    val response = retrofit.updateEmployeeDocRequestWithoutFile(
                        subject, description,
                        requestType, requiredBy, id
                    )
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        emit(DataState.Success(responseBody!!))
                    } else {
                        if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                            emit(DataState.TokenExpired)
                        } else {
                            println("response message = ${response.code()}")
                            emit(DataState.Error(response.message()))
                        }
                    }
                }


            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun deleteDocumentRequest(request: DeleteReq): Flow<DataState<ApiResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.deleteDocRequest(request)
                if (response.isSuccessful) {
                    val response = response.body()
                    emit(DataState.Success(response!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                        emit(DataState.TokenExpired)
                    } else {
                        emit(DataState.Error(response.message()))
                    }
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }
}