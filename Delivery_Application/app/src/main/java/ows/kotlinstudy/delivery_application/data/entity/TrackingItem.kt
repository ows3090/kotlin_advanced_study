package ows.kotlinstudy.delivery_application.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.parcelize.Parcelize


/**
 * safe args로 객체 전달하기 위해서는 Parcelize 지정
 */
@Parcelize
@Entity(primaryKeys = ["invoice","code"])
data class TrackingItem(
    val invoice: String,
    @Embedded val company: ShippingCompany
): Parcelable