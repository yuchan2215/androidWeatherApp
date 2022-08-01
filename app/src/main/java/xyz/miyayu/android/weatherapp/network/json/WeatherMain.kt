package xyz.miyayu.android.weatherapp.network.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class WeatherMain(
    /**気温 */
    @Json(name = "temp") val tempData: Double,
    /**体感気温*/
    @Json(name = "feels_like") val feelTempData: Double
) {
    // ケルビン温度を変換する。
    private fun tempConvert(temp: Double): Double {
        val japanTemp = temp - 273.15
        return (japanTemp * 10.0).roundToInt() / 10.0
    }

    fun getTemp(): Double {
        return tempConvert(tempData)
    }

    fun getFeelTemp(): Double {
        return tempConvert(feelTempData)
    }

}