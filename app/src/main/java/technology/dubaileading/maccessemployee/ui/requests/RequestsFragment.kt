package technology.dubaileading.maccessemployee.ui.requests

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentRequestsBinding
import technology.dubaileading.maccessemployee.rest.entity.LeaveTypeData
import java.text.SimpleDateFormat
import java.util.*


class RequestsFragment  : BaseFragment<FragmentRequestsBinding, RequestsViewModel>() {
    private var cal = Calendar.getInstance()
    var list = ArrayList<LeaveTypeData>()
    private var fromDate : String = ""
    private var toDate : String = ""
    private var IS_FROM_DATE = false

    override fun createViewModel(): RequestsViewModel {
        return ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentRequestsBinding {
        return FragmentRequestsBinding.inflate(layoutInflater!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.newRequest?.setOnClickListener {
            newRequest()
        }

    }

    private fun newRequest() {
        val btnsheet = layoutInflater.inflate(R.layout.new_request_layout, null)
        var newRequestDialog = BottomSheetDialog(requireContext())
        val requestLeave = btnsheet.findViewById<LinearLayout>(R.id.request_leave)
        val requestDoc = btnsheet.findViewById<LinearLayout>(R.id.request_doc)

        requestLeave.setOnClickListener {
            newRequestDialog.dismiss()
            newLeaveRequest()
        }

        requestDoc.setOnClickListener {
            newRequestDialog.dismiss()
            newDocRequest()
        }
        newRequestDialog.setContentView(btnsheet)
        newRequestDialog.show()
    }

    private fun newDocRequest() {
        val btnsheet = layoutInflater.inflate(R.layout.document_request, null)
        var newDocDialog = BottomSheetDialog(requireContext())


        newDocDialog.setContentView(btnsheet)
        newDocDialog.show()

    }

    private fun newLeaveRequest() {
        val btnsheet = layoutInflater.inflate(R.layout.leave_request, null)
        var newLeaveDialog = BottomSheetDialog(requireContext())
        val leaveType = btnsheet.findViewById<AppCompatSpinner>(R.id.leaveType)
        val desc = btnsheet.findViewById<EditText>(R.id.desc)
        val startDate = btnsheet.findViewById<EditText>(R.id.startDate)
        val endDate = btnsheet.findViewById<EditText>(R.id.endDate)
        val leaveBal = btnsheet.findViewById<TextView>(R.id.leaveBal)


        leaveBal.text = list[0].shortCode + "Left:" + list[0].balanceLeaves + " of " +list[0].noOfLeaves
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                if (IS_FROM_DATE){
                    fromDate = sdf.format(cal.time)
                    startDate.setText(fromDate)
                }else{
                    toDate = sdf.format(cal.time)
                    endDate.setText(toDate)

                }
            }

        startDate.setOnClickListener {
            it.hideKeyboard()
            IS_FROM_DATE = true
            DatePickerDialog(
                requireActivity(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        endDate.setOnClickListener {
            it.hideKeyboard()
            IS_FROM_DATE = false
            DatePickerDialog(
                requireActivity(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }




        var typeList = ArrayList<String>()
        for (i in list.indices){
            typeList.add(list[i].title.toString())
        }
        val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, typeList)
        leaveType.adapter = arrayAdapter

        leaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val short_code = list[i].shortCode
                val no_of_leaves = list[i].noOfLeaves
                val balance_leaves = list[i].balanceLeaves
                leaveBal.text = "$short_code Left:$balance_leaves of $no_of_leaves"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }




        newLeaveDialog.setContentView(btnsheet)
        newLeaveDialog.show()

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.materialToolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding?.requestsRv?.itemAnimator = DefaultItemAnimator()
        binding?.requestsRv?.layoutManager = LinearLayoutManager(activity)
        binding?.requestsRv?.adapter = RequestsAdapter()

        viewModel?.getLeaveTypes(requireContext())

        viewModel?.leaveTypesData?.observe(viewLifecycleOwner){
            list = it.data as ArrayList<LeaveTypeData>
        }

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}
