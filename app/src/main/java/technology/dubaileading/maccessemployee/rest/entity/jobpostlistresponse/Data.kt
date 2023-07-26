package technology.dubaileading.maccessemployee.rest.entity.jobpostlistresponse


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    val `data`: DataX,
    @SerializedName("organisation_details")
    val organisationDetails: OrganisationDetails
)