package technology.dubaileading.maccessemployee.ui.services

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentProfileBinding
import technology.dubaileading.maccessemployee.databinding.FragmentServicesBinding
import technology.dubaileading.maccessemployee.ui.profile.ProfileViewModel

class ServicesFragment : BaseFragment<FragmentServicesBinding, ServicesViewModel>() {


    override fun createViewModel(): ServicesViewModel {
        return ViewModelProvider(this).get(ServicesViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentServicesBinding {
        return FragmentServicesBinding.inflate(layoutInflater!!);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}