package xyz.miyayu.android.weatherapp.network.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDesc(
    /**天気の説明*/
    @Json(name = "description") val desc: String
)
