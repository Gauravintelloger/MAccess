package technology.dubaileading.maccessemployee.ui.services

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentBankingBinding
import technology.dubaileading.maccessemployee.databinding.FragmentInsuranceBinding

class InsuranceFragment: BaseFragment<FragmentInsuranceBinding, InsuranceViewModel>() {

    override fun createViewModel(): InsuranceViewModel {
        return ViewModelProvider(this).get(InsuranceViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentInsuranceBinding {
        return FragmentInsuranceBinding.inflate(layoutInflater!!);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}