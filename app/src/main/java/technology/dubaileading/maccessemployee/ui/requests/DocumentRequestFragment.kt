package technology.dubaileading.maccessemployee.ui.requests

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentDocumentRequestBinding

import technology.dubaileading.maccessemployee.rest.entity.OtherRequestsItem
import technology.dubaileading.maccessemployee.utils.Constants

class DocumentRequestFragment  : BaseFragment<FragmentDocumentRequestBinding, RequestsViewModel>() {
    private lateinit var documentRequestAdapter: DocumentRequestAdapter
    private var otherRequestsItem = ArrayList<OtherRequestsItem>()
    override fun createViewModel(): RequestsViewModel {
        return ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentDocumentRequestBinding {
        return FragmentDocumentRequestBinding.inflate(layoutInflater!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        documentRequestAdapter = DocumentRequestAdapter(requireContext())
        otherRequestsItem = arguments?.getParcelableArrayList<OtherRequestsItem>(Constants.OTHER_REQUEST)!!
        documentRequestAdapter.setData(otherRequestsItem)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.documentRv?.itemAnimator = DefaultItemAnimator()
        binding?.documentRv?.layoutManager = LinearLayoutManager(activity)
        binding?.documentRv?.adapter = documentRequestAdapter

    }

}