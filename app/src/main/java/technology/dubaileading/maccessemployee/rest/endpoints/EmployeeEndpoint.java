package technology.dubaileading.maccessemployee.rest.endpoints;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse;
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
import technology.dubaileading.maccessemployee.rest.entity.LikePost;
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest;
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse;
import technology.dubaileading.maccessemployee.rest.entity.Notifications;
import technology.dubaileading.maccessemployee.rest.entity.PasswordRequest;
import technology.dubaileading.maccessemployee.rest.entity.Posts;
import technology.dubaileading.maccessemployee.rest.entity.Profile;
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest;

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

    @GET("employee/listPosts")
    Call<Posts> getPosts();

    @POST("employee/likePost")
    Call<ApiResponse> likePost(@Body LikePost likePost);

    @GET("employee/employeeProfileView")
    Call<Profile> getProfile();

    @POST("employee/changeEmployeePassword")
    Call<ChangePassword> changePassword(@Body PasswordRequest passwordRequest);

}
