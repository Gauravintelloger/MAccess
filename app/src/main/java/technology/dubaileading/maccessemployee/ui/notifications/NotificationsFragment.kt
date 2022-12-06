package technology.dubaileading.maccessemployee.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.FragmentNotificationsBinding
import technology.dubaileading.maccessemployee.rest.entity.Notifications
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.CustomDialog
import java.util.*

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    private lateinit var notificationAdapter: NotificationAdapter
    private val viewModel by viewModels<NotificationsViewModel>()
    private lateinit var viewBinding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
        SessionManager.init(requireContext())
        initNotificationsRecyclerList()
        loadAllNotificationsFromRemote()
        return viewBinding.root
    }

    private fun initNotificationsRecyclerList() {
        viewBinding.recyclerView.initRecyclerView(
            DefaultItemAnimator(), LinearLayoutManager(context)
        )
        notificationAdapter = NotificationAdapter(requireContext(), listOf())
        viewBinding.recyclerView.adapter = notificationAdapter
    }


    private fun loadAllNotificationsFromRemote() {
        viewModel.notifications()
        viewModel.notifications.observe(viewLifecycleOwner, notificationsObserver)
    }

    private var notificationsObserver: Observer<DataState<Notifications>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validateNotificationsData(it.item)
                }
                is DataState.Error -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    requireContext().showToast(it.error.toString())
                }
            }
        }

    private fun validateNotificationsData(response: Notifications) {
        if (activity != null && isAdded) {
            if (response.status == Constants.API_RESPONSE_CODE.OK) {
                if (response.notificationData != null && response.notificationData.isNotEmpty()) {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.show()
                    notificationAdapter.addList(response.notificationData)
                } else {
                    notificationAdapter.addList(Collections.emptyList())

                    viewBinding.errorLayout.errorText.text = "No Data Found"
                    viewBinding.errorLayout.root.show()
                    viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                }


            } else if (response.status == Constants.API_RESPONSE_CODE.NOT_OK && response.statuscode == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                    R.string.tokenExpiredDesc
                ), object : CustomDialog.OnClickListener {
                    override fun okButtonClicked() {
                        (activity as? HomeActivity?)?.logoutUser()
                    }
                })
            } else {
                CustomDialog(requireContext()).showInformationDialog(response.message)
            }

        }
    }
}
