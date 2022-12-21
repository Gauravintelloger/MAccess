package technology.dubaileading.maccessemployee.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.ActivityResetPasswordBinding
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.CustomDialog

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private val viewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var viewBinding: ActivityResetPasswordBinding
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent(false)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        viewBinding.viewModel = viewModel

        email = intent.getStringExtra("email")!!

        viewModel.statusMessage.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        viewBinding.submitMaterialButton.setOnClickListener {
            viewModel.resetPassword(email)
            viewModel.resetPassword.observe(this, resetPasswordObserver)
        }

        viewBinding.backImageView.setOnClickListener {
            viewBinding.newPasswordEditText.hideKeyboard()
            finish()
        }

        viewBinding.newPasswordEditText.showKeyboard()

    }

    private var resetPasswordObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    showProgress()
                }
                is DataState.Success -> {
                    dismissProgress()
                    validateResetPasswordResponse(it.item)
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
                                finishAffinity()
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                            }
                        })
                }
            }
        }

    private fun validateResetPasswordResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            showToast(response.message)
            finishAffinity()
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            CustomDialog(this).showInformationDialog(response.message)
        }
    }

}