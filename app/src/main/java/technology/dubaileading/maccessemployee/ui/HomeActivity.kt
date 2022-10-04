package technology.dubaileading.maccessemployee.ui

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityHomeBinding
import technology.dubaileading.maccessemployee.ui.home_fragment.HomeFragment
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.ui.notifications.NotificationsFragment
import technology.dubaileading.maccessemployee.ui.profile.ProfileFragment
import technology.dubaileading.maccessemployee.ui.requests.RequestsFragment
import technology.dubaileading.maccessemployee.ui.services.ServicesFragment
import technology.dubaileading.maccessemployee.utils.AppShared


class HomeActivity : BaseActivity<ActivityHomeBinding,HomeViewModel>() {

    lateinit var navView : BottomNavigationView
    lateinit var im : ImageView
    private lateinit var count : BadgeDrawable

    private val broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.getNotificationsCount(this@HomeActivity)
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        backGroundColor()
        super.onCreate(savedInstanceState)
        loadFragment(HomeFragment())

        val intentFilter = IntentFilter("IntentFilterAction")
        LocalBroadcastManager.getInstance(this@HomeActivity)
            .registerReceiver(broadcastReceiver, intentFilter)

        navView = findViewById(R.id.bnv_home)
        navView.selectedItemId = R.id.bnm_home

        binding.imgORDR.setOnClickListener{
            navView.selectedItemId = R.id.bnm_home
        }

        count = navView.getOrCreateBadge(R.id.bnm_notification)
        count.backgroundColor = Color.RED
        count.badgeTextColor = Color.WHITE

        viewModel.getNotificationsCount(this)

        viewModel.notificationCountSuccess.observe(this){

            if (it.data !=0){
                count.number = it.data!!
                count.isVisible = true
            } else{
                count.isVisible = false
            }
        }

        viewModel.error.observe(this){
            count.isVisible = false
            if (it.status.equals("notok") && it.message.equals("TOKEN_EXPIRED")){
                AppShared(this@HomeActivity).clearAll()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finishAffinity()
            }

        }


        navView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.title){
                "Services" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)
                    loadFragment(ServicesFragment())
                    binding
                }
                "Requests" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)
                    loadFragment(RequestsFragment())
                }
                "Home" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab)
                    loadFragment(HomeFragment())
                }
                "Notifications" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)
                    loadFragment(NotificationsFragment())
                    count.isVisible = false
                }
                "Profile" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)
                    loadFragment(ProfileFragment())
                }
            }

            item.itemId
            true
        })

    }

    override fun createViewModel(): HomeViewModel {
        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container,
                    fragment
                )
                //.addToBackStack(fragment.tag)
                .commit()
            return true
        }
        return false
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        var id = navView.selectedItemId
        if(id == R.id.bnm_home){
            super.onBackPressed()
            return
        }

        loadFragment(HomeFragment())
        navView.selectedItemId = R.id.bnm_home

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this@HomeActivity).unregisterReceiver(broadcastReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.statusbar_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
    }


}