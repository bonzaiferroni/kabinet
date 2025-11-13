package kabinet.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val x: Double,
    val y: Double,
) {
    val longitude get() = x
    val latitude get() = y

    fun toList(): List<Double> = listOf(latitude, longitude)
}