package technology.dubaileading.maccessemployee.rest.endpoints

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.entity.addnewjobresponse.AddNewjobresponseModel
import technology.dubaileading.maccessemployee.rest.entity.branchlistmodel.Branchlistmodel
import technology.dubaileading.maccessemployee.rest.entity.countrylistmodel.CountrylistModel
import technology.dubaileading.maccessemployee.rest.entity.currencylistmodel.Currencylistmodel
import technology.dubaileading.maccessemployee.rest.entity.departmentlistmodel.Depalrtmentlistresponse
import technology.dubaileading.maccessemployee.rest.entity.designationlist.Designationlistmodel
import technology.dubaileading.maccessemployee.rest.entity.interviewroundlistmodel.Interviewroundlistmodel
import technology.dubaileading.maccessemployee.rest.entity.jobapplicationrequest.JobApplicationRequest
import technology.dubaileading.maccessemployee.rest.entity.jobapplicationresponse.Jobapplicationresponse
import technology.dubaileading.maccessemployee.rest.entity.jobcompanymodel.Jobcomapanymodel
import technology.dubaileading.maccessemployee.rest.entity.jobpostlistresponse.Jobpostlistresponse
import technology.dubaileading.maccessemployee.rest.entity.jobquestions.Jobquestionmodel
import technology.dubaileading.maccessemployee.rest.entity.managerjobpostrequestlist.ManagerjobpostrequestlistModel
import technology.dubaileading.maccessemployee.rest.entity.newjobpostrequestmodel.Newjobpostrequestmodel
import technology.dubaileading.maccessemployee.rest.entity.prerequiistquestionmodel.Prerequistquestionmodel
import technology.dubaileading.maccessemployee.rest.entity.resumeuploadmodel.ResumeuploadModel
import technology.dubaileading.maccessemployee.ui.applyjobform.UploaddocViewModel
import technology.dubaileading.maccessemployee.ui.manager.Addnewjob.AddNewJobPost


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
///
    @GET("admin/getJobPreRequiQuestions/{id}")
    suspend fun jobpostquestion(
        @Path("id") id: String,
    ): Response<Jobquestionmodel?>


    @Multipart
    @POST("uploadResume")
    suspend fun uploadResume(
        @Part file_name: MultipartBody.Part?
    ): Response<ResumeuploadModel?>

    @GET("admin/getCountries")
    suspend fun countrylist(
    ): Response<CountrylistModel?>


    @GET("admin/getCurrencies")
    suspend fun currencylist(
    ): Response<Currencylistmodel?>

    @POST("employeeJobApply")
    suspend fun applyjob(@Body applyjob: JobApplicationRequest?): Response<Jobapplicationresponse?>


    @Multipart
    @POST("admin/jobPostLists")
    suspend fun jobpostlist(
        @Part("organisation_id") perpage: String?,
        @Part("page") page: Int?,
        @Part("department_id") departmentid: String?,
        @Part("designation_id") designationid: String?,
        @Part("job_category") jobcategory: String?,
        @Part("status") status: String?,

        ): Response<Jobpostlistresponse?>

    @GET("admin/departmentLists/{id}")
    suspend fun departmentlist(
        @Path("id") id: String,
    ): Response<Depalrtmentlistresponse?>


    @GET("admin/designationLists/{id}")
    suspend fun designationlist(
        @Path("id") id: String,
    ): Response<Designationlistmodel?>


    @Multipart
    @POST("manager/jobPostRequestList")
    suspend fun managerjobpostlist(
        @Part("page") page: Int?,
        @Part("items_per_page") itemperpage: Int?,
        @Part("department_id") departmentid: String?,
        @Part("designation_id") designationid: String?,
        @Part("job_category") jobcategory: String?,
        @Part("job_request_status") status: String?,

        ): Response<ManagerjobpostrequestlistModel?>


    @GET("manager/getBranches")
    suspend fun getbranchlist(
    ): Response<Branchlistmodel?>
    @GET("manager/getCompanies")
    suspend fun getcompanylist(
    ): Response<Jobcomapanymodel?>


    @GET("manager/getAllPreRequisitesQuestions")
    suspend fun prereqquestion(
    ): Response<Prerequistquestionmodel?>

    @POST("manager/addJobPostRequest")
    suspend fun addnewjob(@Body addNewJobPost: Newjobpostrequestmodel): Response<AddNewjobresponseModel?>

    @Multipart
    @POST("manager/interviewRoundsList")
    suspend fun interviewroundlist(
        @Part("page") page: Int?,
        @Part("items_per_page") itemperpage: Int?,
        @Part("job_application_id") jobapplicationid: Int?,
        @Part("interview_status") interviewstatus: Int?,
        @Part("interview_date") interviewdate: String?,
        @Part("interview_type") interviewtype: Int?,

        ): Response<Interviewroundlistmodel?>

}
