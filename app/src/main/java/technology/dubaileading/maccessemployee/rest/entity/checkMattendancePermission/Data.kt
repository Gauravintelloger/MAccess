package technology.dubaileading.maccessemployee.rest.entity.checkMattendancePermission


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("is_post_view")
    val isPostView: Int, // 0
    @SerializedName("request_module_access")
    val requestmoduleaccess: Int, // 0
    @SerializedName("mobile_attendance_allowed")
    val mobileAttendanceAllowed: Int // 1
)