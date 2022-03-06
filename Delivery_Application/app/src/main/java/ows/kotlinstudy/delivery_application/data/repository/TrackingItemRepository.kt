package ows.kotlinstudy.delivery_application.data.repository

import ows.kotlinstudy.delivery_application.data.entity.TrackingInformation
import ows.kotlinstudy.delivery_application.data.entity.TrackingItem

/**
 * Repository Interface 사용
 * 1. 실제 데이터를 받아오지 못하는 경우 새로운 Stub 객체를 통해 화면을 미리 구성 가능
 * 2. 새로운 RepositoryImpl을 정의하는데 해당 인터페이스만 맞추면 쉽게 가능 -> 유지보수가 좋음
 */
interface TrackingItemRepository {

    suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>>
}