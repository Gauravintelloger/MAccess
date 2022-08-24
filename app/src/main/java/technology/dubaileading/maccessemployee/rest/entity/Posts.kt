package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class Posts(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<PostData?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PostData(

	@field:SerializedName("latest_liked_users")
	val latestLikedUsers: Any? = null,

	@field:SerializedName("image_uri")
	val imageUri: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("liked_users_count")
	var likedUsersCount: Int? = null,

	@field:SerializedName("liked")
	var liked: Boolean? = null,

	@field:SerializedName("video_uri")
	val videoUri: Any? = null,

	@field:SerializedName("organisation_id")
	val organisationId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("readable_created_at")
	val readableCreatedAt: String? = null,

	@field:SerializedName("comments_off")
	val commentsOff: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("latest_comments")
	val latestComments: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)
