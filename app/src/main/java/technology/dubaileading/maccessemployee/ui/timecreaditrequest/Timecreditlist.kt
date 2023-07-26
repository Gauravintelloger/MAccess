package technology.dubaileading.maccessemployee.ui.timecreaditrequest

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.ActivityPayslipBinding
import technology.dubaileading.maccessemployee.databinding.ActivityTimecreditlistBinding
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.DeleteReq
import technology.dubaileading.maccessemployee.rest.entity.ReportRequest
import technology.dubaileading.maccessemployee.rest.entity.deletecreditrequest.Deletecreditrequest
import technology.dubaileading.maccessemployee.rest.entity.paysliplistmodel.Paysliplistmodel
import technology.dubaileading.maccessemployee.rest.entity.timecreditrequestlist.TimecreaditrequestlistModel
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.ui.payslip.PayslipAdaptor
import technology.dubaileading.maccessemployee.ui.payslip.PayslipViewModel
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*

//class Timecreditlist : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_timecreditlist)
//    }
//}
@AndroidEntryPoint
class Timecreditlist : AppCompatActivity(), Creditlistclick {
    private val viewModel: TimecreditViewmodel by viewModels()
    private lateinit var viewBinding: ActivityTimecreditlistBinding
    private lateinit var attendanceReportAdapter: TimeCreditlistAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_timecreditlist)
        setAttendanceReportAdapter()

        setUpListeners()

        getAttendanceReportFromRemote()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Timecreditlist, HomeActivity::class.java))
                finish()
            }
        })

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }


        viewBinding.submitMaterialButton.setOnClickListener {
            startActivity(Intent(this@Timecreditlist, Timecreditapply::class.java))
            finish()
        }
    }


    private fun setAttendanceReportAdapter() {
        attendanceReportAdapter = TimeCreditlistAdaptor(this@Timecreditlist, this,listOf())
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this@Timecreditlist)
        viewBinding.recyclerView.adapter = attendanceReportAdapter

    }




    private fun getAttendanceReportFromRemote() {
        viewModel.timecreditlist()
        viewModel.posts.observe(this, attendanceReportsObserver)
    }

    private var attendanceReportsObserver: Observer<DataState<TimecreaditrequestlistModel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validateAttendanceReportData(it.item)
                }
                is DataState.Error -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                logoutUser()
                            }
                        })
                }
            }
        }


    private fun validateAttendanceReportData(response: TimecreaditrequestlistModel) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            if (response?.data != null && response.data.isNotEmpty()) {
                viewBinding.errorLayout.root.hide()
                viewBinding.recyclerView.show()
                attendanceReportAdapter.addList(response.data)
            } else {
                attendanceReportAdapter.addList(Collections.emptyList())

                viewBinding.errorLayout.errorText.text = "No Data Found"
                viewBinding.errorLayout.root.show()
                viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
            }


        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }

    private fun setUpListeners() {
        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Timecreditlist, HomeActivity::class.java))
            finish()
        }






    }


    fun logoutUser() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finishAffinity()
        showToast("Logged out Successfully")
    }

    override fun onDeleteClicked(id: Int) {
        CustomDialog(this).showDecisionButtonDialog(
            "Do you want to delete the request?",
            "Yes",
            "No",
            true,
            object : CustomDialog.onUserActionCLickListener {
                override fun negativeButtonClicked() {

                }

                override fun positiveButtonClicked() {

                    viewModel.deleteTimeCreditRequest(id.toString())
                    viewModel.deleteLeave.observe(this@Timecreditlist, deleteLeaveRequestObserver)

                }

            })

    }

    private var deleteLeaveRequestObserver: Observer<DataState<Deletecreditrequest>> =
        androidx.lifecycle.Observer<DataState<Deletecreditrequest>> {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validateDeleteRequestResponse(it.item)
                }
                is DataState.Error -> {
                    viewBinding.progressBar.hide()
                   this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.progressBar.hide()
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (this as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateDeleteRequestResponse(response: Deletecreditrequest) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(response.message)
            getAttendanceReportFromRemote()
        } else {
            CustomDialog(this@Timecreditlist).showInformationDialog(response.message)
        }

    }

}