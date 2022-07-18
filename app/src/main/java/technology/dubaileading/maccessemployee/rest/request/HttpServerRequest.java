package technology.dubaileading.maccessemployee.rest.request;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.widget.Toast;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technology.dubaileading.maccessemployee.rest.LoadingDialog;
import technology.dubaileading.maccessemployee.rest.handler.DefaultResponseHandler;

public class HttpServerRequest<T> implements ServerRequest, CancellableRequest {

    /**
     * Context to work with UI thread
     */
    private Context context;

    /**
     * Retrofit call instance
     */
    private Call<T> call;

    /**
     * Callback when request is successful
     */
    private SuccessCallback successCallback;

    /**
     *
     * Callback when request fail
     */
    private FailureCallback failureCallback;

    /**
     * Default response handler
     */
    private DefaultResponseHandler<T> handler;

    /**
     * Flag to indicate if a progress dialogue will be shown
     */
    private boolean showProgressDialogue;

    /**
     * Optional custom message to be shown when request is execution
     */
    private String dialogueMessage;

    private boolean saveRequestError;

    private boolean pdOn = false;
    public LoadingDialog progressDialog;

    public HttpServerRequest(Context context) {
        this(context, null, null);
    }

    public HttpServerRequest(Context context, SuccessCallback sCallback, FailureCallback failureCallback) {
        this.context = context;
        this.successCallback = sCallback;
        this.failureCallback = failureCallback;
        this.handler = new DefaultResponseHandler<>(context, sCallback, failureCallback);
    }

    @Override
    public void executeSync() {
        try {
            if (this.showProgressDialogue) {
                showProgressDialog(context);
            }

            Response<T> response = this.call.execute();
//            Response<ServerResponse<T>> response = this.call.execute();

            if (!this.call.isCanceled() && this.call.isExecuted()) {
                this.handler.handle(response);
            }

        } catch (Throwable t) {
            this.handler.handle(null);
            Log.e("Error executing request", "Failed to execute request", t);
            if (t instanceof UnknownHostException) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failed to connect to host, Please Check your network", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                handleRequestFailure(t);
            }


        } finally {
            dismissProgressDialog();
        }
    }

    @Override
    public void executeAsync() {
        try {
            if (this.showProgressDialogue) {
                showProgressDialog(context);
            }

            this.call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T > response) {
                    handler.handle(response);
                    dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    handler.handle(null);
                    handleRequestFailure(t);
                    dismissProgressDialog();
                }
            });

        } catch (Exception t) {
            handler.handle(null);
            Log.e("Error executing request", "Failed to execute request", t);
            dismissProgressDialog();
            if (t instanceof UnknownHostException) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failed to connect to host, Please Check your network", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                handleRequestFailure(t);
            }
        }
    }

    private <T> void handleRequestFailure(final Throwable t) {
        if(context instanceof Activity){
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    new NoNetworkDialog(context).show();
                    /*Toast.makeText(context, "We are unable to connect to the server! " +
                            "Please check your internet connectivity. If this error persists " +
                            "please contact our customer service", Toast.LENGTH_LONG).show();*/
                    Toast.makeText(context,"Please check your internet connection",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showProgressDialog(Context context) {
        pdOn = true;
        /*progressDialog = initProgressDialog(this.context.getResources().
                getDrawable(R.mipmap.ic_launcher), "Loading");*/
        progressDialog = initProgressDialog(context);
    }

    private void dismissProgressDialog() {
        if (pdOn && progressDialog != null) {
            if(progressDialog.isShowing()){
                //get the Context object that was used to create the dialog
                Context context = ((ContextWrapper)progressDialog.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if(context instanceof Activity) {
                    if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
                        progressDialog.dismiss();
                } else //if the Context used wasn't an Activity, then dismiss it too
                    progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }

    protected LoadingDialog initProgressDialog(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
        return loadingDialog;
    }

    public void setSuccessCallback(SuccessCallback successCallback) {
        this.successCallback = successCallback;
        this.handler.setSuccessCallback(successCallback);
    }
    public void setFailureCallback(FailureCallback failureCallback) {
        this.failureCallback = failureCallback;
        this.handler.setFailureCallback(failureCallback);
    }

    /*public SuccessCallback getSuccessCallback() {
        return successCallback;
    }*/

    /*public boolean isShowProgressDialogue() {
        return showProgressDialogue;
    }*/

    public void setShowProgressDialogue(boolean showProgressDialogue) {
        this.showProgressDialogue = showProgressDialogue;
    }

    /*public String getDialogueMessage() {
        return dialogueMessage;
    }*/

    /*public void setDialogueMessage(String dialogueMessage) {
        this.dialogueMessage = dialogueMessage;
    }*/

    /*public boolean isSaveRequestError() {
        return saveRequestError;
    }*/

    /*public void setSaveRequestError(boolean saveRequestError) {
        this.saveRequestError = saveRequestError;
    }*/

    public Call<T> getCall() {
        return call;
    }

    public void setCall(Call<T> call) {
        this.call = call;
    }

    @Override
    public void cancel() {
        this.call.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.call.isCanceled();
    }
}
