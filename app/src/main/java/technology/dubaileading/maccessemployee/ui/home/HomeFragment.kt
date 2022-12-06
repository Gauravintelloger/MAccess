package technology.dubaileading.maccessemployee.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.config.Constants
import technology.dubaileading.maccessemployee.databinding.FragmentHomeBinding
import technology.dubaileading.maccessemployee.rest.entity.ApiResponse
import technology.dubaileading.maccessemployee.rest.entity.LikePost
import technology.dubaileading.maccessemployee.rest.entity.PostData
import technology.dubaileading.maccessemployee.rest.entity.Posts
import technology.dubaileading.maccessemployee.ui.HomeActivity
import technology.dubaileading.maccessemployee.utility.*
import technology.dubaileading.maccessemployee.utils.CustomDialog
import technology.dubaileading.maccessemployee.utils.TimerHelper
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(), OnLikeClickListener {

    var timer: Timer = Timer()
    private lateinit var homeAdapter: HomeAdapter
    private var likePosition: Int = 0
    private var backPressed: Long = 0L
    private lateinit var viewBinding: FragmentHomeBinding
    private val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        SessionManager.init(requireContext())
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        setUpListeners()
        initPostsRecyclerList()
        loadAllPostsFromRemote()
        return viewBinding.root
    }

    private fun loadAllPostsFromRemote() {
        viewModel.posts()
        viewModel.posts.observe(viewLifecycleOwner, postsObserver)
    }

    private var postsObserver: Observer<DataState<Posts>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validatePostsData(it.item)
                }
                is DataState.Error -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    requireContext().showToast(it.error.toString())
                }
            }
        }

    private fun validatePostsData(response: Posts) {
        if (activity != null && isAdded) {
            if (response.status == Constants.API_RESPONSE_CODE.OK) {
                if (response.data != null && response.data.isNotEmpty()) {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.show()
                    homeAdapter.updateList(response.data)
                } else {
                    homeAdapter.updateList(Collections.emptyList())

                    viewBinding.errorLayout.errorText.text = "No Data Found"
                    viewBinding.errorLayout.root.show()
                    viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                }


            } else if (response.status == Constants.API_RESPONSE_CODE.NOT_OK && response.statuscode == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                    R.string.tokenExpiredDesc
                ),
                    object : CustomDialog.OnClickListener {
                        override fun okButtonClicked() {
                            (activity as? HomeActivity?)?.logoutUser()
                        }
                    })
            } else {
                CustomDialog(requireContext()).showInformationDialog(response.message)
            }

        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }

    private fun setupData() {
        viewBinding.username.text = SessionManager.user?.username
        if (SessionManager.user?.photo != null) {
            loadLogo(SessionManager.user?.photo)
        } else {
            loadLogo(SessionManager.user?.organisationLogo)
        }

    }

    private fun setUpListeners() {
        viewBinding.timeClock.setOnClickListener {
            if (SessionManager.timing.isNullOrEmpty()) {
                findNavController().navigate(
                    R.id.checkInFragment,
                    null,
                    getNavBuilder().build()
                )
            } else {
                findNavController().navigate(
                    R.id.checkOutFragment,
                    null,
                    getNavBuilder().build()
                )

            }

        }
        viewBinding.timeLayout.setOnClickListener {
            findNavController().navigate(
                R.id.checkOutFragment,
                null,
                getNavBuilder().build()
            )

        }
        viewBinding.service.setOnClickListener {
            findNavController().navigate(
                R.id.servicesFragment,
                null,
                getNavBuilder().build()
            )
        }

        viewBinding.request.setOnClickListener {
            findNavController().navigate(
                R.id.requestsFragment,
                null,
                getNavBuilder().build()
            )
        }

        viewModel.likePost.observe(viewLifecycleOwner) {


        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitToast()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun exitToast() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            requireActivity().finish()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.pressOnceAgainToExit),
                Toast.LENGTH_SHORT
            ).show()
        }
        backPressed = System.currentTimeMillis()
    }


    override fun onResume() {
        super.onResume()

        timer.cancel()

        performTimerLogic()

    }


    private fun performTimerLogic() {
        if (SessionManager.timing.isNullOrEmpty()) {
            viewBinding.timeLayout.hide()
            viewBinding.view.hide()
            return
        }

        viewBinding.timeLayout.show()
        viewBinding.view.show()

        if (SessionManager.isBreakOut == true) {
            viewBinding.timer.text = SessionManager.hours
            timer.cancel()
        } else {
            runTimer()
        }

        viewBinding.started.text = "Started at ${SessionManager.timing}"
        viewBinding.timer.text = SessionManager.hours
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private fun runTimer() {

        timer = Timer()

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (SessionManager.timing.isNullOrEmpty()) {
                    timer.cancel()
                } else {
                    SessionManager.hours.let {
                        var hour = it
                        if (it.isNullOrEmpty()) {
                            hour = "00:00:00"
                        }
                        val timerTime =
                            TimerHelper().findTime(SessionManager.timing!!, hour!!)

                        val h1 = timerTime.split(":").toTypedArray()
                        val hour1 = h1[0].toInt()
                        if (hour1 >= 18) {
                            timer.cancel()
                            SessionManager.timing = ""
                            SessionManager.hours = ""
                            SessionManager.isBreakOut = false
                            SessionManager.isBreakStarted = false
                            SessionManager.isTimerRunning = false
                            viewBinding.timeLayout.hide()
                            viewBinding.view.hide()
                        }

                        runOnUiThread {
                            viewBinding.timer.text = timerTime
                        }
                    }

                }
            }
        }, 0, 1000)
    }

    private fun loadLogo(imageUrl: String?) {
        Glide.with(requireContext()).load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(viewBinding.userImage)
    }

    private fun initPostsRecyclerList() {
        viewBinding.recyclerView.initRecyclerView(
            DefaultItemAnimator(),
            LinearLayoutManager(context)
        )
        homeAdapter = HomeAdapter(listOf(), this@HomeFragment)
        viewBinding.recyclerView.adapter = homeAdapter
    }

    override fun onLikeCLicked(bean: PostData, position: Int) {
        likePosition = position
        viewModel.likePost(request = LikePost(bean.id))
        viewModel.likePost.observe(viewLifecycleOwner, likePostObserver)

    }

    private var likePostObserver: Observer<DataState<ApiResponse>> =
        androidx.lifecycle.Observer<DataState<ApiResponse>> {
            when (it) {
                is DataState.Loading -> {
                    requireContext().showProgress()
                }
                is DataState.Success -> {
                    requireContext().dismissProgress()
                    validateLikeResponse(it.item)
                }
                is DataState.Error -> {
                    requireContext().dismissProgress()
                    requireContext().showToast(it.error.toString())
                }
            }
        }

    private fun validateLikeResponse(body: ApiResponse) {
        if (activity != null && isAdded) {
            if (body.status == Constants.API_RESPONSE_CODE.OK) {
                if (homeAdapter.data.get(likePosition)?.liked!!) {
                    homeAdapter.data.get(likePosition)?.liked = false
                    homeAdapter.data.get(likePosition)?.likedUsersCount =
                        homeAdapter.data.get(likePosition)?.likedUsersCount!! - 1
                } else {
                    homeAdapter.data.get(likePosition)?.liked = true
                    homeAdapter.data.get(likePosition)?.likedUsersCount =
                        homeAdapter.data.get(likePosition)?.likedUsersCount!! + 1
                }
                homeAdapter.notifyItemChanged(likePosition)


            } else if (body.status == Constants.API_RESPONSE_CODE.NOT_OK && body.code == Constants.API_RESPONSE_CODE.TOKEN_EXPIRED) {
                CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                    R.string.tokenExpiredDesc
                ),
                    object : CustomDialog.OnClickListener {
                        override fun okButtonClicked() {
                            (activity as? HomeActivity?)?.logoutUser()
                        }
                    })
            } else {
                CustomDialog(requireContext()).showInformationDialog(body.message)
            }

        }
    }


}