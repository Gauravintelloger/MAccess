package technology.dubaileading.maccessemployee

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(), ActivityLifecycleCallbacks {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        registerActivityLifecycleCallbacks(this)

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Companion.currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        Companion.currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        Companion.currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        //currentActivity = null;
    }

    override fun onActivityStopped(activity: Activity) {
        //currentActivity = null;
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        //currentActivity = null;
    }

    companion object {
        @get:Synchronized
        lateinit var instance: BaseApplication
            private set
        private var currentActivity: Activity? = null
    }
}