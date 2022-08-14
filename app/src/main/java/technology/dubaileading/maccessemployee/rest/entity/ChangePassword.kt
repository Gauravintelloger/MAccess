package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class ChangePassword(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val dataPassword: PasswordData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PasswordData(

	@field:SerializedName("organisation_id")
	val organisationId: Any? = null,

	@field:SerializedName("status_id")
	val statusId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("role_id")
	val roleId: Int? = null,

	@field:SerializedName("old_id")
	val oldId: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("username")
	val username: String? = null
)
