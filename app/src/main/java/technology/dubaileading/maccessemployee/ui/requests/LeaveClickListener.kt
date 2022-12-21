package technology.dubaileading.maccessemployee.ui.requests

import technology.dubaileading.maccessemployee.rest.entity.LeaveRequestsItem

interface LeaveClickListener {
    fun updateLeaveRequest(leaveRequestsItem: LeaveRequestsItem)
    fun onDeleteClicked(id: Int)
}