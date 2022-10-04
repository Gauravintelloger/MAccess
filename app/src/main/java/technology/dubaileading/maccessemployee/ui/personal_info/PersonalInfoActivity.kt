package technology.dubaileading.maccessemployee.ui.personal_info

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.databinding.ActivityPersonalInfoBinding
import technology.dubaileading.maccessemployee.rest.entity.UpdateProfile
import java.text.SimpleDateFormat
import java.util.*


class PersonalInfoActivity : BaseActivity<ActivityPersonalInfoBinding, PersonalInfoViewModel>(){
    private var cal = Calendar.getInstance()

    private val dateFormat = "dd-MM-yyyy"
    private lateinit var dateOfBith : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backGroundColor()
        viewModel.getProfile(this@PersonalInfoActivity)
        viewModel.profileData.observe(this){
            if (it?.profileData?.name != null){
                binding?.name?.setText(it?.profileData?.name.toString())
            }

            if (it?.profileData?.dateOfBirth != null){
                binding?.dob?.setText(it?.profileData?.dateOfBirth.toString())
            }

            if (it?.profileData?.personalContactNumber != null){
                binding?.number?.setText(it?.profileData.personalContactNumber.toString())
            }

        }

        viewModel.updateProfileSuccess.observe(this){
            if (it?.profileData?.name != null){
                binding?.name?.setText(it?.profileData?.name.toString())
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
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                dateOfBith = sdf.format(cal.time)
                binding?.dob?.setText(dateOfBith)
            }
        var datePicker = DatePickerDialog(
            this@PersonalInfoActivity,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )


        binding?.dob?.setOnClickListener {
            it.hideKeyboard()
            datePicker.show()
        }


        binding?.save?.setOnClickListener {
            val name = binding?.name?.text?.toString()?.trim()!!
            val number = binding?.number?.text?.toString()?.trim()!!
            val date = binding?.dob?.text?.toString()?.trim()!!

            if (name != null && name.isEmpty()) {
                Toast.makeText(this@PersonalInfoActivity, "Enter Name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (number != null && number.isEmpty()) {
                Toast.makeText(this@PersonalInfoActivity, "Enter Mobile Number", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (date != null && date.isEmpty()) {
                Toast.makeText(this@PersonalInfoActivity, "Enter Date of birth", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var updateProfile = UpdateProfile(name,date,number)
            viewModel?.updateProfile(this@PersonalInfoActivity,updateProfile)

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