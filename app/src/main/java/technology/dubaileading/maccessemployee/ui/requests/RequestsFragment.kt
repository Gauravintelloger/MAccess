package technology.dubaileading.maccessemployee.ui.requests

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentRequestsBinding

class RequestsFragment  : BaseFragment<FragmentRequestsBinding, RequestsViewModel>() {


    override fun createViewModel(): RequestsViewModel {
        return ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentRequestsBinding {
        return FragmentRequestsBinding.inflate(layoutInflater!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.newRequest?.setOnClickListener {
            newRequest()
        }
    }

    private fun newRequest() {
        var newRequestDialog: BottomSheetDialog
        val btnsheet = layoutInflater.inflate(R.layout.new_request_layout, null)
        newRequestDialog = BottomSheetDialog(requireContext())
        val requestLeave = btnsheet.findViewById<LinearLayout>(R.id.request_leave)
        val requestDoc = btnsheet.findViewById<LinearLayout>(R.id.request_doc)

        requestLeave.setOnClickListener {
            newRequestDialog.dismiss()
            newLeaveRequest()
        }

        requestDoc.setOnClickListener {
            newRequestDialog.dismiss()
            newDocRequest()
        }
        newRequestDialog.setContentView(btnsheet)
        newRequestDialog.show()
    }

    private fun newDocRequest() {
        var newDocDialog: BottomSheetDialog
        val btnsheet = layoutInflater.inflate(R.layout.document_request, null)
        newDocDialog = BottomSheetDialog(requireContext())


        newDocDialog.setContentView(btnsheet)
        newDocDialog.show()

    }

    private fun newLeaveRequest() {
        var newLeaveDialog: BottomSheetDialog
        val btnsheet = layoutInflater.inflate(R.layout.leave_request, null)
        newLeaveDialog = BottomSheetDialog(requireContext())


        newLeaveDialog.setContentView(btnsheet)
        newLeaveDialog.show()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.requestsRv?.itemAnimator = DefaultItemAnimator()
        binding?.requestsRv?.layoutManager = LinearLayoutManager(activity)
        binding?.requestsRv?.adapter = RequestsAdapter()

    }


}
