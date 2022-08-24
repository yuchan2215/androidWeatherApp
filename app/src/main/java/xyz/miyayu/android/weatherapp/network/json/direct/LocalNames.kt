package xyz.miyayu.android.weatherapp.network.json.direct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class LocalNames(
    @SerialName("ja") val japanese: String? = null,
    @SerialName("en") val english: String? = null
) {
    fun currentName(): String? {
        return if (Locale.getDefault() == Locale.JAPAN && japanese != null) {
            japanese
        } else {
            english
        }
    }
}
