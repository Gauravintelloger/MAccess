package technology.dubaileading.maccessemployee.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.FragmentProfileBinding
import technology.dubaileading.maccessemployee.rest.entity.GetLeave
import technology.dubaileading.maccessemployee.rest.entity.LeaveDataItem
import technology.dubaileading.maccessemployee.rest.entity.Profile
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.attendance.AttendanceActivity
import technology.dubaileading.maccessemployee.ui.change_password.ChangePasswordActivity
import technology.dubaileading.maccessemployee.ui.personal_info.PersonalInfoActivity
import technology.dubaileading.maccessemployee.ui.settings.SettingsActivity
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.CustomDialog

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var leaveAdapter: LeaveAdapter
    private lateinit var viewBinding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    private var profileObserver: Observer<DataState<Profile>> =
        androidx.lifecycle.Observer<DataState<Profile>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateProfileResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
            }
        }

    private var leavesObserver: Observer<DataState<GetLeave>> =
        androidx.lifecycle.Observer<DataState<GetLeave>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateLeaveResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
            }
        }

    private fun validateProfileResponse(body: Profile) {
        if (activity != null && isAdded) {
            if (body.status == Constants.API_RESPONSE_CODE.OK) {
                if (body.profileData?.photo != null) {
                    viewBinding.profilePicView.load(body.profileData.photo) {
                        transformations(CircleCropTransformation())
                    }
                } else {
                    viewBinding.profilePicView.load(SessionManager.user?.organisationLogo) {
                        transformations(CircleCropTransformation())
                    }
                }
                viewBinding.nameText.text = body.profileData?.name.toString()
                viewBinding.positionTv.text = body.profileData?.designation?.title.toString()


            } else if (body.status == Constants.API_RESPONSE_CODE.NOT_OK && body.statuscode == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                    R.string.tokenExpiredDesc
                ),
                    object : CustomDialog.OnClickListener {
                        override fun okButtonClicked() {
                            (activity as? HomeActivity?)?.logoutUser()
                        }
                    })
            } else {
                CustomDialog(requireContext()).showInformationDialog(body.message)
            }

        }
    }

    private fun validateLeaveResponse(body: GetLeave) {
        if (activity != null && isAdded) {
            if (body.status == Constants.API_RESPONSE_CODE.OK) {
                viewBinding.availableLeave.text = body.data?.sumAvailableLeave.toString()
                viewBinding.totalLeave.text = "/" + body.data?.sumTotalLeave.toString()
                viewBinding.leaveRequested.text = body.data?.leaveRequested.toString()
                leaveAdapter.addList(body.data?.leaveData as ArrayList<LeaveDataItem>)

            } else if (body.status == Constants.API_RESPONSE_CODE.NOT_OK && body.statuscode == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                    R.string.tokenExpiredDesc
                ),
                    object : CustomDialog.OnClickListener {
                        override fun okButtonClicked() {
                            (activity as? HomeActivity?)?.logoutUser()
                        }
                    })
            } else {
                CustomDialog(requireContext()).showInformationDialog(body.message)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        SessionManager.init(requireContext())
        initLeaverecyclerAdapter()
        getProfileFromRemote()
        getLeavesFromRemote()
        setUpListeners()
        return viewBinding.root
    }

    private fun initLeaverecyclerAdapter() {
        leaveAdapter = LeaveAdapter(requireContext())
        viewBinding.leaveRv.itemAnimator = DefaultItemAnimator()
        viewBinding.leaveRv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.leaveRv.adapter = leaveAdapter
    }

    private fun getProfileFromRemote() {
        viewModel.profile()
        viewModel.profile.observe(viewLifecycleOwner, profileObserver)
    }

    private fun getLeavesFromRemote() {
        viewModel.leaves()
        viewModel.leaves.observe(viewLifecycleOwner, leavesObserver)
    }

    private fun setUpListeners() {
        viewBinding.personalInfo.setOnClickListener {
            startActivity(Intent(activity, PersonalInfoActivity::class.java))
        }

        viewBinding.changePass.setOnClickListener {
            startActivity(Intent(activity, ChangePasswordActivity::class.java))
        }

        viewBinding.attendanceLog.setOnClickListener {
            startActivity(Intent(activity, AttendanceActivity::class.java))
        }

        viewBinding.settings.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
        }

        viewBinding.logOut.setOnClickListener {
            val message: String = if (SessionManager.isTimerRunning == true) {
                "Timer is Running,Timer will get cleared Do you want to logout from mAccess?"
            } else {
                "Do you want to logout from mAccess?"
            }
            CustomDialog(requireActivity()).showDecisionButtonDialog(
                message,
                "Yes",
                "No",
                true,
                object : CustomDialog.onUserActionCLickListener {
                    override fun negativeButtonClicked() {

                    }

                    override fun positiveButtonClicked() {
                        (activity as? HomeActivity?)?.logoutUser()
                    }

                })
        }

    }


}
