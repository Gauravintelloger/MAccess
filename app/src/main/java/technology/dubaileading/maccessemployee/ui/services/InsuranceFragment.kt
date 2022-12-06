package technology.dubaileading.maccessemployee.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import technology.dubaileading.maccessemployee.databinding.FragmentInsuranceBinding

class InsuranceFragment : Fragment() {

    private lateinit var viewBinding: FragmentInsuranceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentInsuranceBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

}