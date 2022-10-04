package technology.dubaileading.maccessemployee.ui.home_fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.base.BaseActivity
import technology.dubaileading.maccessemployee.base.BaseFragment
import technology.dubaileading.maccessemployee.databinding.FragmentHomeBinding
import technology.dubaileading.maccessemployee.rest.entity.LikePost
import technology.dubaileading.maccessemployee.rest.entity.PostData
import technology.dubaileading.maccessemployee.ui.check_in.CheckInActivity
import technology.dubaileading.maccessemployee.ui.check_out.CheckOutActivity
import technology.dubaileading.maccessemployee.ui.login.LoginActivity
import technology.dubaileading.maccessemployee.ui.requests.RequestsFragment
import technology.dubaileading.maccessemployee.ui.services.ServicesFragment
import technology.dubaileading.maccessemployee.utils.AppShared
import technology.dubaileading.maccessemployee.utils.TimerHelper
import technology.dubaileading.maccessemployee.utils.Utils
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment<FragmentHomeBinding,HomeFragmentViewModel>(),onLikeClickListener {

    var t : Timer = Timer()

    lateinit var timerText : TextView

    lateinit var activity: Context
    lateinit var homeAdapter: HomeAdapter

    var PERMISSION_ID = 44

    private var likePosition: Int = 0
    private lateinit var likePostData: PostData
    private var dataList = ArrayList<PostData>()

    override fun createViewModel(): HomeFragmentViewModel {
        return ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }
    private val REQ_CODE_LOCATION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeAdapter = HomeAdapter(requireContext(),this)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.rv?.itemAnimator = DefaultItemAnimator()
        binding?.rv?.layoutManager = LinearLayoutManager(activity)
        binding?.rv?.adapter = homeAdapter

        viewModel?.getPosts(requireContext())
        viewModel?.postsList?.observe(viewLifecycleOwner){
            dataList = it.data as ArrayList<PostData>
            homeAdapter.addList(dataList)
        }
        viewModel?.invalidUser?.observe(viewLifecycleOwner){
            if (it.status.equals("notok") && it.message.equals("TOKEN_EXPIRED")){
                AppShared(requireContext()).clearAll()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finishAffinity()
            }

        }



        timerText = binding?.timer!!

        val token = AppShared(activity).getToken()
        var deviceToken = Utils.getUniqueID(requireContext())
        var Token =  AppShared(activity).getFirebaseToken()
        Log.d("token", token.toString());
        Log.d("token", deviceToken.toString());

        var user = AppShared(activity as Context).getUser()
        var image = AppShared(activity as Context).getImage()
        binding?.username?.text = user.data?.name

        if (image != null){
            binding?.userImage?.load(image){
                transformations(RoundedCornersTransformation(16f))
            }
        }else{
            binding?.userImage?.load(user.data?.organisationLogo){
                transformations(RoundedCornersTransformation(16f))
            }
        }

        binding?.welcomText?.text = buildSpannedString {
            inSpans(
                ForegroundColorSpan(ContextCompat.getColor(binding?.welcomText?.context!!, R.color.wlcolor))
            ){
                append("Welcome")
            }
            append(" \uD83D\uDD90")
        }



        val locationManager = activity.getSystemService(BaseActivity.LOCATION_SERVICE) as LocationManager

        binding?.timeClock?.setOnClickListener{
            if (!checkLocationPermission()){
                requestPermission()
            }else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showAlert()
            } else {
                startActivity(Intent(activity,CheckInActivity::class.java))
            }

        }
        binding?.timeLayout?.setOnClickListener{
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(Intent(activity, CheckOutActivity::class.java))
            } else {
                showAlert()
            }

        }
        binding?.service?.setOnClickListener{
            val fragment = ServicesFragment()
            val fragmentManager: FragmentManager = requireFragmentManager()
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment, "tag")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding?.request?.setOnClickListener{
            val fragment = RequestsFragment()
            val fragmentManager: FragmentManager = requireFragmentManager()
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment, "tag")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        viewModel?.likePost?.observe(viewLifecycleOwner){
            if (dataList[likePosition].liked!!){
                dataList[likePosition].liked = false
                dataList[likePosition].likedUsersCount =  dataList[likePosition].likedUsersCount!! -1
            } else {
                dataList[likePosition].liked = true
                dataList[likePosition].likedUsersCount =  dataList[likePosition].likedUsersCount!! + 1
            }

            homeAdapter.addList(dataList)

        }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onLikeClick(postData: PostData, position: Int) {
        likePosition = position
        likePostData = postData
        var likePost = LikePost(postData.id)
        viewModel?.likePost(requireContext(),likePost)

    }


    private fun showAlert(){
        android.app.AlertDialog.Builder(activity)
            .setTitle("Alert")
            .setMessage("You need give location access to use app")
            .setPositiveButton(
                "Ok"
            ) { dialog, _ ->
            dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()

        t.cancel()

        performTimerLogic()
    }

    private fun performTimerLogic() {

        var timing = AppShared(activity as Context).getTiming()
        if(timing.equals("")){
            binding?.timeLayout?.visibility = View.GONE
            binding?.view?.visibility = View.GONE
            return
        }
        else{

        }

        val isBreakOut: Boolean = AppShared(activity as Context).isBreakOut()
        if(isBreakOut){
            binding?.timeLayout?.visibility = View.VISIBLE
            binding?.view?.visibility = View.VISIBLE

            //when is break out - no updating in ui will happen
            binding?.timer?.text = AppShared(activity as Context).getHours()

//            TickTokTimer.cancelTimer()
            t.cancel()
        }
        else{
            binding?.timeLayout?.visibility = View.VISIBLE
            binding?.view?.visibility = View.VISIBLE

            runTimer()
        }

        binding?.started?.text = "Started at ${AppShared(activity as Context).getTiming()}"
        binding?.timer?.text = AppShared(activity as Context).getHours()
    }

    override fun onDestroy() {
        super.onDestroy()
        t.cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context
    }

    private fun runTimer() {



        t = Timer()

        binding?.timeLayout?.visibility = View.VISIBLE
        binding?.view?.visibility = View.VISIBLE


        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                (activity as Activity).runOnUiThread(Runnable {
                    val savedTime = AppShared(activity as Context).getTiming()!!
                    val hours: String = AppShared(activity as Context).getHours()!!


                    if(savedTime == "") {
                        t.cancel()
                        return@Runnable
                    }

                    var timerTime = TimerHelper().findTime(savedTime,hours)

                    val h1 = timerTime.split(":").toTypedArray()
                    val hour1 = h1[0].toInt()
                    //val minute1 = h1[1].toInt()

                    if (hour1 >= 18){
                        t.cancel()
                        AppShared(activity).saveTiming("")
                        AppShared(activity).saveHours("")
                        AppShared(activity).setBreakOut(false)
                        AppShared(activity).setBreakStarted(false)
                        AppShared(activity).setTimerRunning(false)
                        binding?.timeLayout?.visibility = View.GONE
                        binding?.view?.visibility = View.GONE
                    }

                    timerText?.text = timerTime
                })
            }
        }, 0, 1000)
    }



}