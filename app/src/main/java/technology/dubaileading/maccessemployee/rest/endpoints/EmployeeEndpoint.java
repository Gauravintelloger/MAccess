package technology.dubaileading.maccessemployee.rest.endpoints;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse;
import technology.dubaileading.maccessemployee.rest.entity.ApplyLeave;
import technology.dubaileading.maccessemployee.rest.entity.AttendenceReport;
import technology.dubaileading.maccessemployee.rest.entity.BreakInRequest;
import technology.dubaileading.maccessemployee.rest.entity.BreakInResponse;
import technology.dubaileading.maccessemployee.rest.entity.BreakOutRequest;
import technology.dubaileading.maccessemployee.rest.entity.BreakOutResponse;
import technology.dubaileading.maccessemployee.rest.entity.ChangePassword;
import technology.dubaileading.maccessemployee.rest.entity.CheckInRequest;
import technology.dubaileading.maccessemployee.rest.entity.CheckInResponse;
import technology.dubaileading.maccessemployee.rest.entity.CheckOutRequest;
import technology.dubaileading.maccessemployee.rest.entity.CheckOutResponse;
import technology.dubaileading.maccessemployee.rest.entity.EmployeeRequests;
import technology.dubaileading.maccessemployee.rest.entity.ForgotPassword;
import technology.dubaileading.maccessemployee.rest.entity.GetLeave;
import technology.dubaileading.maccessemployee.rest.entity.GetRequests;
import technology.dubaileading.maccessemployee.rest.entity.LeaveTypes;
import technology.dubaileading.maccessemployee.rest.entity.LikePost;
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest;
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse;
import technology.dubaileading.maccessemployee.rest.entity.Notifications;
import technology.dubaileading.maccessemployee.rest.entity.PasswordRequest;
import technology.dubaileading.maccessemployee.rest.entity.Posts;
import technology.dubaileading.maccessemployee.rest.entity.Profile;
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest;
import technology.dubaileading.maccessemployee.rest.entity.RequestType;
import technology.dubaileading.maccessemployee.rest.entity.ResetPassword;
import technology.dubaileading.maccessemployee.rest.entity.VerifyOTP;

public interface EmployeeEndpoint {

    @POST("employee/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("employee/checkIN")
    Call<CheckInResponse> checkIN(@Body CheckInRequest checkInRequest);

    @POST("employee/breakIN")
    Call<BreakInResponse> breakIN(@Body BreakInRequest breakInRequest);

    @POST("employee/breakOUT")
    Call<BreakOutResponse> breakOut(@Body BreakOutRequest breakOutRequest);

    @POST("employee/checkOUT")
    Call<CheckOutResponse> checkOut(@Body CheckOutRequest breakOutRequest);

    @POST("employee/EmployeeAttendenceReport")
    Call<AttendenceReport> getAttendanceReport(@Body ReportRequest reportRequest);

    @POST("employee/notificationList")
    Call<Notifications> getNotifications();

    @GET("employee/listPostsnew")
    Call<Posts> getPosts();

    @POST("employee/likePostnew")
    Call<ApiResponse> likePost(@Body LikePost likePost);

    @GET("employee/employeeProfileView")
    Call<Profile> getProfile();

    @GET("employee/getLeaveDetails")
    Call<GetLeave> getLeaves();

    @POST("employee/changeEmployeePassword")
    Call<ChangePassword> changePassword(@Body PasswordRequest passwordRequest);

    @GET("getLeaveTypesnew")
    Call<LeaveTypes> getLeaveTypes();

    @POST("applyLeaveApplicationnew")
    Call<ApiResponse> applyLeave(@Body ApplyLeave applyLeave);

    @GET("getRequestTypes")
    Call<RequestType> getRequestTypes();

    @Multipart
    @POST("makeEmployeeRequestnew")
    Call<ApiResponse> requestDocumentWithFile(
            @Part("subject")  RequestBody subject,
            @Part("description")  RequestBody description,
            @Part("request_type")  RequestBody request_type,
            @Part("required_by")  RequestBody required_by,
            @Part("email")  RequestBody email,
            @Part  MultipartBody.Part NationalIDFile
    );

    @Multipart
    @POST("makeEmployeeRequestnew")
    Call<ApiResponse> requestDocumentWithoutFile(
            @Part("subject")  RequestBody subject,
            @Part("description")  RequestBody description,
            @Part("request_type")  RequestBody request_type,
            @Part("required_by")  RequestBody required_by,
            @Part("email")  RequestBody email
    );

    @POST("getEmployeeRequestsnew")
    Call<EmployeeRequests> getEmployeeRequests(@Body GetRequests getRequests);

    @POST("employee-forgot-password")
    Call<ApiResponse> forgotPassword(@Body ForgotPassword forgotPassword);

    @POST("employee-resend-otp")
    Call<ApiResponse> resendOTP(@Body ForgotPassword forgotPassword);

    @POST("employee-verify-otp")
    Call<ApiResponse> verifyOTP(@Body VerifyOTP verifyOTP);

    @POST("employee-reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPassword resetPassword);

}
