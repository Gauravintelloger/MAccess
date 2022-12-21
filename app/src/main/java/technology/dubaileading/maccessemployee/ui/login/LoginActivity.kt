package technology.dubaileading.maccessemployee.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.ActivityLoginBinding
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.LoginRequest
import technology.dubaileading.maccessemployee.rest.entity.LoginResponse
import technology.dubaileading.maccessemployee.rest.entity.TokenRequest
import technology.dubaileading.maccessemployee.ui.forgot_password.ForgotPasswordActivity
import technology.dubaileading.maccessemployee.ui.splash.SplashOrganisationActivity
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.CustomDialog
import technology.dubaileading.maccessemployee.utils.Utils


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var viewBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(this)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewBinding.submitMaterialButton.setOnClickListener {
            val loginRequest = LoginRequest(
                device_token = getUniqueID(this@LoginActivity),
                password = Utils.md5(viewBinding.passwordTextInputEditText.text.toString().trim()),
                username = viewBinding.userNameTextInputEditText.text.toString()
            )

            viewModel.userLogin(loginRequest)
            viewModel.userDetails.observe(this, loginObserver)

        }


        viewModel.statusMessage.observe(this@LoginActivity) { it ->
            it.getContentIfNotHandled()?.let {
                if (it.contains("Username", true)) {
                    viewBinding.userNameTextInputLayout.error = it
                    if (viewBinding.passwordTextInputLayout.isErrorEnabled) {
                        viewBinding.passwordTextInputLayout.isErrorEnabled = false
                    }
                } else if (it.contains("Password", true)) {
                    viewBinding.passwordTextInputLayout.error = it
                    if (viewBinding.userNameTextInputLayout.isErrorEnabled) {
                        viewBinding.userNameTextInputLayout.isErrorEnabled = false
                    }

                } else {
                    viewBinding.userNameTextInputLayout.isErrorEnabled = false
                    viewBinding.passwordTextInputLayout.isErrorEnabled = false

                }

            }
        }

        viewBinding.forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

    }

    private var loginObserver: Observer<DataState<LoginResponse>> =
        androidx.lifecycle.Observer<DataState<LoginResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateLoginResponse(it.item)
                }
                is DataState.Error -> {
                    dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                }
            }
        }

    private var saveFcmTokenObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateSaveFcmTokenResponse(it.item)
                }
                is DataState.Error -> {
                    dismissProgress()
                    showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    dismissProgress()
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                logoutUser()
                            }
                        })
                }
            }
        }
    fun logoutUser() {
        SessionManager.deleteAllUserInfo()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
        showToast("Logged out Successfully")
    }
    private fun validateLoginResponse(response: LoginResponse) {

        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            SessionManager.user = response.data
            SessionManager.token = response.token

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    CustomDialog(this).showInformationDialog("Unable to get FCM token")
                }else{
                    viewModel.saveFcmToken(TokenRequest(task.result, "android"))
                    viewModel.saveToken.observe(this, saveFcmTokenObserver)
                }
            })

        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }

    private fun validateSaveFcmTokenResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            SessionManager.isLoggedIn = true
            startActivity(Intent(this@LoginActivity, SplashOrganisationActivity::class.java))
            finish()
        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }

    }

    override fun onStart() {
        super.onStart()
        viewBinding.userNameTextInputEditText.showKeyboard()

    }
}