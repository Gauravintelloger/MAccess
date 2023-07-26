package technology.dubaileading.maccessemployee.ui.interviewround

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.BaseApplication
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.databinding.ActivityDetailInterviewlistBinding
import technology.dubaileading.maccessemployee.rest.entity.addnewjobresponse.AddNewjobresponseModel
import technology.dubaileading.maccessemployee.rest.entity.interviewchangestatusmodel.Interviewchangestatusmodel
import technology.dubaileading.maccessemployee.rest.entity.interviewchangestatusmodel.Interviewchangestatusrequest
import technology.dubaileading.maccessemployee.rest.entity.interviewjobdetailmodel.Interviewjobdetailmodel
import technology.dubaileading.maccessemployee.rest.entity.jobtitle.DataModel
import technology.dubaileading.maccessemployee.rest.entity.newjobpostrequestmodel.Newjobpostrequestmodel
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.ui.applyjobform.JobQuestionViewModel
import technology.dubaileading.maccessemployee.ui.jobpost.JobPostAdaptor
import technology.dubaileading.maccessemployee.ui.manager.Managerjobpostlist
import technology.dubaileading.maccessemployee.utility.DataState
import technology.dubaileading.maccessemployee.utility.hide
import technology.dubaileading.maccessemployee.utility.show
import technology.dubaileading.maccessemployee.utility.showToast
import technology.dubaileading.maccessemployee.utils.CustomDialog
import java.util.ArrayList


@AndroidEntryPoint
class DetailInterviewlist : AppCompatActivity() {
    var isAllFieldsChecked = false

    // private val viewModel: Jobtitleviewmodel by viewModels()
    private lateinit var viewBinding: ActivityDetailInterviewlistBinding
    private lateinit var jobPostAdaptor: JobPostAdaptor
    internal lateinit var dataModelArrayList: ArrayList<DataModel>
    private val viewModel by viewModels<JobQuestionViewModel>()
    var detailid:String?=""
    var statusarray: ArrayList<String>? = null
    var completedarray: ArrayList<String>? = null
    private  var statusid:Int=0
    private  var completedid:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_interviewlist)

        statusarray= ArrayList()
        statusarray!!.add("Schedule")
        statusarray!!.add("Completed")
        statusarray!!.add("Postponed")
        statusarray!!.add("Cancelled")


        val statusadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, statusarray!!)
        viewBinding.statuschange!!.adapter = statusadapter


        viewBinding.statuschange.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (statusadapter.getItem(position).toString()=="Schedule")
                {
                    statusid=1
                    viewBinding.completedstatuslayout.visibility=View.GONE
//                    viewBinding.feedbackcardview.visibility=View.GONE
                    viewBinding.cancelledcardview.visibility=View.GONE
                }
                else if(statusadapter.getItem(position).toString()=="Completed")
                {

                    viewBinding.completedstatuslayout.visibility=View.GONE
//                    viewBinding.feedbackcardview.visibility=View.GONE
                    viewBinding.cancelledcardview.visibility=View.GONE
                    statusid=2
                }

                else if (statusadapter.getItem(position).toString()=="Postponed")
                {
                    viewBinding.completedstatuslayout.visibility=View.GONE
//                    viewBinding.feedbackcardview.visibility=View.GONE
                    viewBinding.cancelledcardview.visibility=View.GONE
                    statusid=3
                }
                else if (statusadapter.getItem(position).toString()=="Cancelled")
                {
                    viewBinding.completedstatuslayout.visibility=View.GONE
//                    viewBinding.feedbackcardview.visibility=View.VISIBLE

                    viewBinding.cancelledcardview.visibility=View.GONE
                    statusid=4
                }
                else
                {
                    viewBinding.completedstatuslayout.visibility=View.GONE
                    viewBinding.feedbackcardview.visibility=View.GONE
                    viewBinding.cancelledcardview.visibility=View.GONE
                    statusid=0
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                statusid=0
            }
        }



        ///

        completedarray= ArrayList()
        completedarray!!.add("Shortlisted")
        completedarray!!.add("Rejected")




        val completedadapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, completedarray!!)
        viewBinding.completedspinner!!.adapter = completedadapter


        viewBinding.completedspinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if (statusadapter.getItem(position).toString()=="Shortlisted")
                {
                    completedid=1

                }
                else if(statusadapter.getItem(position).toString()=="Rejected")
                {

                    completedid=2
                }

                else
                {
                    completedid=0
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                completedid=0
            }
        }

        ////


        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@DetailInterviewlist, Interviewroundlist::class.java))
            finish()
        }

        detailid=intent.getStringExtra("Username")
        Log.e("dfghjkl",   BaseApplication.QuestionObj.detailid.toString())


        intent.getStringExtra("Username")?.let { Log.e("ertyuio", it) }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@DetailInterviewlist, Interviewroundlist::class.java))
                finish()
            }
        })

        viewBinding.materialToolbar.setOnClickListener {
            startActivity(Intent(this@DetailInterviewlist, Managerjobpostlist::class.java))
            finish()
        }

        loadAllPostsFromRemote(detailid)


        viewBinding.submitMaterialButton.setOnClickListener {
            isAllFieldsChecked = CheckAllFields()
            if(isAllFieldsChecked) {
                val interviewchangestatusrequest = Interviewchangestatusrequest(
                    interviewRoundId = detailid!!.toInt(),
                    applicantStatus = completedid,
                    applicantFeedback = viewBinding.feedback.text?.trim().toString()
                )

                viewModel.interviewchangestatus(interviewchangestatusrequest)
                viewModel.interviewjobchangestatus.observe(this, postsObserverapplyjob)
            }
        }



        //    getVolley()
    }



    private var postsObserverapplyjob: Observer<DataState<Interviewchangestatusmodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {


                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()


//                    validatePostsData(it.item)

                    Toast.makeText(this,it.item.message, Toast.LENGTH_LONG).show()
                    val intent=Intent(this,Interviewroundlist::class.java)
                    startActivity(intent)
                    finish()


                }
                is DataState.Error -> {

                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                    viewBinding.progressBar.hide()
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (this as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }
    private fun loadAllPostsFromRemote(id:String?) {
        if (id != null) {
            viewModel.interviewroundjobdetail(id)
            viewModel.interviewjobdetails.observe(this, postsObserver)
        }


    }

    private var postsObserver: Observer<DataState<Interviewjobdetailmodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {

                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()

                    Log.e("dataasd",it.item.data.toString())
                    viewBinding.jobPostName.text=it.item.data.jobPostName
                    viewBinding.applicantname.text=it.item.data.applicantName
                    viewBinding.interviewer.text=it.item.data.inteviewerName
                    viewBinding.interviewround.text=it.item.data.interviewRoundNo.toString()
                    viewBinding.interviewtype.text=it.item.data.interviewType
                    viewBinding.jobLocation.text=it.item.data.interviewLocation
                    viewBinding.jobLocation.text=it.item.data.interviewLocation

//                    viewBinding.submitMaterialButton.setOnClickListener {
////                            interviewchangestatusrequest: Interviewchangestatusrequest
//
//                        val newjobpostrequestmodel = Interviewchangestatusrequest(
//                           interviewRoundId = it.id,
//                            applicantStatus=statusid,
//                            applicantFeedback = viewBinding.feedback.text!!.trim().toString()
//
//                            )
//
//
//                        viewModel.interviewchangestatus(newjobpostrequestmodel)
//                      //  viewModel.addnewjob.observe(this, postsObserverapplyjob)
//                    }



                }
                is DataState.Error -> {

                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                    viewBinding.progressBar.hide()
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (this as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }


    private  fun CheckAllFields(): Boolean {



        if (viewBinding.feedback.text?.trim()!!.isEmpty())
        {
            viewBinding.feedback.setError("This field is required");
            return false
        }



        return true

    }

}