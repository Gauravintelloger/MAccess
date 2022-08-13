package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class ApiResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)