package technology.dubaileading.maccessemployee.rest;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;

import technology.dubaileading.maccessemployee.R;


public class LoadingDialog extends Dialog {

    private Context context;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_loading);
        setCancelable(false);
    }
}
