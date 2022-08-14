package technology.dubaileading.maccessemployee.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse

class AppShared(context : Context) {

    var shared : SharedPreferences = context.getSharedPreferences("app_shared",Context.MODE_PRIVATE)

    /*----------*/
    fun saveUser(user : LoginResponse){
        val edit = shared.edit()
        edit.putString("user",Gson().toJson(user))
        edit.apply()
    }

    fun getUser() : LoginResponse{
        var userS = shared.getString("user","");
        return Gson().fromJson(userS,LoginResponse::class.java)
    }
    /*----------*/

    /*----------*/
    fun saveUserName(user : LoginResponse){
        val edit = shared.edit()
        edit.putString("userName",Gson().toJson(user))
        edit.apply()
    }

    fun getUserName() : LoginResponse{
        var userS = shared.getString("userName","");
        return Gson().fromJson(userS,LoginResponse::class.java)
    }
    /*----------*/


    /*----------*/
    fun saveToken(token : String){
        val edit = shared.edit()
        edit.putString("token",token)
        edit.apply()
    }

    fun getToken() : String?{
        var token = shared.getString("token","");
        return token
    }
    /*----------*/


    fun savePlace(place: String?) {
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putString("place", place)
        edit.apply()
    }

    fun getPlace(): String? {
        return shared.getString("place", "")
    }

    /*----------*/

    fun saveTiming(date: String?) {
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putString("date", date)
        edit.apply()
    }

    fun getTiming(): String? {
        return shared.getString("date", "")
    }

    /*---------*/

    fun isBreakOut(): Boolean {
        return shared.getBoolean("breakOut", false)
    }

    fun setBreakOut(flag: Boolean) {
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putBoolean("breakOut", flag)
        edit.apply()
    }

    /*---------*/

    open fun saveHours(value: String?) {
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putString("timing", value)
        edit.apply()
    }

    fun getHours(): String? {
        val value: String? = shared.getString("timing", "")
        return if (value.isNullOrEmpty()) {
            "00:00:00"
        } else value
    }

    /*--------*/

    fun setBreakStarted(value: Boolean) {
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putBoolean("breakStarted", value)
        edit.apply()
    }

    fun getBreakStarted(): Boolean {
        return shared.getBoolean("breakStarted", false)
    }

    fun clearAll() {
        var editor = shared.edit()
        editor.clear()
        editor.apply()
    }

}