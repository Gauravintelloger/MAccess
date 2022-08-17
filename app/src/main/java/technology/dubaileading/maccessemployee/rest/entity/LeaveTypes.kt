package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class LeaveTypes(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: List<LeaveTypeData?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class  LeaveTypeData(

	@field:SerializedName("balance_leaves")
	val balanceLeaves: Int? = null,

	@field:SerializedName("no_of_leaves")
	val noOfLeaves: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("short_code")
	val shortCode: String? = null
)
