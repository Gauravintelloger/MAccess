package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Data(

	@field:SerializedName("status_id")
	val statusId: Int? = null,

	@field:SerializedName("role_id")
	val roleId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("organisation")
	val organisation: String? = null,

	@field:SerializedName("organisation_logo")
	val organisationLogo: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
