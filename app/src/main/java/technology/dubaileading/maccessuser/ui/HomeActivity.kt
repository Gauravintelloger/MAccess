package technology.dubaileading.maccessuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessuser.R
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.base.BaseViewModel
import technology.dubaileading.maccessuser.databinding.ActivityHomeBinding
import technology.dubaileading.maccessuser.databinding.ActivityLoginBinding
import technology.dubaileading.maccessuser.ui.home_fragment.HomeFragment
import technology.dubaileading.maccessuser.ui.login.LoginViewModel

class HomeActivity : BaseActivity<ActivityHomeBinding,BaseViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFragment(HomeFragment())
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
                ) //                    .addToBackStack(fragment.getTag())
                .commit()
            return true
        }
        return false
    }

}