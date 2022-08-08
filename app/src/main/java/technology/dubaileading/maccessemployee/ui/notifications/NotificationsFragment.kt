package technology.dubaileading.maccessemployee.ui.notifications


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
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
        notificationAdapter = NotificationAdapter()

    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.notificationRv?.itemAnimator = DefaultItemAnimator()
        binding?.notificationRv?.layoutManager = LinearLayoutManager(activity)
        binding?.notificationRv?.adapter = notificationAdapter
        viewModel?.getNotifications(requireContext())

        viewModel?.notificationsList?.observe(viewLifecycleOwner){
            //Toast.makeText(activity, it.message,Toast.LENGTH_LONG).show()
            notificationAdapter.addList(it.notificationData as ArrayList<NotificationData>)

        }

        viewModel?.invalidUser?.observe(viewLifecycleOwner){
            //Toast.makeText(activity, it.message,Toast.LENGTH_LONG).show()
        }


    }




}
