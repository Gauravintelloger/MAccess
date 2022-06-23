package technology.dubaileading.maccessuser.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessuser.base.BaseActivity
import technology.dubaileading.maccessuser.databinding.ActivityLoginBinding
import technology.dubaileading.maccessuser.ui.HomeActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.submitBt.setOnClickListener{
            startActivity(Intent(applicationContext,HomeActivity::class.java))
        }
    }

    override fun createViewModel(): LoginViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}