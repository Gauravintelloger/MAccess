package technology.dubaileading.maccessemployee.ui.requests

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.braver.tool.picker.BraverDocPathUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentDocumentRequestBinding
import technology.dubaileading.maccessemployee.rest.entity.*
import technology.dubaileading.maccessemployee.utility.SessionManager
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.AppUtils
import technology.dubaileading.maccessemployee.utils.Constants
import technology.dubaileading.maccessemployee.utils.PermissionUtils
import java.text.SimpleDateFormat
import java.util.*

class DocumentRequestFragment  : BaseFragment<FragmentDocumentRequestBinding, RequestsViewModel>(),documentClickListener {
    private lateinit var documentRequestAdapter: DocumentRequestAdapter
    private var otherRequestsItem = ArrayList<OtherRequestsItem>()
    private lateinit var docUpdate : BottomSheetDialog
    private var cal = Calendar.getInstance()
    private var attachmentFileTextView: TextView? = null
    private var docTypeList = java.util.ArrayList<RequestTypeItem>()
    private val dateFormat = "dd-MM-yyyy"
    private var doc_type_id: Int? = null
    private var reqDate: String = ""
    private var downloadId : Long = 0


    override fun createViewModel(): RequestsViewModel {
        return ViewModelProvider(this).get(RequestsViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentDocumentRequestBinding {
        return FragmentDocumentRequestBinding.inflate(layoutInflater!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        documentRequestAdapter = DocumentRequestAdapter(requireContext(),this)
        otherRequestsItem = arguments?.getParcelableArrayList<OtherRequestsItem>(Constants.OTHER_REQUEST)!!
        documentRequestAdapter.setData(otherRequestsItem)
        requireActivity().registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.documentRv?.itemAnimator = DefaultItemAnimator()
        binding?.documentRv?.layoutManager = LinearLayoutManager(activity)
        binding?.documentRv?.adapter = documentRequestAdapter

        viewModel?.getRequestTypes(requireContext())

        viewModel?.requestTypesSuccess?.observe(viewLifecycleOwner) {
            docTypeList = it.requestTypeItem as java.util.ArrayList<RequestTypeItem>
        }

        viewModel?.selectedFileLiveData?.observe(viewLifecycleOwner) {
            if (attachmentFileTextView != null){
                val fileName = AppUtils.getFileNameFromPath(it)
                attachmentFileTextView?.text = fileName
            }
        }


        viewModel?.documentRequestSuccess?.observe(viewLifecycleOwner){
            docUpdate.dismiss()
            Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_LONG).show()
            var getRequests = GetRequests(20, 1)
            viewModel?.getEmployeeRequests(requireContext(), getRequests)
        }


        viewModel?.documentRequestError?.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }

        viewModel?.deleteDocRequestSuccess?.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            var getRequests = GetRequests(20, 1)
            viewModel?.getEmployeeRequests(requireContext(), getRequests)
        }

        viewModel?.deleteDocRequestError?.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }

        viewModel?.getRequestSuccess?.observe(viewLifecycleOwner) {
            otherRequestsItem = it.data?.otherRequests as ArrayList<OtherRequestsItem>
            documentRequestAdapter.setData(otherRequestsItem)
        }

    }

    override fun updateDocRequest(otherRequestsItem: OtherRequestsItem) {
        showUpdateDailog(otherRequestsItem)

    }

    private fun showUpdateDailog(otherRequestsItem: OtherRequestsItem) {
        val btnsheet = layoutInflater.inflate(R.layout.document_request, null)
        docUpdate = BottomSheetDialog(requireContext())

        val spinnerDocType = btnsheet.findViewById<AppCompatSpinner>(R.id.spinnerDocType)
        val dateCard = btnsheet.findViewById<CardView>(R.id.dateCard)
        val date = btnsheet.findViewById<EditText>(R.id.date)
        val email = btnsheet.findViewById<EditText>(R.id.email)
        val attachCardView = btnsheet.findViewById<CardView>(R.id.attachCardView)
        val desc = btnsheet.findViewById<EditText>(R.id.desc)
        val remove = btnsheet.findViewById<ImageView>(R.id.remove)
        attachmentFileTextView = btnsheet.findViewById<TextView>(R.id.attachmentFileTextView)

        val submitBt = btnsheet.findViewById<AppCompatButton>(R.id.submitBt)

        date.setText(otherRequestsItem.required_by)
        desc.setText(otherRequestsItem.description)

        var docType = java.util.ArrayList<String>()
        for (i in docTypeList.indices) {
            docType.add(docTypeList[i].type.toString())
        }
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, docType)
        spinnerDocType.adapter = arrayAdapter

        for (i in 0 until spinnerDocType.count) {
            if (spinnerDocType.getItemAtPosition(i).equals(otherRequestsItem.requesttype?.type)) {
                spinnerDocType.setSelection(i)
                break
            }
        }

        doc_type_id = otherRequestsItem.requesttype?.id

        spinnerDocType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val docType = docTypeList[i].type
                doc_type_id = docTypeList[i].id

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                reqDate = sdf.format(cal.time)
                date.setText(reqDate)
            }
        var datePicker = DatePickerDialog(
            requireActivity(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = cal.timeInMillis


        date.setOnClickListener {
            datePicker.show()
        }

        requireActivity().let {
            SessionManager.init(it)
        }

        SessionManager.user.let {
            email.setText(it?.username)
        }

        attachCardView.setOnClickListener {
            callFileAccessIntent()
        }

        remove.setOnClickListener {
            viewModel?.clearAttachment()
        }


        submitBt.setOnClickListener {
            val description = desc?.text?.toString()?.trim()
            var updateDocumentRequest = UpdateDocumentRequest("Document Request", description, "", doc_type_id, reqDate,otherRequestsItem.id)
            viewModel?.updateDocumentRequest(requireContext(), updateDocumentRequest)
        }

        docUpdate.setContentView(btnsheet)
        docUpdate.show()


    }

    private fun callFileAccessIntent() {
        if (PermissionUtils.isFileAccessPermissionGranted(requireContext())) {
            openAttachments()
        } else {
            PermissionUtils.showFileAccessPermissionRequestDialog(requireContext())
        }
    }

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

    var activityResultLauncherForDocs =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                try {
                    //String tempFileAbsolutePath = "";
                    val selectedDocUri = result.data!!.data
                    if (selectedDocUri.toString().contains("video") || selectedDocUri.toString()
                            .contains("mp4") || selectedDocUri.toString()
                            .contains("mkv") || selectedDocUri.toString().contains("mov")
                    ) {

                        setSourcePathView(selectedDocUri.toString())
                    } else {
                        var tempFileAbsolutePath = BraverDocPathUtils.Companion.getSourceDocPath(
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
        viewModel?.selectedFileLiveData?.value = path

    }

    override fun onClick(id: Int) {
        android.app.AlertDialog.Builder(activity)
            .setTitle("Alert")
            .setMessage("Do you want to delete the request?")
            .setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                var deleteReq = DeleteReq(id)
                viewModel?.deleteDocRequest(requireContext(),deleteReq)
                dialog.dismiss()
            }
            .setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    override fun downloadDoc(otherRequestsItem: OtherRequestsItem) {
        if (PermissionUtils.isFileWritePermissionGranted(requireContext())) {
            download(otherRequestsItem)
        } else {
            PermissionUtils.showFileWritePermissionRequestDialog(requireContext())
        }

    }

    private fun download(otherRequestsItem: OtherRequestsItem) {
        if (otherRequestsItem.doc_url_from_admin != null){
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val nowTime = sdf.format(Date())
            var docLink = Uri.parse(otherRequestsItem.doc_url_from_admin)
            var request = DownloadManager.Request(docLink)
                .setTitle(nowTime)
                .setDescription("Download")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
            var downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = downloadManager.enqueue(request)

        } else {
            Toast.makeText(requireContext(),"No Attachment is Available for Download",Toast.LENGTH_LONG).show()
        }


    }

    var receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            var id:Long? = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
            if (id == downloadId){
                Toast.makeText(requireContext(),"Download Completed",Toast.LENGTH_LONG).show()
            }

        }

    }



}