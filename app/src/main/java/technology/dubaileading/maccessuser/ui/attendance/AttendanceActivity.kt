package technology.dubaileading.maccessuser.ui.attendance

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivityTimelogBinding

class AttendanceActivity : BaseActivity<ActivityTimelogBinding,AttendanceViewModel>(),
    OnDateSetListener {

    override fun createViewModel(): AttendanceViewModel {
        return ViewModelProvider(this).get(AttendanceViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityTimelogBinding {
        return ActivityTimelogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val datePickerDialog = DatePickerDialog(applicationContext,
            this@AttendanceActivity, 2022, 2, 1)

        binding.fromDate.setOnClickListener{
            datePickerDialog.show()
        }
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

    }

}