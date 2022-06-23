package technology.dubaileading.maccessuser.ui.home_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import technology.dubaileading.maccessuser.base.BaseFragment
import technology.dubaileading.maccessuser.databinding.ActivityLoginBinding
import technology.dubaileading.maccessuser.databinding.FragmentHomeBinding
import technology.dubaileading.maccessuser.ui.login.LoginViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding,HomeFragmentViewModel>() {


    override fun createViewModel(): HomeFragmentViewModel {
        return ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.rv?.itemAnimator = DefaultItemAnimator()
        binding?.rv?.layoutManager = LinearLayoutManager(activity)
        binding?.rv?.adapter = HomeAdapter()
    }

}