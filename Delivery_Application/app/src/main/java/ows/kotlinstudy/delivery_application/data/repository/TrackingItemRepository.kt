package ows.kotlinstudy.delivery_application.data.repository

import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem

interface TrackingItemRepository {

    suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>>
}