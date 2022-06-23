package technology.dubaileading.maccessuser.utils

import android.content.Context
import android.content.SharedPreferences

object Utils {

    var sharedName = "mAccess";

    private fun getShared(context: Context) : SharedPreferences{
        return context.getSharedPreferences(sharedName,Context.MODE_PRIVATE);
    }

    fun getToken(context : Context) : String?{
        var shared = getShared(context)
        return shared.getString("token","");
    }


}