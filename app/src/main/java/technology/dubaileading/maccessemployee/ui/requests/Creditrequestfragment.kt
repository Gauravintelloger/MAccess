package technology.dubaileading.maccessemployee.ui.requests

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.FragmentCreditrequestfragmentBinding
import technology.dubaileading.maccessemployee.rest.entity.deletecreditrequest.Deletecreditrequest
import technology.dubaileading.maccessemployee.rest.entity.timecreditrequestlist.TimecreaditrequestlistModel
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.ui.timecreaditrequest.Creditlistclick
import technology.dubaileading.maccessemployee.ui.timecreaditrequest.TimeCreditlistAdaptor
import technology.dubaileading.maccessemployee.ui.timecreaditrequest.TimecreditViewmodel
import technology.dubaileading.maccessemployee.ui.timecreaditrequest.Timecreditapply
import technology.dubaileading.maccessemployee.utility.DataState
import technology.dubaileading.maccessemployee.utility.hide
import technology.dubaileading.maccessemployee.utility.show
import technology.dubaileading.maccessemployee.utility.showToast
import technology.dubaileading.maccessemployee.utils.CustomDialog
import java.util.*

@AndroidEntryPoint
class Creditrequestfragment: Fragment(), Creditlistclick {
    private val viewModel: TimecreditViewmodel by viewModels()
    private lateinit var viewBinding: FragmentCreditrequestfragmentBinding
    private lateinit var attendanceReportAdapter: TimeCreditlistAdaptor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCreditrequestfragmentBinding.inflate(inflater, container, false)
        setAttendanceReportAdapter()

        getAttendanceReportFromRemote()
        viewBinding.submitMaterialButton.visibility=View.GONE
        viewBinding.submitMaterialButton.setOnClickListener {
            startActivity(Intent(requireContext(), Timecreditapply::class.java))
            requireActivity().finish()
        }
        return viewBinding.root
    }



    private fun setAttendanceReportAdapter() {
        attendanceReportAdapter = TimeCreditlistAdaptor(requireActivity(), this,listOf())
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        viewBinding.recyclerView.adapter = attendanceReportAdapter

    }




    fun getAttendanceReportFromRemote() {
        viewModel.timecreditlist()
        viewModel.posts.observe(requireActivity(), attendanceReportsObserver)
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
                    requireActivity().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
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
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }


    fun logoutUser() {
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finishAffinity()
        requireActivity().showToast("Logged out Successfully")
    }

    override fun onDeleteClicked(id: Int) {
        CustomDialog(requireActivity()).showDecisionButtonDialog(
            "Do you want to delete the request?",
            "Yes",
            "No",
            true,
            object : CustomDialog.onUserActionCLickListener {
                override fun negativeButtonClicked() {

                }

                override fun positiveButtonClicked() {
                    viewModel.deleteTimeCreditRequest(id.toString())
                    viewModel.deleteLeave.observe(requireActivity(), deleteLeaveRequestObserver)
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
                    requireActivity().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.progressBar.hide()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
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
            requireActivity().showToast(response.message)
            getAttendanceReportFromRemote()
        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }

}