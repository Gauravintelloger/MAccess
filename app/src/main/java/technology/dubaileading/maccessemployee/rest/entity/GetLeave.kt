package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class GetLeave(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: LeaveData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LeaveData(

	@field:SerializedName("sum_available_leave")
	val sumAvailableLeave: Int? = null,

	@field:SerializedName("leave_requested")
	val leaveRequested: Int? = null,

	@field:SerializedName("sum_total_leave")
	val sumTotalLeave: Int? = null,

	@field:SerializedName("leave_data")
	val leaveData: List<LeaveDataItem?>? = null
)

data class LeaveDataItem(

	@field:SerializedName("remaining_leaves")
	val remainingLeaves: Int? = null,

	@field:SerializedName("total_leaves")
	val totalLeaves: Int? = null,

	@field:SerializedName("leave_code")
	val leaveCode: String? = null
)
