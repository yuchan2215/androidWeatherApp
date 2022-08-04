package xyz.miyayu.android.weatherapp.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class WeatherMain(
    /**気温 */
    @SerialName("temp") val tempData: Double,
    /**体感気温*/
    @SerialName("feels_like") val feelTempData: Double
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