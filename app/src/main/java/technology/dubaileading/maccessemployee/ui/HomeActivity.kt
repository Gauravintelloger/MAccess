package technology.dubaileading.maccessemployee.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.base.BaseViewModel
import technology.dubaileading.maccessemployee.databinding.ActivityHomeBinding
import technology.dubaileading.maccessemployee.ui.coming_soon.ComingSoonFragment
import technology.dubaileading.maccessemployee.ui.home_fragment.HomeFragment
import technology.dubaileading.maccessemployee.ui.login.LoginViewModel
import technology.dubaileading.maccessemployee.ui.notifications.NotificationsFragment
import technology.dubaileading.maccessemployee.ui.profile.ProfileFragment
import technology.dubaileading.maccessemployee.ui.requests.RequestsFragment


class HomeActivity : BaseActivity<ActivityHomeBinding,BaseViewModel>() {

    lateinit var navView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        backGroundColor()
        super.onCreate(savedInstanceState)
        loadFragment(HomeFragment())

        navView = findViewById(R.id.bnv_home)
        navView.selectedItemId = R.id.bnm_home

        binding.imgORDR.setOnClickListener{
            navView.selectedItemId = R.id.bnm_home
        }



        navView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.title){
                "Services" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)

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

    override fun createViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
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
                .addToBackStack(fragment.tag)
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun backGroundColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.setBackgroundDrawableResource(R.drawable.statusbar_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
    }


}