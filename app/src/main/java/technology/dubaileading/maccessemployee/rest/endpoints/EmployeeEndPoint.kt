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

    @POST("employee/EmployeeUpdateProfile")
    suspend fun updateProfile(@Body updateProfile: UpdateProfile?): Response<Profile?>

    @GET("employee/getLeaveDetails")
    suspend fun leaves(): Response<GetLeave>

    @POST("employee/changeEmployeePassword")
    suspend fun changePassword(@Body passwordRequest: PasswordRequest?): Response<ChangePassword?>

    @GET("getLeaveTypesnew")
    suspend fun leaveTypes(): Response<LeaveTypes?>

    @GET("getRequestTypes")
    suspend fun requestTypes(): Response<RequestType?>

    @Multipart
    @POST("applyLeaveApplicationnew")
    suspend fun applyLeaveWithFile(
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Response<ApiResponse2?>

    @Multipart
    @POST("applyLeaveApplicationnew")
    suspend fun applyLeaveWithoutFile(
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?
    ): Response<ApiResponse2?>

    @Multipart
    @POST("updateEmployeeLeaveRequest")
    suspend fun updateLeaveWithFile(
        @Part("id") id: RequestBody?,
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Response<ApiResponse2?>

    @Multipart
    @POST("updateEmployeeLeaveRequest")
    suspend fun updateLeaveWithoutFile(
        @Part("id") id: RequestBody?,
        @Part("leave_type_id") leave_type_id: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?
    ): Response<ApiResponse2?>

    @Multipart
    @POST("makeEmployeeRequestnew")
    suspend fun requestDocumentWithFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part,
    ): Response<ApiResponse?>

    @Multipart
    @POST("makeEmployeeRequestnew")
    suspend fun requestDocumentWithoutFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("email") email: RequestBody?
    ): Response<ApiResponse?>

    @Multipart
    @POST("updateEmployeeDocRequest")
    suspend fun updateEmployeeDocRequestWithFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("id") id: RequestBody?,
        @Part NationalIDFile: MultipartBody.Part
    ): Response<ApiResponse?>

    @Multipart
    @POST("updateEmployeeDocRequest")
    suspend fun updateEmployeeDocRequestWithoutFile(
        @Part("subject") subject: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("request_type") request_type: RequestBody?,
        @Part("required_by") required_by: RequestBody?,
        @Part("id") id: RequestBody?
    ): Response<ApiResponse?>

    @POST("getEmployeeRequestsnew")
    suspend fun getEmployeeRequests(@Body getRequests: GetRequests?): Response<EmployeeRequests?>

    @POST("employee-forgot-password")
    suspend fun forgotPassword(@Body forgotPassword: ForgotPassword?): Response<ApiResponse?>

    @POST("employee-resend-otp")
    suspend fun resendOTP(@Body forgotPassword: ForgotPassword?): Response<ApiResponse?>

    @POST("employee-verify-otp")
    suspend fun verifyOTP(@Body verifyOTP: VerifyOTP?): Response<ApiResponse?>

    @POST("employee-reset-password")
    suspend fun resetPassword(@Body resetPassword: ResetPassword?): Response<ApiResponse?>

    @POST("employee/notificationToken")
    suspend fun notificationToken(@Body tokenRequest: TokenRequest?): Response<ApiResponse?>

    @POST("employeeDeleteDocRequest")
    suspend fun deleteDocRequest(@Body deleteReq: DeleteReq?): Response<ApiResponse?>

    @POST("employeeDeleteLeaveRequest")
    suspend fun deleteLeaveRequest(@Body deleteReq: DeleteReq?): Response<ApiResponse?>

    @GET("employee/unreadnotificationListCount")
    suspend fun notificationCount(): Response<NotificationCount?>
}
