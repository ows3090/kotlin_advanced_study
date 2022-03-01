package ows.kotlinstudy.subway_application.data.api.response


import com.google.gson.annotations.SerializedName

data class ErrorMessage(
    @SerializedName("code")
    val code: String?,
    @SerializedName("developerMessage")
    val developerMessage: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("total")
    val total: Int?
)