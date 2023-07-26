package technology.dubaileading.maccessemployee.rest.entity.interviewroundlistmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("interviewRoundLists")
    val interviewRoundLists: InterviewRoundLists,
    @SerializedName("jobDetails")
    val jobDetails: List<Any>
)