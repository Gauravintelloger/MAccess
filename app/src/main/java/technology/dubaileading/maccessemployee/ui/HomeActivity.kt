package technology.dubaileading.maccessemployee.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.entity.NotificationCount
import technology.dubaileading.maccessemployee.rest.entity.checkMattendancePermission.checkMattendancePermissionModel
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
        SessionManager.init(this)
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
        viewModel.checkMattendancePermission()
        viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)

    }


    private var loginObserver: Observer<DataState<checkMattendancePermissionModel>> =
        androidx.lifecycle.Observer<DataState<checkMattendancePermissionModel>> {
            when (it) {
                is DataState.Loading -> {
                    //showProgress()
                }
                is DataState.Success -> {
                   // dismissProgress()
                    SessionManager.attendenceaccess=it.item.data.mobileAttendanceAllowed.toString()
                    SessionManager.requestmoduleaccess=it.item.data.requestmoduleaccess.toString()
                    SessionManager.postupdate=it.item.data.isPostView.toString()

                }
                is DataState.Error -> {
                   // dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                }
            }
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
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.requests -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.requestsFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.home -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.homeFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.notifications -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.notificationsFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)
                }
                R.id.profile -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        R.id.profileFragment,
                        null,
                        getNavBuilder().build()
                    )
                    viewModel.checkMattendancePermission()
                    viewModel.checkMattendancePermissionresponse.observe(this, loginObserver)

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
        count = viewBinding.bottomNavigationViewInclude.bottomNavigationView.getOrCreateBadge(R.id.notifications)
        count.backgroundColor = Color.RED
        count.badgeTextColor = Color.WHITE
    }

    fun loadNotificationCountFromRemote() {
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
                is DataState.TokenExpired -> {
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
        SessionManager.deleteAllUserInfo()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
        showToast("Logged out Successfully")
    }

}