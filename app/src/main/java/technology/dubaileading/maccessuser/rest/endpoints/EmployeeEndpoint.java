package technology.dubaileading.maccessuser.rest.endpoints;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import technology.dubaileading.maccessuser.rest.entity.BreakInRequest;
import technology.dubaileading.maccessuser.rest.entity.BreakInResponse;
import technology.dubaileading.maccessuser.rest.entity.BreakOutRequest;
import technology.dubaileading.maccessuser.rest.entity.BreakOutResponse;
import technology.dubaileading.maccessuser.rest.entity.CheckInRequest;
import technology.dubaileading.maccessuser.rest.entity.CheckInResponse;
import technology.dubaileading.maccessuser.rest.entity.CheckOutRequest;
import technology.dubaileading.maccessuser.rest.entity.CheckOutResponse;
import technology.dubaileading.maccessuser.rest.entity.LoginRequest;
import technology.dubaileading.maccessuser.rest.entity.LoginResponse;

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

}
