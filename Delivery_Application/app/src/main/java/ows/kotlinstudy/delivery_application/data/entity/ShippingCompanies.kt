package ows.kotlinstudy.delivery_application.data.entity

import com.google.gson.annotations.SerializedName

data class ShippingCompanies(

    @SerializedName("Company", alternate = ["Recommend"])
    val shippingCompanies: List<ShippingCompany>? = null
)
