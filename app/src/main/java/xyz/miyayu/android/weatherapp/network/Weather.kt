package xyz.miyayu.android.weatherapp.network

import com.squareup.moshi.Json

data class Weather(
    @Json(name = "cod") val cod: Int
)