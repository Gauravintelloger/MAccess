package technology.dubaileading.maccessemployee.ui.requests

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.braver.tool.picker.BraverDocPathUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.leave_request.*
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.FragmentRequestsBinding
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.rest.entity.applycreditrequest.Applycreditresponsemodel
import technology.dubaileading.maccessemployee.rest.entity.applycreditrequest.Newcreditrequestmodel
import technology.dubaileading.maccessemployee.rest.entity.employeecreditbalance.Employeecreditbalancemodel
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.attendance.AttendanceReportAdapter
import technology.dubaileading.maccessemployee.ui.profile.ProfileViewModel
import technology.dubaileading.maccessemployee.ui.timecreaditrequest.TimecreditViewmodel
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.AppUtils
import technology.dubaileading.maccessemployee.utils.CustomDialog
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RequestsFragment : Fragment() {
    private var calendar = Calendar.getInstance()
    private var attachmentFileTextView: TextView? = null
    private var leaveAttachmentFileTextView: TextView? = null
    private lateinit var documentRequestBottomSheetDialog: BottomSheetDialog
    private lateinit var leaveBottomSheetDialog: BottomSheetDialog
    private lateinit var creditBottomSheetDialog: BottomSheetDialog
    private val dateFormat = "dd-MM-yyyy"
    private val viewModel by viewModels<RequestsViewModel>()
    private lateinit var viewBinding: FragmentRequestsBinding
    lateinit var requestFragmentAdapter: RequestFragmentAdapter
    private val profileviewModel by viewModels<ProfileViewModel>()

    var isAllFieldsChecked = false
    private val timecreditViewmodel: TimecreditViewmodel by viewModels()

    private lateinit var attendanceReportAdapter: AttendanceReportAdapter
    var categoryarray: ArrayList<Int>? = null
    var categoryspinner:Int=5

    var timedurationvalue:String="0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setStatusBarTranslucent(true)
        viewBinding = FragmentRequestsBinding.inflate(inflater, container, false)
        setupListeners()
        setUpObservers()

//Log.e("requestmodule",SessionManager.requestmoduleaccess.toString())
        return viewBinding.root
    }

    private var employeecreditbalanceleavesObserver: Observer<DataState<Employeecreditbalancemodel>> =
        androidx.lifecycle.Observer<DataState<Employeecreditbalancemodel>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    try {
                        Log.e("timeduration",it.item.data.balance.toString())
                        if (it.item.data.balance.toString().isNullOrEmpty())
                        {
                            timedurationvalue="0"+"/360"
                        }
                        else{
                            timedurationvalue=it.item.data.balance.toString()+"/360"
                        }


                    }
                    catch (ex:Exception)
                    {
                        timedurationvalue="0"

                    }
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                }
            }
        }

    private fun setupListeners() {
        viewBinding.newRequestTextView.setOnClickListener {
            newRequest()
        }
    }

    private fun setUpObservers() {
        viewModel.statusMessage.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
                requireContext().showToast(it)
            }
        }
//        profileviewModel.employeecreditbalance()
//        profileviewModel.employeecreditbalance.observe(viewLifecycleOwner, employeecreditbalanceleavesObserver)

    }

    private fun newRequest() {
        val view = layoutInflater.inflate(R.layout.new_request_layout, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val requestLeaveLinearLayout: LinearLayout =
            view.findViewById(R.id.requestLeaveLinearLayout)
        val requestDocumentsLinearLayout: LinearLayout =
            view.findViewById(R.id.requestDocumentsLinearLayout)

        val CreditRequestapply: LinearLayout = view.findViewById(R.id.CreditRequestapply)
        if (SessionManager.username.toString()=="24" || SessionManager.username.toString()=="19")
        {
            CreditRequestapply.visibility=View.VISIBLE
        }
        else
        {
            CreditRequestapply.visibility=View.GONE
        }

        requestLeaveLinearLayout.setOnClickListener {
            bottomSheetDialog.dismiss()
            if(SessionManager.requestmoduleaccess.toString()=="1")
            {
                newLeaveRequest()
            }
            else
            {
                requireContext().showToast("New request is not allowed, please contact to administration.")

            }


        }


        CreditRequestapply.setOnClickListener {
//            startActivity(Intent(requireContext(), Timecreditapply::class.java))
//            requireActivity().finish()
            bottomSheetDialog.dismiss()

            newcreditRequest()

        }

        requestDocumentsLinearLayout.setOnClickListener {
            bottomSheetDialog.dismiss()
            if(SessionManager.requestmoduleaccess.toString()=="1")
            {
             newDocRequest()
            }
            else
            {
                requireContext().showToast("New request is not allowed, please contact to administration.")
            }
//
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun newDocRequest() {
        val view = layoutInflater.inflate(R.layout.document_request, null)
        documentRequestBottomSheetDialog = BottomSheetDialog(requireContext())

        val documentTypesSpinner = view.findViewById<DynamicWidthSpinner>(R.id.documentTypesSpinner)
        val descriptionTextView = view.findViewById<EditText>(R.id.descriptionTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val emailAddressTextView = view.findViewById<EditText>(R.id.emailAddressTextView)
        val attachCardView = view.findViewById<MaterialCardView>(R.id.attachCardView)
        val remove = view.findViewById<ImageView>(R.id.remove)
        attachmentFileTextView = view.findViewById(R.id.attachmentFileTextView)
        val submitMaterialButton = view.findViewById<MaterialButton>(R.id.submitMaterialButton)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateTextView.text = SimpleDateFormat(dateFormat, Locale.US).format(calendar.time)
            }
        val datePicker = DatePickerDialog(
            requireActivity(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = calendar.timeInMillis

        dateTextView.setOnClickListener {
            datePicker.show()
        }

        SessionManager.init(requireContext())
        SessionManager.user?.let { emailAddressTextView.setText(it.username) }

        attachCardView.setOnClickListener {
            checkPermissionAndOpenPicker()
        }

        remove.setOnClickListener {
            viewModel.clearAttachment()
        }

        submitMaterialButton.setOnClickListener {
            viewModel.applyLeave(
                ApplyLeave(
                    viewModel.selectedLeaveType?.value?.id,
                    descriptionTextView.text.toString(),
                    startDateTextView.text.toString(),
                    endDateTextView.text.toString()
                )
            )
            viewModel.applyLeave.observe(viewLifecycleOwner, applyLeaveObserver)
        }

        submitMaterialButton.setOnClickListener {
            val documentRequest =
                DocumentRequest(
                    "Document Request",
                    descriptionTextView?.text?.toString()?.trim(),
                    "",
                    viewModel.selectedDocumentRequestType?.value?.id,
                    dateTextView.text.toString(),
                    emailAddressTextView?.text?.toString()?.trim()
                )
            viewModel.requestDocument(documentRequest)
            viewModel.requestDocument.observe(viewLifecycleOwner, requestDocumentObserver)

        }

        loadAllDocumentRequestTypes(documentTypesSpinner)


        documentRequestBottomSheetDialog.setContentView(view)
        documentRequestBottomSheetDialog.show()

    }

    private fun checkPermissionAndOpenPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        } else {
            openAttachments()
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showPermissionNeededAlert()
        } else {
            openAttachments()
        }
    }

    private fun showPermissionNeededAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.needPermissions))
        builder.setMessage(getString(R.string.somePermissionsAreRequiredToDoTheTask))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            permissionResult.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
        builder.setNeutralButton(getString(R.string.cancel), null)
        val dialog = builder.create()
        dialog.show()
    }

    private var permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                println("it value = ${it.value}")
                if (it.value) {
                    openAttachments()
                }
            }
        }



    /**
     * Handling Get doc,pdf,xlx,apk,et., from user to send throw Attachments <br></br>
     */
    private fun openAttachments() {
        try {
            val addAttachment = Intent(Intent.ACTION_GET_CONTENT)
            addAttachment.type = "*/*"
            addAttachment.action = Intent.ACTION_GET_CONTENT
            addAttachment.action = Intent.ACTION_OPEN_DOCUMENT
            activityResultLauncherForDocs.launch(addAttachment)
        } catch (exception: Exception) {
            AppUtils.printLogConsole("openAttachments", "Exception-------->" + exception.message)
        }
    }

    private var activityResultLauncherForDocs =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                try {
                    val selectedDocUri = result.data!!.data
                    if (selectedDocUri.toString().contains("video") || selectedDocUri.toString()
                            .contains("mp4") || selectedDocUri.toString()
                            .contains("mkv") || selectedDocUri.toString().contains("mov")
                    ) {

                        setSourcePathView(selectedDocUri.toString())
                    } else {
                        val tempFileAbsolutePath = BraverDocPathUtils.Companion.getSourceDocPath(
                            requireContext(),
                            selectedDocUri!!
                        )
                        setSourcePathView(tempFileAbsolutePath!!)
                    }
                } catch (e: java.lang.Exception) {
                    AppUtils.printLogConsole(
                        "activityResultLauncherForDocs",
                        "Exception-------->" + e.message
                    )
                }
            }
        }

    private fun setSourcePathView(path: String) {
        viewModel.selectedFileLiveData?.value = path
    }


    @SuppressLint("SetTextI18n")
    private fun newcreditRequest() {
        val view = layoutInflater.inflate(R.layout.fragment_make_credit_request, null)
        creditBottomSheetDialog = BottomSheetDialog(requireContext())

        val category = view.findViewById<DynamicWidthSpinner>(R.id.categoryspinner)
        //val timeduration = view.findViewById<TextView>(R.id.timeduration)
        val selectdate = view.findViewById<TextView>(R.id.selectdate)
        val categoryerror = view.findViewById<TextView>(R.id.categoryerror)
        val reason = view.findViewById<TextView>(R.id.reason)
        val submitMaterialButton = view.findViewById<MaterialButton>(R.id.submitMaterialButton)

//        timeduration.text=timedurationvalue+"/360"


        selectdate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                selectdate.text = sdf.format(datePicker.selection)

            }

        }

        viewModel.statusMessage.observe(requireActivity()) { it ->
            it.getContentIfNotHandled()?.let {
                requireActivity().showToast(it)
            }
        }

        categoryarray= ArrayList()

        for (i in 5..60 step 5)
        {
            categoryarray!!.add((i))
        }

        val adapter = ArrayAdapter(requireActivity(),
            android.R.layout.simple_spinner_item, categoryarray!!)
        category!!.adapter = adapter


        category.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                Log.e("timeee",adapter.getItem(position).toString())
                categoryspinner= adapter.getItem(position)!!
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }


        submitMaterialButton.setOnClickListener {
            categoryerror.visibility=View.GONE

            if (selectdate.length()==0)
            {
                selectdate.setError("This field is required");

            }
            else if (reason.length()==0)
            {
                reason.setError("This field is required");


            }

            else
            {
                val newcreditrequestmodel= Newcreditrequestmodel(
                    usageType = "early_going",
                    reason = reason.text?.trim().toString(),
                    creditsRequiredInMins = categoryspinner,
                    date = selectdate.text.trim().toString()

                )

                timecreditViewmodel.addnewrequest(newcreditrequestmodel)
                timecreditViewmodel.addnew.observe(requireActivity(), postsObserverapplyjob)


            }


        }





        creditBottomSheetDialog.setContentView(view)
        creditBottomSheetDialog.show()

    }



    private var postsObserverapplyjob: Observer<DataState<Applycreditresponsemodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {


                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()

                    Log.e("applyjob",it.item.message)
//                    validatePostsData(it.item)

                    Toast.makeText(requireContext(),it.item.message,Toast.LENGTH_LONG).show()


                    creditBottomSheetDialog.dismiss()
                }
                is DataState.Error -> {
                    Toast.makeText(requireContext(),it.error.toString(),Toast.LENGTH_LONG).show()


                    requireContext().dismissProgress()
                }
                is DataState.TokenExpired -> {

                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }







    @SuppressLint("SetTextI18n")
    private fun newLeaveRequest() {
        val view = layoutInflater.inflate(R.layout.leave_request, null)
        leaveBottomSheetDialog = BottomSheetDialog(requireContext())

        val leaveTypesSpinner = view.findViewById<DynamicWidthSpinner>(R.id.leaveTypesSpinner)
        val descriptionTextView = view.findViewById<EditText>(R.id.descriptionTextView)
        val startDateTextView = view.findViewById<TextView>(R.id.startDateTextView)
        val endDateTextView = view.findViewById<TextView>(R.id.endDateTextView)
        val leaveBalanceTextView = view.findViewById<TextView>(R.id.leaveBalanceTextView)
        val leavefrom = view.findViewById<TextView>(R.id.leavefrom)
        val leaveto = view.findViewById<TextView>(R.id.leaveto)
        val openingleavebalence = view.findViewById<TextView>(R.id.openingleavebalence)
        val attachCardView = view.findViewById<MaterialCardView>(R.id.attachCardView)
        val remove = view.findViewById<ImageView>(R.id.remove)
        leaveAttachmentFileTextView = view.findViewById(R.id.attachmentFileTextView)
        val submitMaterialButton = view.findViewById<MaterialButton>(R.id.submitMaterialButton)

        startDateTextView.setOnClickListener {
            startDateTextView.hideKeyboard()
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                startDateTextView.text = sdf.format(datePicker.selection?.first)
                endDateTextView.text = sdf.format(datePicker.selection?.second)

                val startDate: Long? = datePicker.selection?.first
                val endDate: Long? = datePicker.selection?.second

                val msDiff = endDate!!.minus(startDate!!)
                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)

                val days = daysDiff + 1

                submitMaterialButton.text = "Submit($days day)"

            }
        }

        endDateTextView.setOnClickListener {
            endDateTextView?.hideKeyboard()
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                startDateTextView.text = sdf.format(datePicker.selection?.first)
                endDateTextView.text = sdf.format(datePicker.selection?.second)

                val startDate: Long? = datePicker.selection?.first
                val endDate: Long? = datePicker.selection?.second

                val msDiff = endDate!!.minus(startDate!!)
                val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)

                val days = daysDiff + 1

                submitMaterialButton.text = "Submit($days day)"
            }
        }

        attachCardView.setOnClickListener {
            checkPermissionAndOpenPicker()
        }

        remove.setOnClickListener {
            viewModel.clearAttachment()
        }

        submitMaterialButton.setOnClickListener {
            viewModel.applyLeave(
                ApplyLeave(
                    viewModel.selectedLeaveType?.value?.id,
                    descriptionTextView.text.toString(),
                    startDateTextView.text.toString(),
                    endDateTextView.text.toString()
                )
            )
            viewModel.applyLeave.observe(viewLifecycleOwner, applyLeaveObserver)
        }

        loadAllLeaveTypes(leaveTypesSpinner, leaveBalanceTextView,leavefrom,leaveto)

        leaveBottomSheetDialog.setContentView(view)
        leaveBottomSheetDialog.show()

    }

    private var applyLeaveObserver: Observer<DataState<ApiResponse2>> =
        androidx.lifecycle.Observer<DataState<ApiResponse2>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateApplyLeaveResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private var requestDocumentObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateDocumentRequestResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    requireContext().dismissProgress()
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateApplyLeaveResponse(response: ApiResponse2) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            requireContext().showToast(response.message)
            if (leaveBottomSheetDialog.isShowing) {
                leaveBottomSheetDialog.dismiss()
            }
            if (viewPager.currentItem == 0){
                (requestFragmentAdapter.fragmentList[0] as? LeaveRequestFragment)?.loadAllLeaveRequestsFromRemote()
            }

        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }

    private fun validateDocumentRequestResponse(response: ApiResponse) {
        if (response.status == Constants.API_RESPONSE_CODE.OK) {
            requireContext().showToast(response.message)
            if (documentRequestBottomSheetDialog.isShowing) {
                documentRequestBottomSheetDialog.dismiss()
            }
            if (viewPager.currentItem == 1){
                (requestFragmentAdapter.fragmentList[1] as? DocumentRequestFragment)?.loadAllDocumentRequestsFromRemote()
            }
            if (viewPager.currentItem==2)
            {
                (requestFragmentAdapter.fragmentList[2] as? Creditrequestfragment)?.getAttendanceReportFromRemote()

            }

        } else {
            CustomDialog(requireActivity()).showInformationDialog(response.message)
        }

    }

    private fun loadAllLeaveTypes(
        leaveTypesSpinner: DynamicWidthSpinner,
        leaveBalanceTextView: TextView,leavefrom:TextView,
        leaveto:TextView

    ) {
        viewModel.getLeaveTypes()
        viewModel.leaveTypes.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    validateLeaveTypesData(it.item, leaveTypesSpinner, leaveBalanceTextView,leavefrom,leaveto)
                }
                is DataState.Error -> {
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }
    }

    private fun loadAllDocumentRequestTypes(
        spinner: DynamicWidthSpinner
    ) {
        viewModel.getRequestTypes()
        viewModel.documentRequestTypes.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    validateDocumentRequestTypesData(it.item, spinner)
                }
                is DataState.Error -> {
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }
    }

    private fun validateDocumentRequestTypesData(
        response: RequestType,
        spinner: DynamicWidthSpinner
    ) {
        if (activity != null && isAdded) {
            if (response.status == Constants.API_RESPONSE_CODE.OK) {
                if (response.requestTypeItem != null && response.requestTypeItem.isNotEmpty()) {
                    val spinnerAdapter =
                        DocumentRequestTypesSpinnerAdapter(
                            requireContext(),
                            response.requestTypeItem
                        )
                    spinner.apply {
                        adapter = spinnerAdapter
                        onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    if (p2 != 0) {
                                        viewModel.selectedDocumentRequestType?.value =
                                            spinnerAdapter.result[p2 - 1]
                                    } else {
                                        viewModel.selectedDocumentRequestType?.value = null

                                    }
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                }
                            }
                    }

                } else {
                    val spinnerAdapter =
                        LeaveTypesSpinnerAdapter(requireContext(), Collections.emptyList())
                    spinner.apply {
                        adapter = spinnerAdapter
                    }
                }

            } else {
                CustomDialog(requireActivity()).showInformationDialog(response.message)
            }

        }
    }


    @SuppressLint("SetTextI18n")
    private fun validateLeaveTypesData(
        response: LeaveTypes,
        leaveTypesSpinner: DynamicWidthSpinner,
        leaveBalanceTextView: TextView,
        leavefrom: TextView,
        leaveto: TextView
    ) {
        if (activity != null && isAdded) {
            if (response.status == Constants.API_RESPONSE_CODE.OK) {
                if (response.data != null && response.data.isNotEmpty()) {
                    val spinnerAdapter =
                        LeaveTypesSpinnerAdapter(requireContext(), response.data)
                    leaveTypesSpinner.apply {
                        adapter = spinnerAdapter
                        onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    if (p2 != 0) {
                                        leavefrom.show()
                                        leaveto.show()
                                        viewModel.selectedLeaveType?.value =
                                            spinnerAdapter.result[p2 - 1]
                                        leaveBalanceTextView.text  = "${spinnerAdapter.result[p2 - 1]?.shortCode} Left:${spinnerAdapter.result[p2 - 1]?.balanceLeaves} of ${spinnerAdapter.result[p2 - 1]?.noOfLeaves}"
                                        leavefrom.text="From: "+"${spinnerAdapter.result[p2 - 1]?.leave_cycle_from}"
                                        leaveto.text="To: "+"${spinnerAdapter.result[p2 - 1]?.leave_cycle_to}"

                                        leaveBalanceTextView.show()
                                    } else {
                                        viewModel.selectedLeaveType?.value = null
                                        leaveBalanceTextView.hide()
                                        leavefrom.hide()
                                        leaveto.hide()

                                    }
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                }
                            }
                    }

                } else {
                    val equipmentsSpinnerAdapter =
                        LeaveTypesSpinnerAdapter(requireContext(), Collections.emptyList())
                    leaveTypesSpinner.apply {
                        adapter = equipmentsSpinnerAdapter
                    }
                }

            } else {
                CustomDialog(requireActivity()).showInformationDialog(response.message)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Leave"))
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Document"))
        if (SessionManager.username.toString()=="24" || SessionManager.username.toString()=="19")
        {
            viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Credit Request"))
        }



        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewBinding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewBinding.tabLayout.selectTab(viewBinding.tabLayout.getTabAt(position))

            }
        })

        requestFragmentAdapter = RequestFragmentAdapter(
            childFragmentManager,
            lifecycle
        )

        requestFragmentAdapter.addFragment(LeaveRequestFragment())
        requestFragmentAdapter.addFragment(DocumentRequestFragment())
        requestFragmentAdapter.addFragment(Creditrequestfragment())
        viewBinding.viewPager.adapter = requestFragmentAdapter

        viewModel.selectedFileLiveData?.observe(viewLifecycleOwner) {
            if (attachmentFileTextView != null) {
                val fileName = AppUtils.getFileNameFromPath(it)
                attachmentFileTextView?.text = fileName
            }
            if (leaveAttachmentFileTextView != null) {
                val fileName = AppUtils.getFileNameFromPath(it)
                leaveAttachmentFileTextView?.text = fileName
            }
        }
    }

    class RequestFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        val fragmentList: ArrayList<Fragment> = ArrayList()
        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        override fun getItemCount(): Int {
            return fragmentList.size
        }
    }

}
