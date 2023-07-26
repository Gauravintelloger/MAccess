package technology.dubaileading.maccessemployee.rest.entity.interviewroundlistmodel


import com.google.gson.annotations.SerializedName

data class Interviewroundlistmodel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // fetched successfully
    @SerializedName("status")
    val status: String, // ok
    @SerializedName("statuscode")
    val statuscode: Int // 200
)