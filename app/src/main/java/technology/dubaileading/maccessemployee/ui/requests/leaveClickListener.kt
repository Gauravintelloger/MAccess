package technology.dubaileading.maccessemployee.ui.requests

import technology.dubaileading.maccessemployee.rest.entity.LeaveRequestsItem

interface leaveClickListener {
    fun updateLeaveRequest(leaveRequestsItem: LeaveRequestsItem)
    fun onClick(id: Int)
}