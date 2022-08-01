package xyz.miyayu.android.weatherapp.network.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    /** ステータスコード */
    @Json(name = "cod") val cod: Int,
    /** 天気の説明などが含まれる */
    @Json(name = "weather") val description: List<WeatherDesc>,
    /** 気温などが含まれる */
    val main: WeatherMain
)