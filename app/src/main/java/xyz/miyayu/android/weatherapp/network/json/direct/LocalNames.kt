package xyz.miyayu.android.weatherapp.network.json.direct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalNames(
    @SerialName("ja") val japanese: String? = null,
    @SerialName("en") val english: String? = null
)
