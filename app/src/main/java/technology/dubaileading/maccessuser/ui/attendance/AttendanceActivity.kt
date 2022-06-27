package technology.dubaileading.maccessuser.ui.attendance

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivityTimelogBinding
import technology.dubaileading.maccessuser.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessuser.rest.entity.AttendenceReport
import technology.dubaileading.maccessuser.rest.entity.LoginResponse
import technology.dubaileading.maccessuser.rest.entity.ReportRequest
import technology.dubaileading.maccessuser.rest.request.ServerRequestFactory
import technology.dubaileading.maccessuser.rest.request.SuccessCallback
import technology.dubaileading.maccessuser.ui.HomeActivity
import technology.dubaileading.maccessuser.utils.AppShared
import java.util.*

class AttendanceActivity : BaseActivity<ActivityTimelogBinding,AttendanceViewModel>(),
    OnDateSetListener {

    private var IS_FROM_DATE = false
    private var fromDate : String = ""
    private var toDate : String = ""

    override fun createViewModel(): AttendanceViewModel {
        return ViewModelProvider(this).get(AttendanceViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityTimelogBinding {
        return ActivityTimelogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this@AttendanceActivity,
            this@AttendanceActivity, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        binding.fromDate.setOnClickListener{
            IS_FROM_DATE = true
            datePickerDialog.show()
        }
        binding.toDate.setOnClickListener {
            IS_FROM_DATE = false
            datePickerDialog.show()
        }

        binding.search.setOnClickListener {
            if(fromDate.isEmpty()){
                Toast.makeText(this@AttendanceActivity,"From date is not selected", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(toDate.isEmpty()){
                Toast.makeText(this@AttendanceActivity,"To date is not selected", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var reportRequest = ReportRequest(fromDate,toDate)
            getReport(reportRequest)
        }



    }

    private fun getReport(reportRequest: ReportRequest) {
        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .getAttendanceReport(reportRequest)

        val request = requestFactory.newHttpRequest<Any>(this@AttendanceActivity)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<AttendenceReport?> {
                override fun onSuccess(attendenceReport: AttendenceReport?) {
                    if (attendenceReport?.attendanceData?.data != null){

                    }

                }
            }) {
                Toast.makeText(this@AttendanceActivity, "error", Toast.LENGTH_LONG).show()
            }.build()
        request.executeAsync()
    }

    private fun processChecking() {

//        var checkInRequest = CheckInRequest(
//            mode = 1,
//            date = Utils.getCurrentDate(),
//            time = Utils.getCurrentTime(),
//            lat_long = gpsTracker.latitude.toString() + "," + gpsTracker.longitude,
//        )
//
//        val requestFactory = ServerRequestFactory()
//        val call = requestFactory
//            .obtainEndpointProxy(EmployeeEndpoint::class.java)
//            .checkIN(checkInRequest)
//
//        val request = requestFactory.newHttpRequest<Any>(this@AttendanceActivity)
//            .withEndpoint(call)
//            .withProgressDialogue()
//            .withSuccessAndFailureCallback(object : SuccessCallback<CheckInResponse?> {
//                override fun onSuccess(user: CheckInResponse?) {
//
//                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
//                    val nowTime = sdf.format(Date())
//
//                    AppShared(applicationContext).saveTiming(nowTime)
//                    AppShared(applicationContext).savePlace(binding.placeName.text.toString())
//
//                    startActivity(Intent(applicationContext, CheckOutJiginActivity::class.java))
//                    finish()
//
//                }
//            }
//            ) { Toast.makeText(this@AttendanceActivity, "error", Toast.LENGTH_LONG).show() }.build()
//        request.executeAsync()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if (IS_FROM_DATE){
            fromDate = "$dayOfMonth/$month/$year"
            binding?.fromDate?.text = "$dayOfMonth/$month/$year"
        }else{
            toDate = "$dayOfMonth/$month/$year"
            binding?.toDate?.text = "$dayOfMonth/$month/$year"
        }
    }

}