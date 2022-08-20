package technology.dubaileading.maccessemployee.ui.requests


import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import technology.dubaileading.maccessemployee.rest.entity.LeaveRequestsItem
import technology.dubaileading.maccessemployee.rest.entity.OtherRequestsItem
import technology.dubaileading.maccessemployee.utils.Constants


class RequestFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val leaveRequests: List<LeaveRequestsItem?>?,
    val otherRequests: List<OtherRequestsItem?>?
) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = LeaveRequestFragment()
                val bundle = Bundle()
                bundle.putParcelableArrayList(
                    Constants.LEAVE_REQUEST,
                    leaveRequests as ArrayList<out Parcelable?>?
                )
                fragment!!.arguments = bundle
            }
            1 -> {
                fragment = DocumentRequestFragment()
                val bundle1 = Bundle()
                bundle1.putParcelableArrayList(
                    Constants.OTHER_REQUEST,
                    otherRequests as ArrayList<out Parcelable?>?
                )
                fragment!!.arguments = bundle1
            }
        }
        return fragment!!
    }


    override fun getItemCount(): Int {
        return 2
    }

}