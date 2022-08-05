package xyz.miyayu.android.weatherapp.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    @SerialName("lon") val longitude: Double,
    @SerialName("lat") val latitude: Double,
)