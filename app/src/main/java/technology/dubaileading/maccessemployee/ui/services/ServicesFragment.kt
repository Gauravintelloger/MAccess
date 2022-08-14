package technology.dubaileading.maccessemployee.ui.services

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentServicesBinding

class ServicesFragment : BaseFragment<FragmentServicesBinding, ServicesViewModel>() {


    override fun createViewModel(): ServicesViewModel {
        return ViewModelProvider(this).get(ServicesViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentServicesBinding {
        return FragmentServicesBinding.inflate(layoutInflater!!)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.materialToolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding?.banking?.setOnClickListener {
            binding?.bankComingSoon?.visibility = View.VISIBLE
            binding?.insuranceLay?.visibility = View.GONE


            binding?.banking?.setTextColor(R.color.text_color_primary)
            binding?.banking?.background = ContextCompat.getDrawable(this.requireContext(),R.drawable.button_bg)

            binding?.insurance?.background = null
            binding?.insurance?.setTextColor(ContextCompat.getColor(this.requireContext(),R.color.text_color_light))
        }

        binding?.insurance?.setOnClickListener {
            binding?.bankComingSoon?.visibility = View.GONE
            binding?.insuranceLay?.visibility = View.VISIBLE

            binding?.insurance?.background = ContextCompat.getDrawable(this.requireContext(),R.drawable.button_bg)
            binding?.insurance?.setTextColor(R.color.text_color_primary)

            binding?.banking?.background = null
            binding?.banking?.setTextColor(ContextCompat.getColor(this.requireContext(),R.color.text_color_light))

        }

    }



}
