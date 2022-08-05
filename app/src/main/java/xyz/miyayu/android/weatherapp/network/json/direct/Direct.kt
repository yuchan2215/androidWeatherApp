package xyz.miyayu.android.weatherapp.network.json.direct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Direct(
    @SerialName("name") val name: String,
    @SerialName("local_names") val names: LocalNames = LocalNames(),
    @SerialName("lon") val longitude: Double,
    @SerialName("lat") val latitude: Double,
    @SerialName("state") val state: String? = null
)
