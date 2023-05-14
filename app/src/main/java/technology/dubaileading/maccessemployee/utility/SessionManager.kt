package technology.dubaileading.maccessemployee.utility

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import technology.dubaileading.maccessemployee.rest.entity.UserDetails

object SessionManager {
    private lateinit var prefs: SharedPreferences
    private val APP_PREF = "AppPref"
    private val KEY_USER_DETAIL = "UserDetail"
    private val IS_USER_LOGGED_IN="IS_USER_LOGGED_IN"
    private val IS_TIMER_RUNNING="isTimerRunning"
    private val IS_BREAK_OUT="isBreakOut"
    private val BREAK_STARTED="breakStarted"
    private val IS_NOTIFICATION="isNotification"
    private val OFFICE_LOCATION_DETAIL="OFFICE_LOCATION_DETAIL"
    private val PROFILE_IMAGE="PROFILE_IMAGE"

    private val ATTENDANCE_INPUT="AttendanceInput"
    private val TOKEN="TOKEN"
    private val ENCRIPTIONORGID="encryptorgid"
    private val managerid="Managerid"
    private val USER_ID="USER_ID"
    private val DATE="date"
    private val HOURS="timing"

    fun init(cntx: Context) {
        prefs = cntx.getSharedPreferences(
            APP_PREF, Context.MODE_PRIVATE
        )
    }

    var user: UserDetails?
        get() = Gson().fromJson(prefs.getString(KEY_USER_DETAIL, null), UserDetails::class.java)
        set(value) = prefs.edit().putString(KEY_USER_DETAIL, Gson().toJson(value)).apply()

    //an inline function to put variable and save it
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLoggedIn:Boolean?
    get()= prefs.getBoolean(IS_USER_LOGGED_IN,false)
    set(value)= prefs.edit().putBoolean(IS_USER_LOGGED_IN, value == true).apply()

    var attendanceInput:String?
        get()= prefs.getString(ATTENDANCE_INPUT,null)
        set(value)= prefs.edit().putString(ATTENDANCE_INPUT,value).apply()

    var token:String?
        get()= prefs.getString(TOKEN,null)
        set(value)= prefs.edit().putString(TOKEN,value).apply()

    var encryptedorgid:String?
        get()= prefs.getString(ENCRIPTIONORGID,null)
        set(value)= prefs.edit().putString(ENCRIPTIONORGID,value).apply()


    var managertype:Int
        get()= prefs.getInt(managerid,2)
        set(value)= prefs.edit().putInt(managerid,value).apply()


    var userId:String?
        get()= prefs.getString(USER_ID,null)
        set(value)= prefs.edit().putString(USER_ID,value).apply()


    var officeLocations:String?
        get()= prefs.getString(OFFICE_LOCATION_DETAIL,null)
        set(value)= prefs.edit().putString(OFFICE_LOCATION_DETAIL,value).apply()


    var profileImage:String?
        get()= prefs.getString(PROFILE_IMAGE,null)
        set(value)= prefs.edit().putString(PROFILE_IMAGE,value).apply()

    var timing:String?
        get()= prefs.getString(DATE, null)
        set(value)= prefs.edit().putString(DATE,value).apply()

    var hours:String?
//        get() {
//            if (prefs.getString(HOURS,"").isNullOrEmpty()) {
//                return prefs.getString(HOURS,"00:00:00")
//            }else{
//               return prefs.getString(HOURS, "")
//            }
//        }
        get()= prefs.getString(HOURS, null)
        set(value)= prefs.edit().putString(HOURS,value).apply()

    var isTimerRunning:Boolean?
        get()= prefs.getBoolean(IS_TIMER_RUNNING,false)
        set(value)= prefs.edit().putBoolean(IS_TIMER_RUNNING, value == true).apply()

    var isBreakOut:Boolean?
        get()= prefs.getBoolean(IS_BREAK_OUT,false)
        set(value)= prefs.edit().putBoolean(IS_BREAK_OUT, value == true).apply()

    var isBreakStarted:Boolean?
        get()= prefs.getBoolean(BREAK_STARTED,false)
        set(value)= prefs.edit().putBoolean(BREAK_STARTED, value == true).apply()

    var isNotification:Boolean?
        get()= prefs.getBoolean(IS_NOTIFICATION,false)
        set(value)= prefs.edit().putBoolean(IS_NOTIFICATION, value == true).apply()


    fun logout() {
        prefs.edit().clear().apply()
    }

     fun deleteAllUserInfo() {
        prefs.edit().clear().commit()
    }

}