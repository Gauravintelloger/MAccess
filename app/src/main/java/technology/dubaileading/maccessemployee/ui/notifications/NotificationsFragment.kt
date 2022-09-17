package technology.dubaileading.maccessemployee.ui.notifications


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentNotificationsBinding
import technology.dubaileading.maccessemployee.rest.entity.NotificationData


class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>() {
    private lateinit var notificationAdapter: NotificationAdapter

    override fun createViewModel(): NotificationsViewModel {
        return ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentNotificationsBinding {
        return FragmentNotificationsBinding.inflate(layoutInflater!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationAdapter = NotificationAdapter(requireContext())

    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.notificationRv?.itemAnimator = DefaultItemAnimator()
        binding?.notificationRv?.layoutManager = LinearLayoutManager(activity)
        binding?.notificationRv?.adapter = notificationAdapter
        viewModel?.getNotifications(requireContext())

        viewModel?.notificationsList?.observe(viewLifecycleOwner){
            notificationAdapter.addList(it.notificationData as ArrayList<NotificationData>)

        }

        viewModel?.error?.observe(viewLifecycleOwner){
        }

        binding?.materialToolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }


    }




}
