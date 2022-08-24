package technology.dubaileading.maccessemployee.ui.personal_info

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityPersonalInfoBinding
import java.util.*


class PersonalInfoActivity : BaseActivity<ActivityPersonalInfoBinding, PersonalInfoViewModel>(){
    private var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        viewModel.getProfile(this@PersonalInfoActivity)
        viewModel.profileData.observe(this){
            if (it?.profileData?.name != null){
                binding?.name?.setText(it?.profileData?.name.toString())
            }

            if (it?.profileData?.email != null){
                binding?.email?.setText(it?.profileData?.email.toString())
            }

            if (it?.profileData?.dateOfBirth != null){
                binding?.dob?.setText(it?.profileData?.dateOfBirth.toString())
            }

            if (it?.profileData?.personalContactNumber != null){
                binding?.number?.setText(it?.profileData.personalContactNumber.toString())
            }

        }
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }


        binding?.dob?.setOnClickListener {
            it.hideKeyboard()
            DatePickerDialog(this@PersonalInfoActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding?.materialToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun createViewModel(): PersonalInfoViewModel {
        return ViewModelProvider(this).get(PersonalInfoViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivityPersonalInfoBinding {
        return ActivityPersonalInfoBinding.inflate(layoutInflater!!)
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