package technology.dubaileading.maccessuser

import java.lang.Exception

interface RetrofitResponse<T> {

    fun onSuccess(t : T)

    fun onError(e : Exception)

}