package ows.kotlinstudy.delivery_application.data.api


import ows.kotlinstudy.delivery_application.BuildConfig
import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SweetTrackerApi {

    @GET("api/v1/trackingInfo?t_key=${BuildConfig.SWEET_TRACKER_API_KEY}")
    suspend fun getTrackingInformation(
        @Query("t_code") companyCode: String,
        @Query("t_invoice") invoice: String
    ): Response<TrackingInformation>




}