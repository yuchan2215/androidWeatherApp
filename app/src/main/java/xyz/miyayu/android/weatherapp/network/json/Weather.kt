package xyz.miyayu.android.weatherapp.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    /** ステータスコード */
    @SerialName("cod") val cod: Int,
    /** 天気の説明などが含まれる */
    @SerialName("weather") val description: List<WeatherDesc>,
    /** 気温などが含まれる */
    val main: WeatherMain,
    /**緯度経度が含まれる*/
    @SerialName("coord") val coordinate: Coordinate
)