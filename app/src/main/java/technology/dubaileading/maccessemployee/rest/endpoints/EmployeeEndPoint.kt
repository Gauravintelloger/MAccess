package technology.dubaileading.maccessemployee.rest.endpoints

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import technology.dubaileading.maccessemployee.rest.entity.*


interface EmployeeEndpoint {
    @POST("employee/login")
    suspend fun login(@Body loginRequest: LoginRequest?): Response<LoginResponse?>

    @POST("employee/checkIN")
    suspend fun checkIN(@Body checkInRequest: CheckInRequest?): Response<CheckInResponse?>

    @POST("employee/breakIN")
    suspend fun breakIN(@Body breakInRequest: BreakInRequest?): Response<BreakInResponse?>

    @POST("employee/breakOUT")
    suspend fun breakOut(@Body breakOutRequest: BreakOutRequest?): Response<BreakOutResponse?>

    @POST("employee/checkOUT")
    suspend fun checkOut(@Body breakOutRequest: CheckOutRequest?): Response<CheckOutResponse?>

    @POST("employee/EmployeeAttendenceReport")
    suspend fun getAttendanceReport(@Body reportRequest: ReportRequest?): Response<AttendenceReport?>

    @POST("employee/notificationList")
    suspend fun notifications(): Response<Notifications?>

    @GET("employee/listPostsnew")
    suspend fun posts(): Response<Posts?>

    @POST("employee/likePostnew")
    suspend fun likePost(@Body likePost: LikePost?): Response<ApiResponse?>

    @GET("employee/employeeProfileView")
    suspend fun getProfile(): Response<Profile?>

    @get:GET("employee/employeeProfileView")
    val profile: Call<Profile?>?

    @POST("employee/EmployeeUpdateProfile")
    fun updateProfile(@Body updateProfile: UpdateProfile?): Call<Profile?>?

    @GET("employee/getLeaveDetails")
    suspend fun leaves(): Response<GetLeave>

    @POST("employee/changeEmployeePassword")
    fun changePassword(@Body passwordRequest: PasswordRequest?): Call<ChangePassword?>?

    @get:GET("getLeaveTypesnew")
    val leaveTypes: Call<LeaveTypes?>?

    @get:GET("getRequestTypes")
    val requestTypes: Call<RequestType?>?

    @Multipart
    @POST("applyLeaveApplicationnew")
    fun applyLeaveWithFile(
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Call<ApiResponse2?>?

    @Multipart
    @POST("applyLeaveApplicationnew")
    fun applyLeaveWithoutFile(
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?
    ): Call<ApiResponse2?>?

    @Multipart
    @POST("updateEmployeeLeaveRequest")
    fun updateLeaveWithFile(
        @Part("id") id: RequestBody?,
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Call<ApiResponse2?>?

    @Multipart
    @POST("updateEmployeeLeaveRequest")
    fun updateLeaveWithoutFile(
        @Part("id") id: RequestBody?,
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?
    ): Call<ApiResponse2?>?

    @Multipart
    @POST("makeEmployeeRequestnew")
    fun requestDocumentWithFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part,
    ): Call<ApiResponse?>?

    @Multipart
    @POST("makeEmployeeRequestnew")
    fun requestDocumentWithoutFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("email") email: RequestBody?
    ): Call<ApiResponse?>?

    @Multipart
    @POST("updateEmployeeDocRequest")
    fun updateEmployeeDocRequestWithFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("id") id: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Call<ApiResponse?>?

    @Multipart
    @POST("updateEmployeeDocRequest")
    fun updateEmployeeDocRequestWithoutFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("id") id: RequestBody?
    ): Call<ApiResponse?>?

    @POST("getEmployeeRequestsnew")
    fun getEmployeeRequests(@Body getRequests: GetRequests?): Call<EmployeeRequests?>?

    @POST("employee-forgot-password")
    fun forgotPassword(@Body forgotPassword: ForgotPassword?): Call<ApiResponse?>?

    @POST("employee-resend-otp")
    fun resendOTP(@Body forgotPassword: ForgotPassword?): Call<ApiResponse?>?

    @POST("employee-verify-otp")
    fun verifyOTP(@Body verifyOTP: VerifyOTP?): Call<ApiResponse?>?

    @POST("employee-reset-password")
    fun resetPassword(@Body resetPassword: ResetPassword?): Call<ApiResponse?>?

    @POST("employee/notificationToken")
    suspend fun notificationToken(@Body tokenRequest: TokenRequest?): Response<ApiResponse?>

    @POST("employeeDeleteDocRequest")
    fun deleteDocRequest(@Body deleteReq: DeleteReq?): Call<ApiResponse?>?

    @POST("employeeDeleteLeaveRequest")
    fun deleteLeaveRequest(@Body deleteReq: DeleteReq?): Call<ApiResponse?>?

    @GET("employee/unreadnotificationListCount")
    suspend fun notificationCount(): Response<NotificationCount?>
}
