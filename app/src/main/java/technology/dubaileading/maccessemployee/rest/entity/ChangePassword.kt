package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class ChangePassword(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)


