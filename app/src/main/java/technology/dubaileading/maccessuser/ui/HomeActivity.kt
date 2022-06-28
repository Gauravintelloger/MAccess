package technology.dubaileading.maccessuser.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import technology.dubaileading.maccessuser.R
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.base.BaseViewModel
import technology.dubaileading.maccessuser.databinding.ActivityHomeBinding
import technology.dubaileading.maccessuser.ui.coming_soon.ComingSoonFragment
import technology.dubaileading.maccessuser.ui.dialog.ComingSoonDialog
import technology.dubaileading.maccessuser.ui.home_fragment.HomeFragment
import technology.dubaileading.maccessuser.ui.login.LoginViewModel


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
                    loadFragment(ComingSoonFragment())
                    binding
                }
                "Requests" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)
                    loadFragment(ComingSoonFragment())
                }
                "Home" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab)
                    loadFragment(HomeFragment())
                }
                "Notifications" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)
                    loadFragment(ComingSoonFragment())
                }
                "Profile" -> {
                    binding.imgORDR.background = this.getDrawable(R.drawable.hom_fab_disable)
                    loadFragment(ComingSoonFragment())
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
//                .addToBackStack(fragment.getTag())
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