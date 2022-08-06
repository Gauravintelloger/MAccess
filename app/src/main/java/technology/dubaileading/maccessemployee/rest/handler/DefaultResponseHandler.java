package technology.dubaileading.maccessemployee.rest.handler;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Response;
import technology.dubaileading.maccessemployee.rest.ErrorMessages;
import technology.dubaileading.maccessemployee.rest.request.ErrorResponse;
import technology.dubaileading.maccessemployee.rest.request.FailureCallback;
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback;
import technology.dubaileading.maccessemployee.rest.response.ToastMessage;

public class DefaultResponseHandler<T> {
    private SuccessCallback<T> successCallback;
    private FailureCallback failureCallback;
    private Context context;

    public DefaultResponseHandler(Context context, SuccessCallback<T> sCallback,
                                  FailureCallback failureCallback) {
        this.context = context;
        this.successCallback = sCallback;
        this.failureCallback = failureCallback;
    }

    public boolean handle(Response<T> response) {
        try {

            //handles if the response is null
            if(response == null){
                if(failureCallback != null){
                    failureCallback.onFailure(new ErrorResponse(0,
                            new ToastMessage("","failure response")));
                }
                return true;
            }

            //handles if the response is not successful
            if (!response.isSuccessful()) {

                //check the api is
                if (response.code() == ErrorMessages.RESOURCE_NOT_FOUND) {
                    ErrorMessages.getInstance().showResourceNotFoundDialog(context);
                    return true;
                }
                else {
                    if (failureCallback == null) {
//                        new NoNetworkDialog(context).show();
//                        Toast.makeText(context, "Request failed with " + response.code() + " failure code", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        failureCallback.onFailure(new ErrorResponse(
                                0,new ToastMessage("","")));
                    }
                }
                return true;
            } else {
                T sr = response.body();

                successCallback.onSuccess(sr);

            }
        } catch (Throwable t) {
            if(failureCallback != null)
                failureCallback.onFailure(new ErrorResponse(0,
                        new ToastMessage("Error!",t.getMessage())));
            return true;
        }

        return true;
    }

    public void setSuccessCallback(SuccessCallback<T> successCallback) {
        this.successCallback = successCallback;
    }

    public void setFailureCallback(FailureCallback failureCallback) {
        this.failureCallback = failureCallback;
    }

}
