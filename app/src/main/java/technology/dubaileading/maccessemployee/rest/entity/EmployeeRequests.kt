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
data class LeaveType(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
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
data class OtherRequestsItem(

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("doc_url_to_admin")
	val docUrlToAdmin: String? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null,

	@field:SerializedName("doc_uri_to_employee")
	val docUriToEmployee: String? = null,

	@field:SerializedName("organisation_id")
	val organisationId: Int? = null,

	@field:SerializedName("requesttype")
	val requesttype: RequestItemType? = null,

	@field:SerializedName("status_id")
	val statusId: String? = null,

	@field:SerializedName("required_by")
	val requiredBy: String? = null,

	@field:SerializedName("doc_uri_from_employee")
	val docUriFromEmployee: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("doc_url_from_admin")
	val docUrlFromAdmin: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("request_type_id")
	val requestTypeId: Int? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable

@Parcelize
data class LeaveRequestsItem(

	@field:SerializedName("from_date")
	val fromDate: String? = null,

	@field:SerializedName("is_settled")
	val isSettled: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("leave_type_id")
	val leaveTypeId: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: String? = null,

	@field:SerializedName("organisation_id")
	val organisationId: Int? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("to_date")
	val toDate: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("added_by")
	val addedBy: Int? = null,

	@field:SerializedName("leave_type")
	val leaveType: LeaveType? = null,

	@field:SerializedName("employee_profile_id")
	val employeeProfileId: Int? = null,

	@field:SerializedName("old_id")
	val oldId: Int?? = null,

	@field:SerializedName("days")
	val days: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class Employee(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("employee_id")
	val employeeId: String? = null,

	@field:SerializedName("name")
	val name: String? = null
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


