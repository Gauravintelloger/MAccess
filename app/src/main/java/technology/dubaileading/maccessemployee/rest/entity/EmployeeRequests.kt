package technology.dubaileading.maccessemployee.rest.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmployeeRequests(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: EmployeeRequestData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class EmployeeRequestData(

	@field:SerializedName("total_other_requests")
	val totalOtherRequests: Int? = null,

	@field:SerializedName("leave_requests")
	val leaveRequests: List<LeaveRequestsItem?>? = null,

	@field:SerializedName("total_leave_requests")
	val totalLeaveRequests: Int? = null,

	@field:SerializedName("other_requests")
	val otherRequests: List<OtherRequestsItem?>? = null
) : Parcelable

@Parcelize
data class LeaveRequestsItem(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("leave_type")
	val leaveType: LeaveType? = null,

	@field:SerializedName("from_date")
	val fromDate: String? = null,

	@field:SerializedName("to_date")
	val toDate: String? = null,

	@field:SerializedName("days")
	val days: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("status")
	val status: String? = null

) : Parcelable

@Parcelize
data class LeaveType(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
) : Parcelable



@Parcelize
data class OtherRequestsItem(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("requesttype")
	val requesttype: RequestItemType? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("status_id")
	val statusId: String? = null,

) : Parcelable


@Parcelize
data class RequestItemType(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("deleted_at")
	val deleted_at: String? = null
) : Parcelable




