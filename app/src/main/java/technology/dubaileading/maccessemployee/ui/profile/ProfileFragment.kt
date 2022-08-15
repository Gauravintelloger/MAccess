package technology.dubaileading.maccessemployee.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import coil.load
import coil.transform.CircleCropTransformation
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentProfileBinding
import technology.dubaileading.maccessemployee.ui.attendance.AttendanceActivity
import technology.dubaileading.maccessemployee.ui.change_password.ChangePasswordActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.ui.personal_info.PersonalInfoActivity
import technology.dubaileading.maccessemployee.ui.settings.SettingsActivity
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.Constants

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    val totalLeaves: Float = 100f
    val alLeaves: Float = 20F
    val slLeaves: Float = 10f
    val clLeaves: Float = 20f
    val availableLeaves: Float = 50f

    override fun createViewModel(): ProfileViewModel {
        return ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater!!);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var user = AppShared(requireContext()).getUser()
        var logo = Constants.photoUrl+user.data?.organisationLogo

        binding?.profilePicView?.load(logo){
            transformations(CircleCropTransformation())
        }

        viewModel?.getProfile(requireContext())

        viewModel?.profileData?.observe(viewLifecycleOwner){
            if (it.profileData?.photo != null){
                binding?.profilePicView?.load(it.profileData?.photo){
                    transformations(CircleCropTransformation())
                }
            }else{
                binding?.profilePicView?.load(logo){
                    transformations(CircleCropTransformation())
                }
            }

            binding?.nameText?.text = it.profileData?.name.toString()
            binding?.positionTv?.text = it.profileData?.designation?.title.toString()



        }

        val linearAlViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,alLeaves)
        linearAlViewParams.weight = alLeaves
        binding?.al?.layoutParams = linearAlViewParams


        val linearSlViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,slLeaves)
        //linearAlViewParams.weight = slLeaves
        binding?.sl?.layoutParams = linearSlViewParams

        val linearClViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,clLeaves)
        //linearAlViewParams.weight = clLeaves
        binding?.cl?.layoutParams = linearClViewParams

        val linearAvaillViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,availableLeaves)
        //linearAlViewParams.weight = availableLeaves
        binding?.restLeave?.layoutParams = linearAvaillViewParams

        binding?.progressView?.weightSum = totalLeaves
        binding?.progressView?.removeAllViews()
        binding?.progressView?.addView(binding?.al)
        binding?.progressView?.addView(binding?.sl)
        binding?.progressView?.addView(binding?.cl)
        binding?.progressView?.addView(binding?.restLeave)

        binding?.materialToolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding?.personalInfo?.setOnClickListener {
            startActivity(Intent(activity, PersonalInfoActivity::class.java))
        }

        binding?.changePass?.setOnClickListener {
            startActivity(Intent(activity, ChangePasswordActivity::class.java))
        }

        binding?.attendanceLog?.setOnClickListener {
            startActivity(Intent(activity, AttendanceActivity::class.java))
        }

        binding?.settings?.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
        }

        binding?.logOut?.setOnClickListener {
            var alertDialog = AlertDialog.Builder(activity as Context)
            alertDialog.setTitle("Logout?")
            alertDialog.setMessage("Do you want to logout from mAccess?")
            alertDialog.setPositiveButton("Yes") { _, _ ->
                run {
                    AppShared(activity as Context).clearAll()
                    startActivity(Intent(activity, LoginActivity::class.java))
                    (activity as Activity).finishAffinity()
                }
            }
            alertDialog.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()

            }

            alertDialog.show()
        }

    }



}
