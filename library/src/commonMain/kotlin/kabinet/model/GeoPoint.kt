package kabinet.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val longitude: Double,
    val latitude: Double,
)