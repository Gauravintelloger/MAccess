package technology.dubaileading.maccessemployee.ui.personal_info

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityPersonalInfoBinding


class PersonalInfoActivity : BaseActivity<ActivityPersonalInfoBinding, PersonalInfoViewModel>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfile(this@PersonalInfoActivity)
        viewModel.profileData.observe(this){
            binding?.name?.setText(it?.profileData?.name.toString())
            binding?.email?.setText(it?.profileData?.email.toString())
            binding?.dob?.setText(it?.profileData?.dateOfBirth.toString())

        }
    }

    override fun createViewModel(): PersonalInfoViewModel {
        return ViewModelProvider(this).get(PersonalInfoViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivityPersonalInfoBinding {
        return ActivityPersonalInfoBinding.inflate(layoutInflater!!)
    }
}