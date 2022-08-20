package technology.dubaileading.maccessemployee.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentServicesBinding

class ServicesFragment : BaseFragment<FragmentServicesBinding, ServicesViewModel>() {


    override fun createViewModel(): ServicesViewModel {
        return ViewModelProvider(this).get(ServicesViewModel::class.java)
    }



    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentServicesBinding {
        return FragmentServicesBinding.inflate(layoutInflater!!);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.materialToolbar?.setOnClickListener {
            activity?.onBackPressed()
        }

        binding?.tabLay?.addTab(binding?.tabLay?.newTab()!!.setText("Banking"))
        binding?.tabLay?.addTab(binding?.tabLay?.newTab()!!.setText("Insurance"))


        val fragmentManager: FragmentManager = activity?.supportFragmentManager !!
        binding?.viewPager?.adapter = ServicesFragmentAdapter(fragmentManager,lifecycle)

        binding?.tabLay?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding?.viewPager?.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        binding?.viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding?.tabLay?.selectTab( binding?.tabLay?.getTabAt(position))

            }
        })

    }

}