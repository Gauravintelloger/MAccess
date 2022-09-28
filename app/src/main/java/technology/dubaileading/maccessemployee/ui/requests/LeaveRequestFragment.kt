package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentLeaveRequestBinding
import technology.dubaileading.maccessemployee.rest.entity.*

import technology.dubaileading.maccessemployee.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class LeaveRequestFragment : BaseFragment<FragmentLeaveRequestBinding, RequestsViewModel>(),leaveClickListener {
    private lateinit var leaveRequestsAdapter: LeaveRequestsAdapter
    private var leaveRequests = ArrayList<LeaveRequestsItem>()
    private var leaveTypeList = java.util.ArrayList<LeaveTypeData>()
    private var fromDate: String = ""
    private var toDate: String = ""
    private val dateFormat = "dd-MM-yyyy"

    private lateinit var updateLeaveDialog : BottomSheetDialog

    override fun createViewModel(): RequestsViewModel {
        return ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentLeaveRequestBinding {
        return FragmentLeaveRequestBinding.inflate(layoutInflater!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        leaveRequestsAdapter = LeaveRequestsAdapter(requireContext(),this)
        leaveRequests = arguments?.getParcelableArrayList<LeaveRequestsItem>(Constants.LEAVE_REQUEST)!!
        leaveRequestsAdapter.setData(leaveRequests)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.leaveRv?.itemAnimator = DefaultItemAnimator()
        binding?.leaveRv?.layoutManager = LinearLayoutManager(activity)
        binding?.leaveRv?.adapter = leaveRequestsAdapter

        viewModel?.getLeaveTypes(requireContext())

        viewModel?.leaveTypesData?.observe(viewLifecycleOwner) {
            leaveTypeList = it.data as java.util.ArrayList<LeaveTypeData>
        }

        viewModel?.applyLeaveSuccess?.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            updateLeaveDialog.dismiss()

        }

        viewModel?.applyLeaveError?.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }




    }

    override fun updateLeaveRequest(leaveRequestsItem: LeaveRequestsItem) {
        showUpdateDailog(leaveRequestsItem)
    }

    private fun showUpdateDailog(leaveRequestsItem: LeaveRequestsItem) {
        val btnsheet = layoutInflater.inflate(R.layout.leave_request, null)
        updateLeaveDialog = BottomSheetDialog(requireContext())

        val leaveType = btnsheet.findViewById<AppCompatSpinner>(R.id.leaveType)
        val desc = btnsheet.findViewById<EditText>(R.id.desc)
        val startDate = btnsheet.findViewById<EditText>(R.id.startDate)
        val endDate = btnsheet.findViewById<EditText>(R.id.endDate)
        val leaveBal = btnsheet.findViewById<TextView>(R.id.leaveBal)
        val submitBt = btnsheet.findViewById<AppCompatButton>(R.id.submitBt)
        var typeList = java.util.ArrayList<String>()
        if (leaveTypeList != null){
            leaveBal.text = leaveTypeList[0].shortCode + "Left:" + leaveTypeList[0].balanceLeaves + " of " + leaveTypeList[0].noOfLeaves

            for (i in leaveTypeList.indices) {
                typeList.add(leaveTypeList[i].title.toString())
            }

        }
        var leave_type_id = leaveTypeList[0]?.id

        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, typeList)
        leaveType.adapter = arrayAdapter

        leaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val short_code = leaveTypeList[i].shortCode
                val no_of_leaves = leaveTypeList[i].noOfLeaves
                val balance_leaves = leaveTypeList[i].balanceLeaves
                leave_type_id = leaveTypeList[i].id
                leaveBal.text = "$short_code Left:$balance_leaves of $no_of_leaves"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }




        startDate.setOnClickListener {
            it.hideKeyboard()
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                fromDate = sdf.format(datePicker.selection?.first)
                startDate.setText(fromDate)
                toDate =  sdf.format(datePicker.selection?.second)
                endDate.setText(toDate)

                val startDate: Long? = datePicker.selection?.first
                val endDate: Long? = datePicker.selection?.second

                val msDiff = endDate!!.minus(startDate!!)
                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)

                val days = daysDiff + 1;

                submitBt.text = "Submit($days day)"
            }
            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {

            }
            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {

            }
        }

        endDate.setOnClickListener {
            it.hideKeyboard()
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                fromDate = sdf.format(datePicker.selection?.first)
                startDate.setText(fromDate)
                toDate =  sdf.format(datePicker.selection?.second)
                endDate.setText(toDate)


                val startDate: Long? = datePicker.selection?.first
                val endDate: Long? = datePicker.selection?.second

                val msDiff = endDate!!.minus(startDate!!)
                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)

                val days = daysDiff + 1;

                submitBt.text = "Submit($days day)"


            }
            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {

            }
            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {

            }
        }

        submitBt.setOnClickListener {
            if (leave_type_id!!.equals(null)) {
                Toast.makeText(requireContext(), "Select Leave Type", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val description = desc?.text?.toString()?.trim()
            if (description!!.isEmpty()) {
                Toast.makeText(requireContext(), "Add Description", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (fromDate.isEmpty()) {
                Toast.makeText(requireContext(), "Start date is not selected", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (toDate.isEmpty()) {
                Toast.makeText(requireContext(), "End date is not selected", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            var updateLeave = UpdateLeave(leaveRequestsItem.id,leave_type_id, description, fromDate, toDate)

            viewModel?.updateLeave(requireContext(), updateLeave)


        }

        updateLeaveDialog.setContentView(btnsheet)
        updateLeaveDialog.show()

    }

    override fun onClick(id: Int) {
        android.app.AlertDialog.Builder(activity)
            .setTitle("Alert")
            .setMessage("Do you want to delete the request?")
            .setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                var deleteReq = DeleteReq(id)
                viewModel?.deleteLeaveRequest(requireContext(),deleteReq)
                dialog.dismiss()
            }
            .setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}