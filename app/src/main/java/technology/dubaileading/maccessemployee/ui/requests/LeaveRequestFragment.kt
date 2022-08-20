package technology.dubaileading.maccessemployee.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentLeaveRequestBinding
import technology.dubaileading.maccessemployee.rest.entity.LeaveRequestsItem

import technology.dubaileading.maccessemployee.utils.Constants

class LeaveRequestFragment : BaseFragment<FragmentLeaveRequestBinding, RequestsViewModel>() {
    private lateinit var leaveRequestsAdapter: LeaveRequestsAdapter
    private var leaveRequests = ArrayList<LeaveRequestsItem>()

    override fun createViewModel(): RequestsViewModel {
        return ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentLeaveRequestBinding {
        return FragmentLeaveRequestBinding.inflate(layoutInflater!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        leaveRequestsAdapter = LeaveRequestsAdapter(requireContext())
        leaveRequests = arguments?.getParcelableArrayList<LeaveRequestsItem>(Constants.LEAVE_REQUEST)!!
        leaveRequestsAdapter.setData(leaveRequests)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.leaveRv?.itemAnimator = DefaultItemAnimator()
        binding?.leaveRv?.layoutManager = LinearLayoutManager(activity)
        binding?.leaveRv?.adapter = leaveRequestsAdapter

    }


}