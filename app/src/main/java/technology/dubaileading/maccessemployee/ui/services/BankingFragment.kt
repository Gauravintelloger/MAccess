package technology.dubaileading.maccessemployee.ui.services

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentBankingBinding


class BankingFragment : BaseFragment<FragmentBankingBinding, BankingViewModel>() {


    override fun createViewModel(): BankingViewModel {
        return ViewModelProvider(this).get(BankingViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentBankingBinding {
        return FragmentBankingBinding.inflate(layoutInflater!!);
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}