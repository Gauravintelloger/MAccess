package technology.dubaileading.maccessemployee.ui.attendance

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityTimelogBinding
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.AttendenceReport
import technology.dubaileading.maccessemployee.rest.entity.DataItem
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utils.AppShared
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : BaseActivity<ActivityTimelogBinding,AttendanceViewModel>(),
    OnDateSetListener {

    private lateinit var attendanceReportAdapter: AttendanceReportAdapter
    private var IS_FROM_DATE = false
    private var fromDate : String = ""
    private var toDate : String = ""
    var cal = Calendar.getInstance()

    override fun createViewModel(): AttendanceViewModel {
        return ViewModelProvider(this).get(AttendanceViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityTimelogBinding {
        return ActivityTimelogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()

        attendanceReportAdapter = AttendanceReportAdapter(this@AttendanceActivity)
        binding?.reportsRv?.itemAnimator = DefaultItemAnimator()
        binding?.reportsRv?.layoutManager = LinearLayoutManager(this@AttendanceActivity)
        binding?.reportsRv?.adapter = attendanceReportAdapter


        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        var date = Date()

        fromDate = sdf.format(date)
        toDate = sdf.format(date)

        binding.fromDate.text = sdf.format(date)
        binding.toDate.text = sdf.format(date)

        var reportRequest = ReportRequest(fromDate,toDate)
        viewModel.getReport(this@AttendanceActivity,reportRequest)

        viewModel.attendanceList.observe(this){
            if (it.attendanceData?.data != null){
                attendanceReportAdapter.addList(it.attendanceData.data as ArrayList<DataItem>)
            }

        }


        val today = Calendar.getInstance()

        val dateSetListener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }


        val datePickerDialog = DatePickerDialog(this@AttendanceActivity,
            this@AttendanceActivity, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        binding.fromDate.setOnClickListener{
            IS_FROM_DATE = true
            DatePickerDialog(this@AttendanceActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.toDate.setOnClickListener {
            IS_FROM_DATE = false
            DatePickerDialog(this@AttendanceActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
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

            viewModel.getReport(this@AttendanceActivity,reportRequest)

        }



    }





    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        if (IS_FROM_DATE){
            fromDate = sdf.format(cal.getTime())
            binding?.fromDate?.text = sdf.format(cal.getTime())
        }else{
            toDate = sdf.format(cal.getTime())
            binding?.toDate?.text = sdf.format(cal.getTime())
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AttendanceActivity, HomeActivity::class.java))
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.statusbar_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
    }

}