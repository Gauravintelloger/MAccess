package technology.dubaileading.maccessemployee.ui.check_in


import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import technology.dubaileading.maccessemployee.di.ErrorHandler
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.utility.DataState
import technology.dubaileading.maccessemployee.utility.NetworkHelper
import javax.inject.Inject

class ActionRepository @Inject constructor(
    val retrofit: EmployeeEndpoint, private val networkHelper: NetworkHelper
) {
    suspend fun checkIn(checkInRequest: CheckInRequest): Flow<DataState<CheckInResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.checkIN(checkInRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    Log.d("RetrofitOnSuccessError", response.message()+response.body())
                    emit(DataState.Error(response.message()))
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun breakIn(breakInRequest: BreakInRequest): Flow<DataState<BreakInResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.breakIN(breakInRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    Log.d("RetrofitOnSuccessError", response.message()+response.body())
                    emit(DataState.Error(response.message()))
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun breakOut(breakOutRequest: BreakOutRequest): Flow<DataState<BreakOutResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.breakOut(breakOutRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    Log.d("RetrofitOnSuccessError", response.message()+response.body())
                    emit(DataState.Error(response.message()))
                }
            } else {
                emit(DataState.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            Log.d("Retrofit error", e.toString())
            emit(DataState.Error(ErrorHandler.onError(e)))
        }
    }

    suspend fun checkOut(checkOutRequest: CheckOutRequest): Flow<DataState<CheckOutResponse>> = flow {
        emit(DataState.Loading)
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = retrofit.checkOut(checkOutRequest)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    emit(DataState.Success(userResponse!!))
                } else {
                    Log.d("RetrofitOnSuccessError", response.message()+response.body())
                    emit(DataState.Error(response.message()))
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