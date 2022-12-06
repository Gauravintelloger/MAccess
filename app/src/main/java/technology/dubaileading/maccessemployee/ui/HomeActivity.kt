package technology.dubaileading.maccessemployee.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.badge.BadgeDrawable
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.ActivityHomeBinding
import technology.dubaileading.maccessemployee.rest.entity.NotificationCount
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utility.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var count: BadgeDrawable
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewBinding: ActivityHomeBinding

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loadNotificationCountFromRemote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val intentFilter = IntentFilter("IntentFilterAction")
        LocalBroadcastManager.getInstance(this@HomeActivity)
            .registerReceiver(broadcastReceiver, intentFilter)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = host.navController
        viewBinding.bottomNavigationViewInclude.bottomNavigationView.selectedItemId = R.id.home

        setCountColor()
        setUpListeners()
        loadNotificationCountFromRemote()

    }

    private fun setUpListeners() {
        viewBinding.bottomNavigationViewInclude.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.services -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.servicesFragment,
                        null,
                        getNavBuilder().build()
                    )
                }
                R.id.requests -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.requestsFragment,
                        null,
                        getNavBuilder().build()
                    )
                }
                R.id.home -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.homeFragment,
                        null,
                        getNavBuilder().build()
                    )
                }
                R.id.notifications -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.notificationsFragment,
                        null,
                        getNavBuilder().build()
                    )
                }
                R.id.profile -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.profileFragment,
                        null,
                        getNavBuilder().build()
                    )

                }
            }
            return@setOnItemSelectedListener true

        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.label == "CheckInFragment" || destination.label == "CheckOutFragment") {
                viewBinding.bottomNavigationLinearLayout.hide()
                viewBinding.homeImageView.hide()
                viewBinding.homeImageButtonFrameLayout.hide()
            } else {
                viewBinding.bottomNavigationLinearLayout.show()
                viewBinding.homeImageView.show()
                viewBinding.homeImageButtonFrameLayout.show()
                if (destination.label == "HomeFragment") {
                    viewBinding.homeImageButtonFrameLayout.background =
                        ContextCompat.getDrawable(this, R.drawable.hom_fab)
                    viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(2)?.isChecked =
                        true
                } else {
                    if (destination.label == "ServicesFragment") {
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(0)?.isChecked =
                            true
                    } else if (destination.label == "RequestsFragment") {
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(1)?.isChecked =
                            true
                    } else if (destination.label == "NotificationsFragment") {
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(3)?.isChecked =
                            true
                    } else if (destination.label == "ProfileFragment") {
                        viewBinding.bottomNavigationViewInclude.bottomNavigationView.menu.getItem(4)?.isChecked =
                            true
                    }
                    viewBinding.homeImageButtonFrameLayout.background =
                        ContextCompat.getDrawable(this, R.drawable.hom_fab_disable)
                }
            }
        }

        viewBinding.homeImageButton.setOnClickListener {
            viewBinding.bottomNavigationViewInclude.bottomNavigationView.selectedItemId = R.id.home
        }

    }

    private fun setCountColor() {
        count =
            viewBinding.bottomNavigationViewInclude.bottomNavigationView.getOrCreateBadge(R.id.notifications)
        count.backgroundColor = Color.RED
        count.badgeTextColor = Color.WHITE
    }

    private fun loadNotificationCountFromRemote() {
        viewModel.notificationCount()
        viewModel.notificationCount.observe(this, notificationCountObserver)
    }

    private var notificationCountObserver: Observer<DataState<NotificationCount>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    validateNotificationCount(it.item)
                }
                is DataState.Error -> {
                }
            }
        }

    private fun validateNotificationCount(body: NotificationCount) {
        if (body.status == Constants.API_RESPONSE_CODE.OK) {
            if (body.data != 0) {
                count.number = body.data!!
                count.isVisible = true
            } else {
                count.isVisible = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this@HomeActivity).unregisterReceiver(broadcastReceiver)
    }

    fun logoutUser() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
        showToast("Logged out Successfully")
    }

}