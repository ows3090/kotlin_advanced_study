package ows.kotlinstudy.subway_application.domain

data class Station(
    val name: String,
    val isFavorited: Boolean,
    val connectedSubways: List<Subway>
)