package ows.kotlinstudy.subway_application.data.api.response


import com.google.gson.annotations.SerializedName

data class RealtimeStationArrivals(
    @SerializedName("errorMessage")
    val errorMessage: ErrorMessage?,
    @SerializedName("realtimeArrivalList")
    val realtimeArrivalList: List<RealtimeArrival>?
)