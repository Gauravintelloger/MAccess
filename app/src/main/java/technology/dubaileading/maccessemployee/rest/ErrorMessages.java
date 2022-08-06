package technology.dubaileading.maccessemployee.rest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;


public class ErrorMessages {

    public static final int RESOURCE_NOT_FOUND = 404;

    private static ErrorMessages instance;

    public static ErrorMessages getInstance() {
        if (instance == null) {
            synchronized (ErrorMessages.class) {
                if (instance == null) {
                    instance = new ErrorMessages();
                }
            }
        }
        return instance;
    }

    public void showUnauthorizedUserErrorDialog(Context context) {
        Handler mainHandler = new Handler(context.getMainLooper());

        if(context instanceof Activity){
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Session expired");
                    builder.setMessage("Your session has been expired. Please login again.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            SharedPreference.getObj(context).clearAll();
//                            context.startActivity(new Intent(context, LogInActivity.class));
//                            ((Activity)context).finishAffinity();
                        }
                    });
                    builder.show();
                }
            };
            mainHandler.post(myRunnable);
        }
    }

    public void showResourceNotFoundDialog(Context context) {
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if(context instanceof Activity){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Application is under maintenance");
                    builder.setMessage("Please wait until application is live. If this error persist contact customer care");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    private ProgressDialog initProgressDialog(Drawable drawableId, Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Signing out...");
        progressDialog.setProgressDrawable(drawableId);
        progressDialog.show();
        return progressDialog;
    }

}
