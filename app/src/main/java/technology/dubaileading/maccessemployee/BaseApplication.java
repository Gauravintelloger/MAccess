package technology.dubaileading.maccessemployee;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks{
    private static BaseApplication instance;

    private static Activity currentActivity;

//    BottomNavigationView view;

    //private FirebaseNotificationContract.Presenter presenter;

    public BaseApplication() {
        instance = this;
    }

    public Activity getCurrentActivity(){
        return currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        registerActivityLifecycleCallbacks(this);

//        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                return false;
//            }
//        });
    }


    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        //currentActivity = null;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        //currentActivity = null;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        //currentActivity = null;
    }
}
