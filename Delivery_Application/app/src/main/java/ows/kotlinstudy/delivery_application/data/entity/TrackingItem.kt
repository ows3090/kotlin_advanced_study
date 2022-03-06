package ows.kotlinstudy.delivery_application.data.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["invoice","code"])
data class TrackingItem(
    val invoice: String,
    @Embedded val company: ShippingCompany
)