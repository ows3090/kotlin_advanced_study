package ows.kotlinstudy.delivery_application.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ows.kotlinstudy.delivery_application.data.entity.Level
import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany
import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem

class TrackingItemRepositoryStub: TrackingItemRepository {

    override val trackingItems: Flow<List<TrackingItem>> = flowOf(emptyList())

    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        listOf(
            TrackingItem("1", ShippingCompany("1","대한통운")) to TrackingInformation(itemName = "운동화", level = Level.START),
            TrackingItem("1", ShippingCompany("1","대한통운")) to TrackingInformation(itemName = "장난감", level = Level.START),
            TrackingItem("1", ShippingCompany("1","대한통운")) to TrackingInformation(itemName = "옷", level = Level.START),
            TrackingItem("1", ShippingCompany("1","대한통운")) to TrackingInformation(itemName = "컴퓨터", level = Level.START)
        )


    override suspend fun saveTrackingItem(trackingItem: TrackingItem) = Unit

    override suspend fun getTrackingInformation(
        companyCode: String,
        invoice: String
    ): TrackingInformation? = null

    override suspend fun deleteTrackingItem(trackingItem: TrackingItem) = Unit
}