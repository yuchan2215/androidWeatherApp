package xyz.miyayu.android.weatherapp.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDesc(
    /**天気の説明*/
    @SerialName("description") val desc: String
)
