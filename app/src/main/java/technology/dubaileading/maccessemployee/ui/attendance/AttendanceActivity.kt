package technology.dubaileading.maccessemployee.ui.attendance

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
import com.google.android.material.datepicker.MaterialDatePicker
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityTimelogBinding
import technology.dubaileading.maccessemployee.rest.entity.DataItem
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest
import technology.dubaileading.maccessemployee.ui.HomeActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : BaseActivity<ActivityTimelogBinding,AttendanceViewModel>(),
    OnDateSetListener {

    private lateinit var attendanceReportAdapter: AttendanceReportAdapter
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




        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }


        binding.fromDate.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding?.fromDate?.text = sdf.format(datePicker.selection?.first)
                Toast.makeText(this, "${datePicker.headerText} is selected", Toast.LENGTH_LONG).show()
            }
            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {

            }
            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {

            }

        }



        binding.toDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding?.toDate?.text = sdf.format(datePicker.selection?.first)
                Toast.makeText(this, "${datePicker.headerText} is selected", Toast.LENGTH_LONG).show()
            }
            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {

            }
            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {

            }
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

}