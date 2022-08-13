package technology.dubaileading.maccessemployee.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import coil.load
import coil.transform.CircleCropTransformation
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentProfileBinding
import technology.dubaileading.maccessemployee.ui.change_password.ChangePasswordActivity
import technology.dubaileading.maccessemployee.ui.personal_info.PersonalInfoActivity

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    val totalLeaves: Float = 100f
    val alLeaves: Float = 20F
    val slLeaves: Float = 10f
    val clLeaves: Float = 20f
    val availableLeaves: Float = 50f

    override fun createViewModel(): ProfileViewModel {
        return ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater!!);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.profilePicView?.load(R.drawable.dlt_single_logo){
            transformations(CircleCropTransformation())
        }

        val linearAlViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,alLeaves)
        linearAlViewParams.weight = alLeaves
        binding?.al?.layoutParams = linearAlViewParams


        val linearSlViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,slLeaves)
        //linearAlViewParams.weight = slLeaves
        binding?.sl?.layoutParams = linearSlViewParams

        val linearClViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,clLeaves)
        // linearAlViewParams.weight = clLeaves
        binding?.cl?.layoutParams = linearClViewParams

        val linearAvaillViewParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,availableLeaves)
        //    linearAlViewParams.weight = availableLeaves
        binding?.restLeave?.layoutParams = linearAvaillViewParams

        binding?.progressView?.weightSum = totalLeaves
        binding?.progressView?.removeAllViews()
        binding?.progressView?.addView(binding?.al)
        binding?.progressView?.addView(binding?.sl)
        binding?.progressView?.addView(binding?.cl)
        binding?.progressView?.addView(binding?.restLeave)

        binding?.personalInfo?.setOnClickListener {
            startActivity(Intent(activity, PersonalInfoActivity::class.java))
        }

        binding?.changePass?.setOnClickListener {
            startActivity(Intent(activity, ChangePasswordActivity::class.java))
        }
    }



}
