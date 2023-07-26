package technology.dubaileading.maccessemployee.ui.timecreaditrequest

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.di.ErrorHandler
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.DeleteReq
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest
import technology.dubaileading.maccessemployee.rest.entity.addnewjobresponse.AddNewjobresponseModel
import technology.dubaileading.maccessemployee.rest.entity.applycreditrequest.Applycreditresponsemodel
import technology.dubaileading.maccessemployee.rest.entity.applycreditrequest.Newcreditrequestmodel
import technology.dubaileading.maccessemployee.rest.entity.creditlistdetail.CreditlistdetailModel
import technology.dubaileading.maccessemployee.rest.entity.deletecreditrequest.Deletecreditrequest
import technology.dubaileading.maccessemployee.rest.entity.newjobpostrequestmodel.Newjobpostrequestmodel
import technology.dubaileading.maccessemployee.rest.entity.paysliplistmodel.Paysliplistmodel
import technology.dubaileading.maccessemployee.rest.entity.prerequiistquestionmodel.Prerequistquestionmodel
import technology.dubaileading.maccessemployee.rest.entity.timecreditrequestlist.TimecreaditrequestlistModel
import technology.dubaileading.maccessemployee.utility.DataState
import technology.dubaileading.maccessemployee.utility.NetworkHelper
import javax.inject.Inject



class TimecreditRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
)
{
    suspend fun timecreditlist(): Flow<DataState<TimecreaditrequestlistModel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.listtimecredit()
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


    suspend fun deleteLeaveRequest(id: String): Flow<DataState<Deletecreditrequest>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.deleteTimeCreditRequest(id)
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


    suspend fun timecreditlistdetail(id: String): Flow<DataState<CreditlistdetailModel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.viewTimeCreditRequest(id)
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



    suspend fun applycreditrequest(newcreditrequestmodel: Newcreditrequestmodel): Flow<DataState<Applycreditresponsemodel>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.applycreditrequest(newcreditrequestmodel)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    if (response.code() == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED){
                        emit(DataState.TokenExpired)
                    }else{
                        emit(DataState.Error(response.message()))

                        Log.e("error", response.message())
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