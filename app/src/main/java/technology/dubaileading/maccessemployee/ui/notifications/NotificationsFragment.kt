package technology.dubaileading.maccessemployee.ui.notifications

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentNotificationsBinding
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.AttendenceReport
import technology.dubaileading.maccessemployee.rest.entity.DataItem
import technology.dubaileading.maccessemployee.rest.entity.Notifications
import technology.dubaileading.maccessemployee.rest.request.ServerRequestFactory
import technology.dubaileading.maccessemployee.rest.request.SuccessCallback
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utils.AppShared


class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>() {

    override fun createViewModel(): NotificationsViewModel {
        return ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentNotificationsBinding {
        return FragmentNotificationsBinding.inflate(layoutInflater!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        getNotifications()
    }

    private fun getNotifications() {

        val requestFactory = ServerRequestFactory()
        val call = requestFactory
            .obtainEndpointProxy(EmployeeEndpoint::class.java)
            .notifications

        val request = requestFactory.newHttpRequest<Any>(activity)
            .withEndpoint(call)
            .withProgressDialogue()
            .withSuccessAndFailureCallback(object : SuccessCallback<Notifications?>{
                override fun onSuccess(notifications: Notifications?) {
                    if (notifications?.status == "ok") {
                        if (notifications.notificationData != null){

                        }

                    } else if (notifications?.status == "notok" && notifications?.statuscode == "500"){
                        Toast.makeText(activity, "Token expired", Toast.LENGTH_LONG).show()
                        AppShared(requireContext()).saveToken("")

                        startActivity(Intent(activity, LoginActivity::class.java))

                    }

                }

            }){
                Toast.makeText(activity,"error", Toast.LENGTH_LONG).show()
            }.build()
        request.executeAsync()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.notificationRv?.itemAnimator = DefaultItemAnimator()
        binding?.notificationRv?.layoutManager = LinearLayoutManager(activity)
        binding?.notificationRv?.adapter = NotificationAdapter()

    }




}
