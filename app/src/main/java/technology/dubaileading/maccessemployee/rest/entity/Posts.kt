package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class Posts(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<PostData?>? = null
)

data class LatestLikedUsers(

	@field:SerializedName("status_id")
	val statusId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("role_id")
	val roleId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class PostData(

	@field:SerializedName("latest_liked_users")
	val latestLikedUsers: List<LatestLikedUsers?>? = null,

	@field:SerializedName("image_uri")
	val imageUri: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("liked_users_count")
	val likedUsersCount: Int? = null,

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
	val latestComments: List<Any?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Pivot(

	@field:SerializedName("is_dislike")
	val isDislike: Int? = null,

	@field:SerializedName("post_id")
	val postId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)
