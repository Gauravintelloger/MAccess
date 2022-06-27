package technology.dubaileading.maccessuser.ui.coming_soon

import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import technology.dubaileading.maccessuser.R
import technology.dubaileading.maccessuser.base.BaseFragment
import technology.dubaileading.maccessuser.databinding.FragmentHomeBinding
import technology.dubaileading.maccessuser.databinding.FragmentServicesBinding

class ComingSoonFragment : BaseFragment<FragmentServicesBinding,ComingSoonViewModel>() {

    override fun createViewModel(): ComingSoonViewModel {
        return ViewModelProvider(this).get(ComingSoonViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentServicesBinding {
        return FragmentServicesBinding.inflate(layoutInflater!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}